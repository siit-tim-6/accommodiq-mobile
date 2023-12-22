package com.example.accommodiq.apiConfig;

import android.content.Context;
import android.content.SharedPreferences;

public class JwtUtils {
    private static final String PREF_NAME = "jwt_pref";
    private static final String KEY_JWT = "jwt_key";
    private static final String KEY_ROLE = "role_key";

    public static void saveJwt(Context context, String jwt) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_JWT, jwt);
        editor.apply();
    }

    public static String getJwt(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_JWT, null);
    }

    public static void saveRole(Context context, String role) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ROLE, role);
        editor.apply();
    }

    public static String getRole(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ROLE, null);
    }
}
