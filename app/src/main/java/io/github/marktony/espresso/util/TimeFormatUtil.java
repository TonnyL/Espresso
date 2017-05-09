/*
 *  Copyright(c) 2017 lizhaotailang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.marktony.espresso.util;

import android.support.annotation.Nullable;

/**
 * Created by lizhaotailang on 2017/3/17.
 */

public class TimeFormatUtil {

    public static String formatTimeIntToString(int hour, int minute) {
        StringBuilder buffer = new StringBuilder(16);
        if (hour <= 9) {
            buffer.append("0");
        }
        buffer.append(hour).append(":");
        if (minute <= 9) {
            buffer.append("0");
        }
        buffer.append(minute);

        return buffer.toString();
    }

    @Nullable
    public static int[] formatTimeStringToIntArray(String timeString) {
        try {
            String[] times = timeString.split(":");
            int[] timeInts = new int[4];
            for (int i = 0; i < timeInts.length; i++) {
                timeInts[i] = Integer.parseInt(times[i]);
            }
            return timeInts;
        } catch (Exception ex) {
            return null;
        }
    }

}
