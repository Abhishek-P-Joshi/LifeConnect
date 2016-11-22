package com.example.amank.lifeconnect.db;

/**
 * Created by Yogesh on 11/13/2016.
 */

import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "com.example.yogesh.lifeconnect.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "notes";

        public static final String COL_TASK_TITLE = "title";
    }
}