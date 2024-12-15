package com.github.warren_bank.webview_client_observer;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SafeBrowsingResponse;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.security.Principal;
import java.util.Map;

public class WebViewClientObserver extends WebViewClient {
  private static final String JAVASCRIPT_LOGGER_METHOD;
  static {
    StringBuffer sb = new StringBuffer();

    sb.append("window.logWebViewClientObserver = window.logWebViewClientObserver || function(data) {");
    sb.append("  var blockCss = 'display: block; margin: 0; padding: 0; width: 100%; border-style: none;';");
    sb.append("  var container = document.getElementById('containerWebViewClientObserver');");
    sb.append("  if (!container) {");
    sb.append("    container = document.createElement('div');");
    sb.append("    container.setAttribute('id', 'containerWebViewClientObserver');");
    sb.append("    container.setAttribute('style', blockCss);");
    sb.append("    container.style.position = 'relative';");
    sb.append("    container.style.top = '0px';");
    sb.append("    container.style.left = '0px';");
    sb.append("    container.style.zIndex = '9999';");
    sb.append("    container.style.backgroundColor = 'white';");
    sb.append("    container.style.color = 'black';");
    sb.append("    container.style.fontFamily = 'monospace';");
    sb.append("    container.style.fontSize = '12px';");
    sb.append("    container.style.height = 'auto';");
//  sb.append("    container.style.maxHeight = '40em';");
//  sb.append("    container.style.overflow = 'auto';");
    sb.append("    if (document.body.childNodes && document.body.childNodes.length)");
    sb.append("      document.body.insertBefore(container, document.body.childNodes[0]);");
    sb.append("    else");
    sb.append("      document.body.appendChild(container);");

    sb.append("    var togglesList = ['onPageStarted', 'onPageFinished', 'doUpdateVisitedHistory', 'onFormResubmission', 'onReceivedClientCertRequest', 'onReceivedError', 'onReceivedHttpAuthRequest', 'onReceivedHttpError', 'onReceivedLoginRequest', 'onReceivedSslError', 'onRenderProcessGone', 'onSafeBrowsingHit', 'onScaleChanged', 'onTooManyRedirects', 'onUnhandledKeyEvent', 'shouldInterceptRequest', 'shouldOverrideKeyEvent', 'shouldOverrideUrlLoading', 'onLoadResource', 'onPageCommitVisible'];");

    sb.append("    var toggle_item_handler = function(index, event) {");
    sb.append("      event.preventDefault(); event.stopImmediatePropagation();");
    sb.append("      var display = event.target.checked ? 'block' : 'none';");
    sb.append("      var logs = document.querySelectorAll('#containerWebViewClientObserver pre.' + togglesList[index]);");
    sb.append("      for (var j=0; j < logs.length; j++) {");
    sb.append("        logs[j].style.display = display;");
    sb.append("      }");
    sb.append("    };");

    sb.append("    var toggles = document.createElement('div');");
    sb.append("    toggles.setAttribute('style', blockCss);");
    sb.append("    var toggle, toggle_item, toggle_label;");
    sb.append("    if (true) {");
    sb.append("      toggle = document.createElement('div');");
    sb.append("      toggle.setAttribute('style', blockCss);");
    sb.append("      toggle_item = document.createElement('button');");
    sb.append("      toggle_item.checked = true;");
    sb.append("      toggle_item.textContent = 'Hide All';");
    sb.append("      toggle_item.addEventListener('click', function(event) {");
    sb.append("        event.preventDefault(); event.stopImmediatePropagation();");
    sb.append("        event.target.checked = !event.target.checked;");
    sb.append("        event.target.textContent = event.target.checked ? 'Hide All' : 'Show All';");
    sb.append("        var changeEvent = new Event('change');");
    sb.append("        var checkbox;");
    sb.append("        for (var i=0; i < togglesList.length; i++) {");
    sb.append("          checkbox = document.getElementById('toggle_' + togglesList[i]);");
    sb.append("          checkbox.checked = event.target.checked;");
    sb.append("          checkbox.dispatchEvent(changeEvent);");
    sb.append("        }");
    sb.append("      });");
    sb.append("      toggle.appendChild(toggle_item);");
    sb.append("      toggles.appendChild(toggle);");
    sb.append("    }");
    sb.append("    for (var i=0; i < togglesList.length; i++) {");
    sb.append("      toggle = document.createElement('div');");
    sb.append("      toggle.setAttribute('style', blockCss);");
    sb.append("      toggle_item = document.createElement('input');");
    sb.append("      toggle_item.setAttribute('type', 'checkbox');");
    sb.append("      toggle_item.setAttribute('id', 'toggle_' + togglesList[i]);");
    sb.append("      toggle_item.checked = true;");
    sb.append("      toggle_item.addEventListener('change', toggle_item_handler.bind(null, i));");
    sb.append("      toggle_label = document.createElement('label');");
    sb.append("      toggle_label.textContent = togglesList[i];");
    sb.append("      toggle.appendChild(toggle_item);");
    sb.append("      toggle.appendChild(toggle_label);");
    sb.append("      toggles.appendChild(toggle);");
    sb.append("    }");
    sb.append("    container.appendChild(toggles);");
    sb.append("  }");

    sb.append("  var log = document.createElement('pre');");
    sb.append("  log.setAttribute('style', blockCss);");
    sb.append("  if (data && (typeof data === 'object') && data['Event'] && (typeof data['Event'] === 'string')) {");
    sb.append("    log.className = data['Event'];");
    sb.append("    log.style.display = document.getElementById('toggle_' + data['Event']).checked ? 'block' : 'none';");
    sb.append("  }");
    sb.append("  log.style.borderTop = '1px solid black';");
    sb.append("  log.textContent = JSON.stringify(data, null, 2);");
    sb.append("  container.appendChild(log);");
    sb.append("};\n");

    JAVASCRIPT_LOGGER_METHOD = sb.toString();
  }

