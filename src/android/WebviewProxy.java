package com.gi.cordova.plugin.webviewproxy;

import android.util.Log;
import android.webkit.WebResourceResponse;

import com.nordnetab.cordova.ul.parser.ULConfigXmlParser;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaPluginPathHandler;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import androidx.webkit.WebViewAssetLoader;

public class WebviewProxy extends CordovaPlugin  {

  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);

    Log.d("WebviewProxy", "Proxy init");
  }

  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
  
    if (action.equals("getServerBasePath")) {
      callbackContext.success("test");
      return true;
    }
    return false;
  }

  public CordovaPluginPathHandler getPathHandler() {
    WebViewAssetLoader.PathHandler pathHandler = new WebViewAssetLoader.PathHandler() {
      @Override
      public WebResourceResponse handle(String path) {
        try {
          URL url = new URL("https://" + path);
          HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
          try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String mimeType = urlConnection.getContentType();
            return new WebResourceResponse(mimeType, null, in);
          } finally {
            //urlConnection.disconnect();
          }
        } catch(Exception e) {
          Log.e("WebviewProxy", e.getMessage());
        }
        return null;
      }
    };

    Log.d("WebviewProxy", "Add proxy");
    return new CordovaPluginPathHandler("/_https_proxy_/", pathHandler);
  }

}
