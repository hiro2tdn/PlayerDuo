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
            ft.replace(R.id.root, new RootMenuFragment(), FLAGMENT_TAGS[0]);
            ft.commit();
        }

        // ALL PLAYボタンの動作設定
        findViewById(R.id.btn_all_play).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                // PLAYアクティビティへ値の受け渡し
                intent.putExtra(MainActivity.EXTRA_POSITION, 0);    // ポジション
                intent.putExtra(EXTRA_ALBUM_ID, albumId);           // アルバムID
                intent.putExtra(EXTRA_ARTIST_ID, artistId);         // アーティストID
                // PLAYアクティビティ起動
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
