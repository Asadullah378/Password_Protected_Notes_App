package com.asadullahnawaz.i200761;

import android.provider.BaseColumns;

public class MyContracts {
    public static final String DATABASE_NAME="NotesDB";
    public static final int DATABASE_VERSION=1;

    public static class Note implements BaseColumns {
        public static String TABLE_NAME="note";
        public static String _ID="id";
        public static String _TITLE="title";
        public static String _DESCRIPTION="description";
        public static String _IMAGE="image";
        public static String _LOCK_STATUS = "lock_status";
        public static String _PASSWORD = "password";
        public static String _CATEGORY_ID = "category_id";
        public static String _DATE_CREATED = "date_created";
        public static String _DATE_UPDATED = "date_updated";
        public static String _COLOR = "color";
    }

    public static class Category implements BaseColumns {
        public static String TABLE_NAME="category";
        public static String _ID="id";
        public static String _TITLE="title";

    }


}
