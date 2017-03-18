package io.github.marktony.espresso.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.data.source.PackagesRepository;
import io.github.marktony.espresso.data.source.local.PackagesLocalDataSource;
import io.github.marktony.espresso.data.source.remote.PackagesRemoteDataSource;
import io.github.marktony.espresso.mvp.packagedetails.PackageDetailsActivity;
import io.github.marktony.espresso.retrofit.RetrofitClient;
import io.github.marktony.espresso.retrofit.RetrofitService;
import io.github.marktony.espresso.ui.SettingsFragment;
import io.github.marktony.espresso.util.PushUtils;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static io.github.marktony.espresso.data.source.local.PackagesLocalDataSource.DATABASE_NAME;

/**
 * Created by lizhaotailang on 2017/3/8.
 * Background service to build notifications and send them to user.
 */

public class ReminderService extends Service {

    private SharedPreferences preference;

    private CompositeDisposable compositeDisposable;

    public static final String TAG = ReminderService.class.getSimpleName();

    public ReminderService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        compositeDisposable = new CompositeDisposable();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean alert = preference.getBoolean(SettingsFragment.KEY_ALERT, true);
        boolean isDisturbMode = preference.getBoolean(SettingsFragment.KEY_DO_NOT_DISTURB_MODE, true);

        // The alert mode is off
        // or DO-NOT-DISTURB-MODE is off
        // or time now is not in the DO-NOT-DISTURB-MODE range.
        if (!alert || (isDisturbMode && PushUtils.isInDisturbTime(this, Calendar.getInstance()))) {
            return super.onStartCommand(intent, flags, startId);
        }

        final Realm rlm = Realm.getInstance(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(DATABASE_NAME)
                .build());

        final List<Package> results = rlm.copyFromRealm(
                rlm.where(Package.class)
                        .notEqualTo("state", String.valueOf(Package.STATUS_DELIVERED))
                        .findAll());

        final NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        for (int i = 0; i < results.size(); i++) {
            final Package pkg = results.get(i);
            synchronized (this) {
                if (compositeDisposable.isDisposed()) {
                    compositeDisposable.clear();
                }
                final int finalI = i;
                Disposable disposable = Observable
                        .interval(PushUtils.getIntervalTime(Integer.parseInt(android.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(SettingsFragment.KEY_NOTIFICATION_INTERVAL, "1"))), TimeUnit.MILLISECONDS)
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                RetrofitClient.getInstance()
                                        .create(RetrofitService.class)
                                        .getPackageState(pkg.getCompany(), pkg.getNumber())
                                        .subscribe(new Consumer<Package>() {
                                            @Override
                                            public void accept(Package aPackage) throws Exception {

                                                if (aPackage.getData() != null
                                                        && aPackage.getData().size() > pkg.getData().size()) {

                                                    pkg.setData(aPackage.getData());
                                                    pkg.setReadable(true);

                                                    rlm.beginTransaction();
                                                    rlm.copyToRealmOrUpdate(pkg);
                                                    rlm.commitTransaction();
                                                    rlm.close();
                                                    nm.notify(finalI, setNotifications(finalI, pkg));
                                                }
                                            }
                                        });
                            }
                        });
                compositeDisposable.add(disposable);
            }
        }
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private static Notification buildNotification(Context context, String title, String subject, String longText, String time, int icon, int color,
                                                  PendingIntent contentIntent, PendingIntent deleteIntent) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(subject);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setStyle(new NotificationCompat.BigTextStyle(builder).bigText(longText));
        builder.setSmallIcon(icon);
        builder.setContentIntent(contentIntent);
        builder.setSubText(time);
        builder.setAutoCancel(true);
        builder.setColor(color);

        return builder.build();

    }

    private Notification setNotifications(int position, Package pkg) {
        if (pkg != null) {

            Intent i = new Intent(getApplicationContext(), PackageDetailsActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.putExtra(PackageDetailsActivity.PACKAGE_ID, pkg.getNumber());

            PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), position, i, PendingIntent.FLAG_UPDATE_CURRENT);

            String title = pkg.getName();
            String subject;
            if (Integer.parseInt(pkg.getState()) == Package.STATUS_DELIVERED) {
                subject = getString(R.string.delivered);
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
                    R.drawable.ic_local_shipping_black_24dp,
                    R.color.colorPrimary,
                    intent,
                    null);

            notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            notification.tickerText = title;

            return notification;
        }
        return null;
    }

}
