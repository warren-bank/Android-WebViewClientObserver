### [WebViewClient Observer](https://github.com/warren-bank/Android-WebViewClientObserver)

A little Android utility app for the purpose of observing [`WebViewClient`](https://developer.android.com/reference/android/webkit/WebViewClient) events.

Debug information for the page loaded in the [`WebView`](https://developer.android.com/reference/android/webkit/WebView) is displayed as HTML in a prepended DOM element.

Though generally informative, the purpose for this app is to debug [this issue](https://issuetracker.google.com/issues/36983315).
Specifically, that the `onPageStarted` and `onPageFinished` events are called multiple times during a single page load.
This issue is also discussed in depth [here](https://stackoverflow.com/questions/18282892/android-webview-onpagefinished-called-twice).

#### Legal

* copyright: [Warren Bank](https://github.com/warren-bank)
* license: [GPL-2.0](https://www.gnu.org/licenses/old-licenses/gpl-2.0.txt)
