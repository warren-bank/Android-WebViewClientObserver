package com.github.warren_bank.webview_client_observer.settings;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (Build.VERSION.SDK_INT >= 11) {
      getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }
    else {
      Toast.makeText(
        SettingsActivity.this,
        "Android 3.0 or higher is required to edit the app settings",
        Toast.LENGTH_SHORT
      ).show();

      finish();
    }
  }
}
