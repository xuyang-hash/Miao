package com.meowing.loud.arms.integration;

import android.app.Activity;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

public class MoreClickManager {
    private static final int MORE_CLICK_TIME = 1000;
    private static MoreClickManager manager;
    private Map<Object, Bundle> clickTimeMap = new HashMap();

    private MoreClickManager() {
    }

    public static MoreClickManager getInstance() {
        Class var0 = MoreClickManager.class;
        synchronized(MoreClickManager.class) {
            if (manager == null) {
                manager = new MoreClickManager();
            }

            return manager;
        }
    }

    public int addClick(Activity activity, final Object key) {
        if (activity != null && !activity.hasWindowFocus()) {
            return 2147483647;
        } else {
            long oldClickTime = 0L;
            int clickCount = 0;
            Bundle oldBundle;
            if (this.clickTimeMap.containsKey(key)) {
                oldBundle = (Bundle)this.clickTimeMap.get(key);
                if (oldBundle != null) {
                    oldClickTime = oldBundle.getLong("clickTime");
                    clickCount = oldBundle.getInt("clickCount");
                }

                if (System.currentTimeMillis() - oldClickTime < 1000L) {
                    ++clickCount;
                    return clickCount;
                } else {
                    oldBundle.putLong("clickTime", System.currentTimeMillis());
                    oldBundle.putInt("clickCount", 1);
                    return 1;
                }
            } else {
                oldBundle = new Bundle();
                oldBundle.putLong("clickTime", System.currentTimeMillis());
                oldBundle.putInt("clickCount", 1);
                this.clickTimeMap.put(key, oldBundle);
                return 1;
            }
        }
    }

    public boolean isMoreClick(final Object key) {
        long oldClickTime = 0L;
        int clickCount = 0;
        Bundle oldBundle;
        if (this.clickTimeMap.containsKey(key)) {
            oldBundle = (Bundle)this.clickTimeMap.get(key);
            if (oldBundle != null) {
                oldClickTime = oldBundle.getLong("clickTime");
                clickCount = oldBundle.getInt("clickCount");
            }

            if (System.currentTimeMillis() - oldClickTime < 1000L) {
                ++clickCount;
                return clickCount > 1;
            } else {
                oldBundle.putLong("clickTime", System.currentTimeMillis());
                oldBundle.putInt("clickCount", 1);
                return false;
            }
        } else {
            oldBundle = new Bundle();
            oldBundle.putLong("clickTime", System.currentTimeMillis());
            oldBundle.putInt("clickCount", 1);
            this.clickTimeMap.put(key, oldBundle);
            return false;
        }
    }

    public void release() {
        if (this.clickTimeMap != null) {
            this.clickTimeMap.clear();
        }

    }
}

