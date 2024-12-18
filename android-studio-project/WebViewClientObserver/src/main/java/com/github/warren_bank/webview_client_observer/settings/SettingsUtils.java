package com.github.warren_bank.webview_client_observer.settings;

import org.json.JSONArray;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingsUtils {

  public static SharedPreferences getPrefs(Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context);
  }

  // --------------------

  public static boolean getPref(String name, Context context) {
    return getPref(name, getPrefs(context));
  }

  private static boolean getPref(String name, SharedPreferences prefs) {
    String  pref_key     = name.toLowerCase() + "_key";
    boolean pref_default = true;

    return prefs.getBoolean(pref_key, pref_default);
  }

  // --------------------

  public static JSONArray getTogglesValues(Context context) {
    return getTogglesValues(getPrefs(context));
  }

  private static String[] togglesList = new String[] {"onPageStarted", "onPageFinished", "doUpdateVisitedHistory", "onFormResubmission", "onReceivedClientCertRequest", "onReceivedError", "onReceivedHttpAuthRequest", "onReceivedHttpError", "onReceivedLoginRequest", "onReceivedSslError", "onRenderProcessGone", "onSafeBrowsingHit", "onScaleChanged", "onTooManyRedirects", "onUnhandledKeyEvent", "shouldInterceptRequest", "shouldOverrideKeyEvent", "shouldOverrideUrlLoading", "onLoadResource", "onPageCommitVisible"};

  public static JSONArray getTogglesValues(SharedPreferences prefs) {
    JSONArray togglesValues = new JSONArray();

    for (String name : togglesList) {
      togglesValues.put(
        getPref(name, prefs)
      );
    }

    return togglesValues;
  }

}
