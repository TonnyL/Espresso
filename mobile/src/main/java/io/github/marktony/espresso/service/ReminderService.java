package io.github.marktony.espresso.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by lizhaotailang on 2017/3/8.
 */

public class ReminderService extends IntentService {

    public ReminderService() {
        super(ReminderService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

}
