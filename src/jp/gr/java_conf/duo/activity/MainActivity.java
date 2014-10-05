package jp.gr.java_conf.duo.activity;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.adapter.MainPagerAdapter;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/* メインアクティビティ */
public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    public static final String CONST_ARTIST_ID = "ARTIST_ID";
    public static final String CONST_ALBUM_ID = "ALBUM_ID";
    public static final String CONST_POSITION = "POSITION";

    public MainPagerAdapter pagerAdapter;
    public ViewPager viewPager;
    public long artistId;
    public long albumId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // OSによる停止時の状態を復元
        if (savedInstanceState != null) {
            artistId = savedInstanceState.getLong(CONST_ARTIST_ID);
            albumId = savedInstanceState.getLong(CONST_ALBUM_ID);
        }

        // ActionBarのモードをタブモードに切り替える
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // FragmentPagerAdapterを継承したクラスのアダプターを作成する
        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), getResources());

        // ViewPagerにSectionPagerAdapterをセットする
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
            ActionBar.Tab tab = actionBar.newTab();
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
        case R.id.menu_readme:
            Toast.makeText(this, "出来る事\nContentProviderの音楽再生\nm4aの歌詞表示", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * タブを選択した時の処理
     */
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // ページを切り替える
        viewPager.setCurrentItem(tab.getPosition());
    }

    /**
     * タブの選択が外れた時の処理
     */
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * 同じタブを再度選択した時の処理
     */
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /* OSによる停止時の処理 */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // 現在の状態を保存
        outState.putLong(CONST_ARTIST_ID, artistId);
        outState.putLong(CONST_ALBUM_ID, albumId);
    }
}
