package io.github.marktony.espresso.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.appwidget.AppWidgetProvider;
import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.mvp.packagedetails.PackageDetailsActivity;
import io.github.marktony.espresso.retrofit.RetrofitClient;
import io.github.marktony.espresso.retrofit.RetrofitService;
import io.github.marktony.espresso.ui.SettingsFragment;
import io.github.marktony.espresso.util.NetworkUtil;
import io.github.marktony.espresso.util.PushUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static io.github.marktony.espresso.data.source.local.PackagesLocalDataSource.DATABASE_NAME;

/**
 * Created by lizhaotailang on 2017/3/8.
 * Background service to build notifications and send them to user.
 */

public class ReminderService extends IntentService {

    private SharedPreferences preference;

    private CompositeDisposable compositeDisposable;

    public static final String TAG = ReminderService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public ReminderService() {
        super(TAG);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        compositeDisposable = new CompositeDisposable();
        Log.d(TAG, "onCreate: ");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent: ");

        boolean alert = preference.getBoolean(SettingsFragment.KEY_ALERT, true);
        boolean isDisturbMode = preference.getBoolean(SettingsFragment.KEY_DO_NOT_DISTURB_MODE, true);

        // The alert mode is off
        // or DO-NOT-DISTURB-MODE is off
        // or time now is not in the DO-NOT-DISTURB-MODE range.
        if (!alert || (isDisturbMode && PushUtils.isInDisturbTime(this, Calendar.getInstance()))) {
            return;
        }

        Realm rlm = Realm.getInstance(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(DATABASE_NAME)
                .build());

        List<Package> results = rlm.copyFromRealm(
                rlm.where(Package.class)
                        .notEqualTo("state", String.valueOf(Package.STATUS_DELIVERED))
                        .findAll());

        for (int i = 0; i < results.size(); i++) {
            if (NetworkUtil.isNetworkConnected(getApplicationContext())) {
                refreshPackage(i, results.get(i));
            } else {

                Package p = results.get(i);
                // Avoid repeated pushing
                if (p.isPushable()) {

                    setNotifications(i, results.get(i));

                    Realm realm = Realm.getInstance(new RealmConfiguration.Builder()
                            .deleteRealmIfMigrationNeeded()
                            .name(DATABASE_NAME)
                            .build());

                    p.setPushable(false);

                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(p);
                    realm.commitTransaction();
                    realm.close();
                }
            }
        }

        rlm.close();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        Log.d(TAG, "onDestroy: ");
    }

    private static Notification buildNotification(Context context, String title, String subject, String longText, String time, int icon, int color,
                                                  PendingIntent contentIntent, PendingIntent deleteIntent) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(subject);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setStyle(new NotificationCompat.BigTextStyle(builder).bigText(longText));
        builder.setSmallIcon(icon);
        builder.setShowWhen(true);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentIntent(contentIntent);
        builder.setSubText(time);
        builder.setAutoCancel(true);
        builder.setColor(color);

        return builder.build();

    }

    /**
     * Set the details like title, subject, etc. of notifications.
     * @param position Position.
     * @param pkg The package.
     * @return The notification.
     */
    private Notification setNotifications(int position, Package pkg) {
        if (pkg != null) {

            Intent i = new Intent(getApplicationContext(), PackageDetailsActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.putExtra(PackageDetailsActivity.PACKAGE_ID, pkg.getNumber());

            PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), position, i, PendingIntent.FLAG_UPDATE_CURRENT);

            String title = pkg.getName();
            String subject;
            int smallIcon = R.drawable.ic_local_shipping_teal_24dp;
            if (Integer.parseInt(pkg.getState()) == Package.STATUS_DELIVERED) {
                subject = getString(R.string.delivered);
                smallIcon = R.drawable.ic_assignment_turned_in_teal_24dp;
            } else {
                if (Integer.parseInt(pkg.getState()) == Package.STATUS_ON_THE_WAY) {
                    subject = getString(R.string.on_the_way);
                } else {
                    subject = getString(R.string.notification_new_message);
                }
            }

            Notification notification = buildNotification(getApplicationContext(),
                    title,
                    subject,
                    pkg.getData().get(0).getContext(),
                    pkg.getData().get(0).getTime(),
                    smallIcon,
                    ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary),
                    intent,
                    null);

            notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            notification.tickerText = title;

            return notification;
        }
        return null;
    }

    /**
     * Update the package by accessing network,
     * then build and send the notifications.
     * @param position Position.
     * @param p The package.
     */
    private void refreshPackage(final int position, final Package p) {

        Log.d(TAG, "refreshPackage: ");
        final NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .getPackageState(p.getCompany(), p.getNumber())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<Package>() {
                    @Override
                    public void accept(Package aPackage) throws Exception {

                        if (aPackage != null && aPackage.getData().size() > p.getData().size()) {

                            Realm rlm = Realm.getInstance(new RealmConfiguration.Builder()
                                    .deleteRealmIfMigrationNeeded()
                                    .name(DATABASE_NAME)
                                    .build());

                            p.setReadable(true);
                            p.setPushable(false);
                            p.setData(aPackage.getData());

                            rlm.beginTransaction();
                            rlm.copyToRealmOrUpdate(p);
                            rlm.commitTransaction();
                            rlm.close();

                            // Send notification
                            nm.notify(position + 1000, setNotifications(position, p));

                            // Update the widget
                            AppWidgetProvider.updateManually(getApplication());
                        }
                    }
                })
                .subscribeWith(new DisposableObserver<Package>() {
                    @Override
                    public void onNext(Package value) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
        });
    }

}
