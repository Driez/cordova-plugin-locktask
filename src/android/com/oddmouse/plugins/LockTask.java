package com.oddmouse.plugins;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

public class LockTask extends CordovaPlugin {

  private static final String ACTION_START_LOCK_TASK = "startLockTask";
  private static final String ACTION_STOP_LOCK_TASK = "stopLockTask";

  private Activity activity = null;
  private final WindowInsetsControllerCompat windowInsetsController =
          WindowCompat.getInsetsController(activity.getWindow(), activity.getWindow().getDecorView());
  private boolean immersive;

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    activity = cordova.getActivity();
    ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
    String adminClassName = "";

    adminClassName = args.getString(0);
    JSONArray whitelist = args.getJSONArray(1);
    immersive = immersive || args.getBoolean(3);




    try {
      if (ACTION_START_LOCK_TASK.equals(action)) {

        if (!activityManager.isInLockTaskMode()) {

          if (!adminClassName.isEmpty()) {

            DevicePolicyManager mDPM = (DevicePolicyManager) activity.getSystemService(Context.DEVICE_POLICY_SERVICE);
            ComponentName mDeviceAdmin = new ComponentName(activity.getPackageName(), activity.getPackageName() + "." + adminClassName);

            if (mDPM.isDeviceOwnerApp(activity.getPackageName())) {

              String[] packages = new String[whitelist.length() + 1];
              for (int i = 0; i < whitelist.length(); i++) {
                packages[i] = whitelist.getString(i);
              }
              packages[whitelist.length()] = activity.getPackageName();
              mDPM.setLockTaskPackages(mDeviceAdmin, packages);
            }

          }
          if (immersive) {
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
          }
          activity.startLockTask();
        }

        callbackContext.success();

        return true;

      } else if (ACTION_STOP_LOCK_TASK.equals(action)) {

        if (activityManager.isInLockTaskMode()) {
          activity.stopLockTask();
          if (immersive) {
            windowInsetsController.show(WindowInsetsCompat.Type.systemBars());
            immersive = false;
          }
        }

        callbackContext.success();
        return true;

      } else {

        callbackContext.error("The method '" + action + "' does not exist.");
        return false;

      }
    } catch (Exception e) {

      callbackContext.error(e.getMessage());
      return false;

    }
  }
}
