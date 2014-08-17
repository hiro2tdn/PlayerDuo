package jp.gr.java_conf.duo.activity;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.fragment.RootMenuFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

/* メインアクティビティ */
public class MainActivity extends FragmentActivity {

    public static final String EXTRA_ALBUM_ID = "ALBUM_ID";
    public static final String EXTRA_ARTIST_ID = "ARTIST_ID";

    private static final String BUNDLE_ALBUM_ID = "ALBUM_ID";
    private static final String BUNDLE_ARTIST_ID = "ARTIST_ID";

    private static final String F_ROOT = "fRoot";

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

        // Fragmentを作成、または、OSによる停止時の値を復元
        if(null == savedInstanceState){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            ft.replace(R.id.root, new RootMenuFragment(), F_ROOT);
            ft.commit();
        } else {
            albumId = savedInstanceState.getLong(BUNDLE_ALBUM_ID);
            artistId = savedInstanceState.getLong(BUNDLE_ARTIST_ID);
        }

        // ボタンの動作設定
        findViewById(R.id.btn_all_play).setOnClickListener(mAllPlayClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (albumId != 0) {
            albumId = 0;
        } else if (artistId != 0) {
            artistId = 0;
        }
    }

    /* OSによる停止時の処理 */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // 現在のインスタンス変数を保存
        outState.putLong(BUNDLE_ALBUM_ID, albumId);
        outState.putLong(BUNDLE_ARTIST_ID, artistId);
    }

    /* ALL PLAYボタンクリック時の処理 */
    private OnClickListener mAllPlayClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, PlayActivity.class);
            // PLAYアクティビティへ値の受け渡し
            intent.putExtra(EXTRA_ALBUM_ID, albumId);   // アルバムID
            intent.putExtra(EXTRA_ARTIST_ID, artistId); // アーティストID
            // PLAYアクティビティ起動
            startActivity(intent);
        }
    };
}
