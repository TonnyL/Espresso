package io.github.marktony.espresso.appwidget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.mvp.packagedetails.PackageDetailsActivity;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.Sort;

import static io.github.marktony.espresso.data.source.local.PackagesLocalDataSource.DATABASE_NAME;

/**
 * Created by lizhaotailang on 2017/3/11.
 */

public class WidgetListFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context context;

    private String statusError;

    private String[] packageStatus;

    public WidgetListFactory(Context context) {
        this.context = context;
        statusError = context.getString(R.string.get_status_error);
        packageStatus = context.getResources().getStringArray(R.array.package_status);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return Realm.getInstance(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(DATABASE_NAME)
                .build())
                .where(Package.class)
                .notEqualTo("state", String.valueOf(Package.STATUS_DELIVERED))
                .findAllSorted("timestamp", Sort.DESCENDING)
                .size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(), R.layout.package_item_for_widget);

        Realm rlm = Realm.getInstance(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(DATABASE_NAME)
                .build());
        List<Package> results = rlm.copyFromRealm(rlm.where(Package.class)
                .notEqualTo("state", String.valueOf(Package.STATUS_DELIVERED))
                .findAllSorted("timestamp", Sort.DESCENDING));

        Package p = results.get(position);

        if (p.getData() != null && p.getData().size() > 0) {
            int state = Integer.parseInt(p.getState());
            remoteViews.setTextViewText(R.id.textViewStatus,
                    String.valueOf(packageStatus[state]) + " - " + p.getData().get(0).getContext());
            remoteViews.setTextViewText(R.id.textViewTime, p.getData().get(0).getTime());
        } else {
            remoteViews.setTextViewText(R.id.textViewTime, "");
            remoteViews.setTextViewText(R.id.textViewStatus, statusError);
        }

        remoteViews.setTextViewText(R.id.textViewPackageName, p.getName());
        remoteViews.setTextViewText(R.id.textViewAvatar, p.getName().substring(0, 1));
        remoteViews.setImageViewResource(R.id.imageViewAvatar, p.getColorAvatar());

        Intent intent = new Intent();
        intent.putExtra(PackageDetailsActivity.PACKAGE_ID, p.getNumber());
        remoteViews.setOnClickFillInIntent(R.id.layoutPackageItemMain, intent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