  private static void log(WebView webView, JSONObject jsonObj) throws Exception {
    JSONObject jsonWebView = new JSONObject();

    jsonWebView.put("progress",    webView.getProgress());
    jsonWebView.put("url",         webView.getUrl());
    jsonWebView.put("originalUrl", webView.getOriginalUrl());

    jsonObj.put("WebView", jsonWebView);

    String jsCode = "window.logWebViewClientObserver(" + jsonObj.toString() + ");";

    if (Build.VERSION.SDK_INT >= 19) {
      webView.evaluateJavascript(
        "typeof window.logWebViewClientObserver",
        new ValueCallback<String>() {
          @Override
          public void	onReceiveValue(String value) {
            if ("function".equals(value)) {
              webView.evaluateJavascript(jsCode, null);
            }
            else {
              webView.evaluateJavascript(JAVASCRIPT_LOGGER_METHOD + jsCode, null);
            }
          }
        }
      );
    }
    else {
      webView.loadUrl("javascript:" + JAVASCRIPT_LOGGER_METHOD + jsCode);
    }
  }

  private static void onGenericEvent(String name, WebView view, String url) {
    try {
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("Event", name);

      JSONObject jsonParams = new JSONObject();
      jsonParams.put("url", url);

      jsonObj.put("Parameters", jsonParams);

      log(view, jsonObj);
    }
    catch(Exception e) {}
  }

  private static void putWebResourceRequest(JSONObject jsonParams, WebResourceRequest request) throws Exception {
    JSONObject jsonReq = new JSONObject();
    jsonReq.put("url", request.getUrl().toString());
    jsonReq.put("method", request.getMethod());

    JSONObject jsonReqHeaders = new JSONObject();
    Map<String, String> mapReqHeaders = request.getRequestHeaders();
    if (mapReqHeaders != null) {
      jsonReqHeaders = new JSONObject(mapReqHeaders);
    }
    jsonReq.put("headers", jsonReqHeaders);

    jsonReq.put("hasGesture", request.hasGesture());
    jsonReq.put("isForMainFrame", request.isForMainFrame());
    jsonReq.put("isRedirect", request.isRedirect());

    jsonParams.put("request", jsonReq);
  }

  private static void putWebResourceResponse(JSONObject jsonParams, WebResourceResponse errorResponse) throws Exception {
    JSONObject jsonResp = new JSONObject();
    jsonResp.put("statusCode", errorResponse.getStatusCode());
    jsonResp.put("statusReason", errorResponse.getReasonPhrase());

    JSONObject jsonRespHeaders = new JSONObject();
    Map<String, String> mapRespHeaders = errorResponse.getResponseHeaders();
    if (mapRespHeaders != null) {
      jsonRespHeaders = new JSONObject(mapRespHeaders);
    }
    jsonResp.put("headers", jsonRespHeaders);

    jsonResp.put("mimeType", errorResponse.getMimeType());
    jsonResp.put("encoding", errorResponse.getEncoding());

    jsonParams.put("response", jsonResp);
  }

