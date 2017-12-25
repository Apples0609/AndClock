package cn.smiles.andclock.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smiles.andclock.R;

public class WebViewActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {

    @BindView(R.id.wv_webview)
    WebView wvWebview;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        // Title
        toolbar.setTitle("WebView");
        setSupportActionBar(toolbar);
        // Navigation Icon 要設定在 setSupoortActionBar 才有作用
        // 否則會出現 back button
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(this);
        toolbar.setOnMenuItemClickListener(this);

        initData();
    }

    private void initData() {
        WebSettings settings = wvWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        //Hardware Acceleration， enable hardware acceleration using code (>= level 11)
        wvWebview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //Zoom Controls
        settings.setBuiltInZoomControls(false);
        settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        settings.setSupportZoom(true);
        //Setting Default Font Size
        settings.setDefaultFontSize(20);
        settings.setPluginState(WebSettings.PluginState.ON);
        wvWebview.setWebViewClient(new WebViewClient() {
            // override all the methods
        });
        wvWebview.setWebChromeClient(new WebChromeClient() {
            // override all the methods
        });
        wvWebview.setFocusable(true);
        wvWebview.setFocusableInTouchMode(true);
        wvWebview.loadUrl("https://www.hao123.com/?1505988966");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_webview_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_shuaxin:
                wvWebview.reload();
                break;
            case R.id.action_zhuru:
                String js = "alert('Alert from Java');";
                wvWebview.loadUrl("JavaScript:" + js);
//                wvWebview.loadData("<script>alert('ahaha');</script>", "text/html", "UTF-8");
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
