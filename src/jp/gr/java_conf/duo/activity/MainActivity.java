package jp.gr.java_conf.duo.activity;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.adapter.MainPagerAdapter;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

/* メインアクティビティ */
public class MainActivity extends FragmentActivity implements TabListener {

    public static final String CONST_ARTIST_ID = "ARTIST_ID";
    public static final String CONST_ALBUM_ID = "ALBUM_ID";
    public static final String CONST_POSITION = "POSITION";

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ActionBarをタブモードに切り替える
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // FragmentPagerAdapterを継承したクラスのアダプターを作成する
        PagerAdapter pagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), getResources());

        // ViewPagerにSectionPagerAdapterを設定する
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        // スワイプしたときにもActionBarのタブ（NavigationItem）を常に表示させる処理
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // ActionBarにタブを追加する
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            Tab tab = actionBar.newTab();
            tab.setText(pagerAdapter.getPageTitle(i));
            tab.setTabListener(this);
            actionBar.addTab(tab);
        }
    }

    /* メニューの作成 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /* メニューの処理 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_scan_sdcard:
            // SDカードのマウント
            String _url = "file://" + Environment.getExternalStorageDirectory();
            Uri _uri = Uri.parse(_url);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, _uri));
            return true;
        }

        return onOptionsItemSelected(item);
    }

    /**
     * タブを選択した時の処理
     */
    @Override
    public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
        // ページを切り替える
        viewPager.setCurrentItem(tab.getPosition());
    }

    /**
     * タブの選択が外れた時の処理
     */
    @Override
    public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
    }

    /**
     * 同じタブを再度選択した時の処理
     */
    @Override
    public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
    }
}