  private static void putKeyEvent(JSONObject jsonParams, KeyEvent event) throws Exception {
    JSONObject jsonKeyEvent = new JSONObject();
    jsonKeyEvent.put("characters", event.getCharacters());
    jsonKeyEvent.put("unicodeChar", event.getUnicodeChar());
    jsonKeyEvent.put("keyCode", event.getKeyCode());
    jsonKeyEvent.put("maxKeyCode", event.getMaxKeyCode());
    jsonKeyEvent.put("eventTime", event.getEventTime());
    jsonKeyEvent.put("repeatCount", event.getRepeatCount());

    String action = null;
    switch (event.getAction()) {
      case KeyEvent.ACTION_DOWN :
        action = "DOWN";
        break;
      case KeyEvent.ACTION_UP :
        action = "UP";
        break;
      case KeyEvent.ACTION_MULTIPLE :
        action = "MULTIPLE";
        break;
    }
    jsonKeyEvent.put("action", action);

    int metaStateFlags = event.getMetaState();
    JSONObject jsonMetaState = new JSONObject();
    jsonMetaState.put("META_ALT_LEFT_ON", (metaStateFlags & KeyEvent.META_ALT_LEFT_ON) == KeyEvent.META_ALT_LEFT_ON);
    jsonMetaState.put("META_ALT_ON", (metaStateFlags & KeyEvent.META_ALT_ON) == KeyEvent.META_ALT_ON);
    jsonMetaState.put("META_ALT_RIGHT_ON", (metaStateFlags & KeyEvent.META_ALT_RIGHT_ON) == KeyEvent.META_ALT_RIGHT_ON);
    jsonMetaState.put("META_CAPS_LOCK_ON", (metaStateFlags & KeyEvent.META_CAPS_LOCK_ON) == KeyEvent.META_CAPS_LOCK_ON);
    jsonMetaState.put("META_CTRL_LEFT_ON", (metaStateFlags & KeyEvent.META_CTRL_LEFT_ON) == KeyEvent.META_CTRL_LEFT_ON);
    jsonMetaState.put("META_CTRL_ON", (metaStateFlags & KeyEvent.META_CTRL_ON) == KeyEvent.META_CTRL_ON);
    jsonMetaState.put("META_CTRL_RIGHT_ON", (metaStateFlags & KeyEvent.META_CTRL_RIGHT_ON) == KeyEvent.META_CTRL_RIGHT_ON);
    jsonMetaState.put("META_FUNCTION_ON", (metaStateFlags & KeyEvent.META_FUNCTION_ON) == KeyEvent.META_FUNCTION_ON);
    jsonMetaState.put("META_META_LEFT_ON", (metaStateFlags & KeyEvent.META_META_LEFT_ON) == KeyEvent.META_META_LEFT_ON);
    jsonMetaState.put("META_META_ON", (metaStateFlags & KeyEvent.META_META_ON) == KeyEvent.META_META_ON);
    jsonMetaState.put("META_META_RIGHT_ON", (metaStateFlags & KeyEvent.META_META_RIGHT_ON) == KeyEvent.META_META_RIGHT_ON);
    jsonMetaState.put("META_NUM_LOCK_ON", (metaStateFlags & KeyEvent.META_NUM_LOCK_ON) == KeyEvent.META_NUM_LOCK_ON);
    jsonMetaState.put("META_SCROLL_LOCK_ON", (metaStateFlags & KeyEvent.META_SCROLL_LOCK_ON) == KeyEvent.META_SCROLL_LOCK_ON);
    jsonMetaState.put("META_SHIFT_LEFT_ON", (metaStateFlags & KeyEvent.META_SHIFT_LEFT_ON) == KeyEvent.META_SHIFT_LEFT_ON);
    jsonMetaState.put("META_SHIFT_ON", (metaStateFlags & KeyEvent.META_SHIFT_ON) == KeyEvent.META_SHIFT_ON);
    jsonMetaState.put("META_SHIFT_RIGHT_ON", (metaStateFlags & KeyEvent.META_SHIFT_RIGHT_ON) == KeyEvent.META_SHIFT_RIGHT_ON);
    jsonMetaState.put("META_SYM_ON", (metaStateFlags & KeyEvent.META_SYM_ON) == KeyEvent.META_SYM_ON);
    jsonKeyEvent.put("MetaState", jsonMetaState);

    jsonParams.put("KeyEvent", jsonKeyEvent);
  }

