package io.github.marktony.espresso.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;

import io.github.marktony.espresso.service.ReminderService;
import io.github.marktony.espresso.ui.SettingsFragment;

/**
 * Created by lizhaotailang on 2017/3/18.
 */

public class PushUtils {

    public static final String TAG = PushUtils.class.getSimpleName();

    public static void startAlarmService(Context context, Class<?> service, long interval) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, service);
        PendingIntent pendingIntent = PendingIntent.getService(context, 10000, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, 0, interval, pendingIntent);
        Log.d(TAG, "startAlarmService");
    }

    public static void stopAlarmService(Context context, Class<?> service) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, service);
        PendingIntent pendingIntent = PendingIntent.getService(context, 10000, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        manager.cancel(pendingIntent);
        Log.d(TAG, "stopAlarmService");
    }

    public static void startReminderService(Context context) {
        // Default value is 30 minutes
        int intervalTime = getIntervalTime(Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context)
                .getString(SettingsFragment.KEY_NOTIFICATION_INTERVAL, "1")));
        if (intervalTime > -1) {
            startAlarmService(context, ReminderService.class, intervalTime);
            Log.d(TAG, "startReminderService: interval time" + intervalTime);
        }
    }

    public static void stopReminderService(Context context) {
        stopAlarmService(context, ReminderService.class);
    }

    public static void restartReminderService(Context context) {
        stopReminderService(context);
        startReminderService(context);
    }

    public static boolean isInDisturbTime(Context context, Calendar calendar) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int startHour = sp.getInt(SettingsFragment.KEY_DO_NOT_DISTURB_MODE_START_HOUR, 23);
        int startMinute = sp.getInt(SettingsFragment.KEY_DO_NOT_DISTURB_MODE_START_MINUTE, 0);
        int endHour = sp.getInt(SettingsFragment.KEY_DO_NOT_DISTURB_MODE_END_HOUR, 6);
        int endMinute = sp.getInt(SettingsFragment.KEY_DO_NOT_DISTURB_MODE_END_MINUTE, 0);
        int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
        int nowMinute = calendar.get(Calendar.MINUTE);
        return (nowHour >= startHour && nowMinute >= startMinute) && (nowHour <= endHour && nowMinute <= endMinute);
    }

    public static int getIntervalTime(int id) {
        switch (id) {
            case 0:
                return 10 * 60 * 1000; // 10 minutes
            case 1:
                return 30 * 60 * 1000; // 30 minutes
            case 2:
                return 60 * 60 * 1000; // 1 hour
            case 3:
                return 90 * 60 * 1000; // 1.5 hours
            case 4:
                return 120 * 60 * 1000; // 2 hours
            default:
                return -1;
        }
    }

}
