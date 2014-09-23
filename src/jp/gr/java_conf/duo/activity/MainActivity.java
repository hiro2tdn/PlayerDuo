package jp.gr.java_conf.duo.activity;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.fragment.RootFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/* メインアクティビティ */
public class MainActivity extends FragmentActivity {

    public static final String[] FLAGMENT_TAGS = {
        "fRoot",
        "fArtist",
        "fAlbum"
    };

    public static final String EXTRA_POSITION = "POSITION";
    public static final String EXTRA_ALBUM_ID = "ALBUM_ID";
    public static final String EXTRA_ARTIST_ID = "ARTIST_ID";

    private static final String BUNDLE_ALBUM_ID = "ALBUM_ID";
    private static final String BUNDLE_ARTIST_ID = "ARTIST_ID";

    private long albumId = 0;
    private long artistId = 0;

    /* 選択したアルバムを設定 */
    public void setAlbumId(long id) {
        albumId = id;
    }

    /* 選択したアルバムを取得 */
    public long getAlbumId() {
        return albumId;
    }

    /* 選択したアーティストを設定 */
    public void setArtistId(long id) {
        artistId = id;
    }

    /* 選択したアーティストを取得 */
    public long getArtistId() {
        return artistId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // OSによる停止前の状態があるか
        if (savedInstanceState != null) {
            // OSによる停止時の状態を復元
            albumId = savedInstanceState.getLong(BUNDLE_ALBUM_ID);
            artistId = savedInstanceState.getLong(BUNDLE_ARTIST_ID);
        } else {
            // Fragmentを作成
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.root, new RootFragment(), FLAGMENT_TAGS[0]);
            ft.commit();
        }
    }

    /* メニューの作成 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /* メニューが選択された時の処理 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // アイテムIDで識別
        switch (item.getItemId()) {
        case R.id.menu_all_play:
            Intent intent = new Intent(MainActivity.this, PlayActivity.class);
            // PLAYアクティビティへ値の受け渡し
            intent.putExtra(MainActivity.EXTRA_POSITION, 0);    // ポジション
            intent.putExtra(EXTRA_ALBUM_ID, albumId);           // アルバムID
            intent.putExtra(EXTRA_ARTIST_ID, artistId);         // アーティストID
            // PLAYアクティビティ起動
            startActivity(intent);
            return true;
        case R.id.menu_scan_sdcard:
            String _url = "file://" + Environment.getExternalStorageDirectory();
            Uri _uri = Uri.parse(_url);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, _uri));
            return true;
        case R.id.menu_memo:
            Toast.makeText(this, "出来る事\nContentProviderの音楽再生\nm4aの歌詞表示", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    /* OSによる停止時の処理 */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // 現在の状態を保存
        outState.putLong(BUNDLE_ALBUM_ID, albumId);
        outState.putLong(BUNDLE_ARTIST_ID, artistId);
    }
}