  @Override
  public void doUpdateVisitedHistory (WebView view, String url, boolean isReload) {
    try {
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("Event", "doUpdateVisitedHistory");

      JSONObject jsonParams = new JSONObject();
      jsonParams.put("url", url);
      jsonParams.put("isReload", isReload);

      jsonObj.put("Parameters", jsonParams);

      log(view, jsonObj);
    }
    catch(Exception e) {}
  }

  @Override
  public void onFormResubmission (WebView view, Message dontResend, Message resend) {
    try {
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("Event", "onFormResubmission");

      JSONObject jsonParams = new JSONObject();
      jsonParams.put("dontResend", (dontResend == null) ? null : dontResend.toString());
      jsonParams.put("resend",     (resend     == null) ? null : resend.toString());

      jsonObj.put("Parameters", jsonParams);

      log(view, jsonObj);
    }
    catch(Exception e) {}
  }

  @Override
  public void onLoadResource (WebView view, String url) {
    onGenericEvent("onLoadResource", view, url);
  }

  @Override
  public void onPageCommitVisible (WebView view, String url) {
    onGenericEvent("onPageCommitVisible", view, url);
  }

  @Override
  public void onPageFinished (WebView view, String url) {
    onGenericEvent("onPageFinished", view, url);
  }

  @Override
  public void onPageStarted (WebView view, String url, Bitmap favicon) {
    onGenericEvent("onPageStarted", view, url);
  }

  @Override
  public void onReceivedClientCertRequest (WebView view, ClientCertRequest request) {
    try {
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("Event", "onReceivedClientCertRequest");

      JSONObject jsonCertReq = new JSONObject();
      jsonCertReq.put("host", request.getHost());
      jsonCertReq.put("keyTypes", new JSONArray(request.getKeyTypes()));
      jsonCertReq.put("port", request.getPort());

      JSONArray principals = new JSONArray();
      Principal[] pArray = request.getPrincipals();
      if (pArray != null) {
        for (Principal p : pArray) {
          principals.put(p.toString());
        }
      }
      jsonCertReq.put("principals", principals);

      JSONObject jsonParams = new JSONObject();
      jsonParams.put("ClientCertRequest", jsonCertReq);

      jsonObj.put("Parameters", jsonParams);

      log(view, jsonObj);
    }
    catch(Exception e) {}
  }

  @Override
  public void onReceivedError (WebView view, int errorCode, String description, String failingUrl) {
    try {
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("Event", "onReceivedError");

      JSONObject jsonParams = new JSONObject();
      jsonParams.put("errorCode", errorCode);
      jsonParams.put("description", description);
      jsonParams.put("failingUrl", failingUrl);

      jsonObj.put("Parameters", jsonParams);

      log(view, jsonObj);
    }
    catch(Exception e) {}
  }

  @Override
  public void onReceivedError (WebView view, WebResourceRequest request, WebResourceError error) {
    try {
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("Event", "onReceivedError");

      JSONObject jsonParams = new JSONObject();
      jsonParams.put("errorCode", error.getErrorCode());
      jsonParams.put("description", error.getDescription());
      jsonParams.put("failingUrl", request.getUrl().toString());
      putWebResourceRequest(jsonParams, request);

      jsonObj.put("Parameters", jsonParams);

      log(view, jsonObj);
    }
    catch(Exception e) {}
  }

  @Override
  public void onReceivedHttpAuthRequest (WebView view, HttpAuthHandler handler, String host, String realm) {
    try {
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("Event", "onReceivedHttpAuthRequest");

      JSONObject jsonParams = new JSONObject();
      jsonParams.put("host", host);
      jsonParams.put("realm", realm);

      jsonObj.put("Parameters", jsonParams);

      log(view, jsonObj);
    }
    catch(Exception e) {}
  }

  @Override
  public void onReceivedHttpError (WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
    try {
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("Event", "onReceivedHttpError");

      JSONObject jsonParams = new JSONObject();
      jsonParams.put("failingUrl", request.getUrl().toString());
      putWebResourceRequest(jsonParams, request);
      putWebResourceResponse(jsonParams, errorResponse);

      jsonObj.put("Parameters", jsonParams);

      log(view, jsonObj);
    }
    catch(Exception e) {}
  }

  @Override
  public void onReceivedLoginRequest (WebView view, String realm, String account, String args) {
    try {
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("Event", "onReceivedLoginRequest");

      JSONObject jsonParams = new JSONObject();
      jsonParams.put("realm", realm);
      jsonParams.put("account", account);
      jsonParams.put("args", args);

      jsonObj.put("Parameters", jsonParams);

      log(view, jsonObj);
    }
    catch(Exception e) {}
  }

