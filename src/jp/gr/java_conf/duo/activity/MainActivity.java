package jp.gr.java_conf.duo.activity;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.domain.Album;
import jp.gr.java_conf.duo.domain.Artist;
import jp.gr.java_conf.duo.domain.Track;
import jp.gr.java_conf.duo.fragment.ListAlbumFragment;
import jp.gr.java_conf.duo.fragment.RootMenuFragment;
import jp.gr.java_conf.duo.fragment.ListTrackFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/* メインアクティビティ */
public class MainActivity extends FragmentActivity {

    enum FrgmType {
        fRoot, fAlbum, fArtist
    }

    private long albumId;
    private long artistId;

    /* 選択したアルバムを設定 */
    public void setAlbumId(long id) {
        albumId = id;
    }

    /* 選択したアルバムを取得 */
    public long getAlbumId() {
        return albumId;
    }

    /* 選択したアーティストを取得 */
    public long getArtistId() {
        return artistId;
    }

    /* 選択したアーティストを設定 */
    public void setArtistId(long id) {
        artistId = id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fragmentを作成、または、OSによる停止時の値を復元
        if(null == savedInstanceState){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            ft.replace(R.id.root, new RootMenuFragment(), "fRoot");
            ft.commit();
        } else {
            albumId = savedInstanceState.getLong("ALBUM_ID");
            artistId = savedInstanceState.getLong("ARTIST_ID");
        }

        // ALLPLAYボタンの動作設定
        findViewById(R.id.allPlayButton).setOnClickListener(allPlayClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // TODO フラグメントの状態による判定にしたい
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
        outState.putLong("ALBUM_ID", albumId);
        outState.putLong("ARTIST_ID", artistId);
    }

    /* 指定されたフラグメントに置き換える */
    public void setNewFragment(FrgmType frgmType) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        switch (frgmType) {
        case fRoot:
            ft.replace(R.id.root, new RootMenuFragment(), "fRoot");
            break;
        case fAlbum:
            ft.replace(R.id.root, new ListTrackFragment(), "fAlbum");
            break;
        case fArtist:
            ft.replace(R.id.root, new ListAlbumFragment(), "fArtist");
            break;
        }

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    /* フラグメントを1段戻す */
    public void popBackFragment() {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
    }

    /* アーティストクリック時の処理 */
    public OnItemClickListener artistClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView lv = (ListView) parent;
            artistId = ((Artist) lv.getItemAtPosition(position)).getId();
            albumId = 0;
            setNewFragment(FrgmType.fArtist);
        }
    };

    /* アルバムクリック時の処理 */
    public OnItemClickListener albumClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView lv = (ListView) parent;
            albumId = ((Album) lv.getItemAtPosition(position)).getId();
            setNewFragment(FrgmType.fAlbum);
        }
    };

    /* トラッククリック時の処理 */
    public OnItemClickListener trackClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView lv = (ListView) parent;
            long trackId = ((Track) lv.getItemAtPosition(position)).getId();

            Intent intent = new Intent(MainActivity.this, PlayActivity.class);
            // PLAYアクティビティへ値の受け渡し
            intent.putExtra("TRACK_ID", trackId);   // トラックID
            // PLAYアクティビティ起動
            startActivity(intent);
        }
    };

    /* ALLPLAYボタンクリック時の処理 */
    private OnClickListener allPlayClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(MainActivity.this, PlayActivity.class);
            // PLAYアクティビティへ値の受け渡し
            intent.putExtra("ALBUM_ID", albumId);   // アルバムID
            intent.putExtra("ARTIST_ID", artistId); // アーティストID
            // PLAYアクティビティ起動
            startActivity(intent);
        }
    };
}
