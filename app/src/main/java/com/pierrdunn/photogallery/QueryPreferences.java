package com.pierrdunn.photogallery;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by pierrdunn on 11.03.18.
 * Класс чтения/записи поискового запроса в хранилище общих настроек
 */

public class QueryPreferences {
    public static final String PREF_SEARCH_QUERY = "searchQuery";

    public static String getStoredQuery(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_QUERY, null);
    }

    public static void setStoredQuery(Context context, String query){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SEARCH_QUERY, query)
                .apply();
    }
}