  @Override
  public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
    try {
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("Event", "onReceivedSslError");

      JSONObject jsonParams = new JSONObject();
      jsonParams.put("url", error.getUrl());
      jsonParams.put("error", error.toString());
      jsonParams.put("certificate", error.getCertificate().toString());

      jsonObj.put("Parameters", jsonParams);

      log(view, jsonObj);
    }
    catch(Exception e) {}
  }

  @Override
  public boolean onRenderProcessGone (WebView view, RenderProcessGoneDetail detail) {
    try {
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("Event", "onRenderProcessGone");

      JSONObject jsonParams = new JSONObject();
      jsonParams.put("didCrash", detail.didCrash());
      jsonParams.put("rendererPriorityAtExit", detail.rendererPriorityAtExit());

      jsonObj.put("Parameters", jsonParams);

      log(view, jsonObj);
    }
    catch(Exception e) {}
    return true; // app is dead, but prevent it from closing to allow the log to remain visible
  }

  @Override
  public void onSafeBrowsingHit (WebView view, WebResourceRequest request, int threatType, SafeBrowsingResponse callback) {
    try {
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("Event", "onSafeBrowsingHit");

      JSONObject jsonParams = new JSONObject();
      putWebResourceRequest(jsonParams, request);
      jsonParams.put("threatType", threatType);

      jsonObj.put("Parameters", jsonParams);

      log(view, jsonObj);
    }
    catch(Exception e) {}
  }

  @Override
  public void onScaleChanged (WebView view, float oldScale, float newScale) {
    try {
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("Event", "onScaleChanged");

      JSONObject jsonParams = new JSONObject();
      jsonParams.put("oldScale", oldScale);
      jsonParams.put("newScale", newScale);

      jsonObj.put("Parameters", jsonParams);

      log(view, jsonObj);
    }
    catch(Exception e) {}
  }

  @Override
  public void onTooManyRedirects (WebView view, Message cancelMsg, Message continueMsg) {
    try {
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("Event", "onTooManyRedirects");

      JSONObject jsonParams = new JSONObject();
      jsonParams.put("cancelMsg",   (cancelMsg   == null) ? null : cancelMsg.toString());
      jsonParams.put("continueMsg", (continueMsg == null) ? null : continueMsg.toString());

      jsonObj.put("Parameters", jsonParams);

      log(view, jsonObj);
    }
    catch(Exception e) {}
  }

  @Override
  public void onUnhandledKeyEvent (WebView view, KeyEvent event) {
    try {
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("Event", "onUnhandledKeyEvent");

      JSONObject jsonParams = new JSONObject();
      putKeyEvent(jsonParams, event);

      jsonObj.put("Parameters", jsonParams);

      log(view, jsonObj);
    }
    catch(Exception e) {}
  }

  @Override
  public WebResourceResponse shouldInterceptRequest (WebView view, WebResourceRequest request) {
    try {
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("Event", "shouldInterceptRequest");

      JSONObject jsonParams = new JSONObject();
      putWebResourceRequest(jsonParams, request);

      jsonObj.put("Parameters", jsonParams);

      log(view, jsonObj);
    }
    catch(Exception e) {}
    return null;
  }

  @Override
  public WebResourceResponse shouldInterceptRequest (WebView view, String url) {
    onGenericEvent("shouldInterceptRequest", view, url);
    return null;
  }

  @Override
  public boolean shouldOverrideKeyEvent (WebView view, KeyEvent event) {
    try {
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("Event", "shouldOverrideKeyEvent");

      JSONObject jsonParams = new JSONObject();
      putKeyEvent(jsonParams, event);

      jsonObj.put("Parameters", jsonParams);

      log(view, jsonObj);
    }
    catch(Exception e) {}
    return false;
  }

  @Override
  public boolean shouldOverrideUrlLoading (WebView view, WebResourceRequest request) {
    try {
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("Event", "shouldOverrideUrlLoading");

      JSONObject jsonParams = new JSONObject();
      putWebResourceRequest(jsonParams, request);

      jsonObj.put("Parameters", jsonParams);

      log(view, jsonObj);
    }
    catch(Exception e) {}
    return false;
  }

  @Override
  public boolean shouldOverrideUrlLoading (WebView view, String url) {
    onGenericEvent("shouldOverrideUrlLoading", view, url);
    return false;
  }
}
