package com.example.exam_progect;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsUtils {
    public static class WriteExternalStorage {
        private static final int REQUEST_WRITE_STORAGE = 120;

        private final static String[] WRITE_EXTERNAL_STORAGE_PERMISSION = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        public static boolean isNeedRequest(Activity activity) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;
            final int permissionCheck = ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return !(permissionCheck == PackageManager.PERMISSION_GRANTED);
        }

        public static void request(Activity activity) {
            ActivityCompat.requestPermissions(activity, WRITE_EXTERNAL_STORAGE_PERMISSION,
                    REQUEST_WRITE_STORAGE);
        }
    }
}
