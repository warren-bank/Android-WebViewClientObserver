package com.github.warren_bank.webview_client_observer;

import com.github.warren_bank.webview_client_observer.R;
import com.github.warren_bank.webview_client_observer.WebViewClientObserver;
import com.github.warren_bank.webview_client_observer.settings.SettingsActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

public class BrowserActivity extends Activity {

  private EditText addressField;
  private WebView  webView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_browser);

    addressField = (EditText) findViewById(R.id.addressField);
    webView      = (WebView)  findViewById(R.id.webView);

    addressField.setOnEditorActionListener(
      new EditText.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
          if ((actionId == EditorInfo.IME_ACTION_GO) || (actionId == EditorInfo.IME_NULL)) {
            String url = v.getText().toString();

            webView.stopLoading();
            webView.loadUrl(url);
            webView.requestFocus();

            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
            return true;
          }
          return false;
        }
      }
    );

    webView.getSettings().setJavaScriptEnabled(true);

    webView.setWebViewClient(
      new WebViewClientObserver(this)
    );

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_browser, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem menuItem) {
    switch(menuItem.getItemId()) {
      case R.id.menu_settings: {
        Intent in = new Intent(BrowserActivity.this, SettingsActivity.class);
        startActivity(in);
        return true;
      }
    }
    return false;
  }

}
