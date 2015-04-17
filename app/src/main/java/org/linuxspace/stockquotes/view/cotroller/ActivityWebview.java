package org.linuxspace.stockquotes.view.cotroller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import org.linuxspace.stockquotes.R;

public class ActivityWebview extends ActionBarActivity {

    private Toolbar mActionBarToolbar;
    private WebView wbNews;
    private ProgressBarCircularIndeterminate pbNewsLoadingBar;
    private String newsLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        mActionBarToolbar.setTitle(R.string.stock_news);
        mActionBarToolbar.setNavigationIcon(R.drawable.icon_toolbal_arrow_white);
        pbNewsLoadingBar = (ProgressBarCircularIndeterminate) findViewById(R.id.pbLoadingNews);
        setSupportActionBar(mActionBarToolbar);
        setWebView();
    }

    private void setWebView() {
        wbNews = (WebView) findViewById(R.id.wbNews);
        wbNews.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pbNewsLoadingBar.setVisibility(View.VISIBLE);
                wbNews.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbNewsLoadingBar.setVisibility(View.GONE);
                wbNews.setVisibility(View.VISIBLE);
            }
        });
        newsLink = getIntent().getExtras().getString(ActivityDetails.INTENT_EXTRA_URL);
        wbNews.loadUrl(newsLink);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_webview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_browser) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(newsLink));
            startActivity(i);
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
