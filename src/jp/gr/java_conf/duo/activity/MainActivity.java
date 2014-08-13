package jp.gr.java_conf.duo.activity;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.domain.Album;
import jp.gr.java_conf.duo.domain.Artist;
import jp.gr.java_conf.duo.domain.Track;
import jp.gr.java_conf.duo.fragment.AlbumTrackFragment;
import jp.gr.java_conf.duo.fragment.ArtistAlbumFragment;
import jp.gr.java_conf.duo.fragment.RootMenuFragment;
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
        fRoot, fArtist, fAlbum
    }

    private Artist focusedArtist;
    private Album focusedAlbum;
    private Track focusedTrack;

    /* タップしたアルバムをセット */
    public void setFocusedAlbum(Album album) {
        if (album != null) {
            focusedAlbum = album;
        }
    }

    /* タップしたアーティストをセット */
    public void setFocusedArtist(Artist artist) {
        if (artist != null)
            focusedArtist = artist;
    }

    /* タップしたアーティストを取得 */
    public Artist getFocusedArtist() {
        return focusedArtist;
    }

    /* タップしたアルバムを取得 */
    public Album getFocusedAlbum() {
        return focusedAlbum;
    }

    /* タップしたトラックをセット */
    public void setFocusedTrack(Track track) {
        if (track != null)
            focusedTrack = track;
    }

    /* タップしたトラックを取得 */
    public Track getFocusedTrack() {
        return focusedTrack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.root, new RootMenuFragment(), "fRoot");
        ft.commit();

        // ALLPLAYボタンの動作設定
        findViewById(R.id.allPlayButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // インテントのインスタンス生成
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);

                if (focusedAlbum != null) {
                    // アルバムIDの受け渡し
                    intent.putExtra("album", focusedAlbum.getId());
                } else if (focusedArtist != null) {
                    // アーティストIDの受け渡し
                    intent.putExtra("artist", focusedArtist.getId());
                }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (focusedTrack != null) {
            focusedTrack = null;
        } else if (focusedAlbum != null) {
            focusedAlbum = null;
        } else if (focusedArtist != null) {
            focusedArtist = null;
        }
    }

    /* 指定されたフラグメントに置き換える */
    public void setNewFragment(FrgmType frgmType) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        switch (frgmType) {
        case fRoot:
            ft.replace(R.id.root, new RootMenuFragment(), "fRoot");
            break;
        case fArtist:
            ft.replace(R.id.root, new ArtistAlbumFragment(), "fArtist");
            break;
        case fAlbum:
            ft.replace(R.id.root, new AlbumTrackFragment(), "fAlbum");
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
            focusedArtist = (Artist) lv.getItemAtPosition(position);
            focusedAlbum = null;
            focusedTrack = null;
            setNewFragment(FrgmType.fArtist);
        }
    };

    /* アルバムクリック時の処理 */
    public OnItemClickListener albumClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView lv = (ListView) parent;
            focusedAlbum = (Album) lv.getItemAtPosition(position);
            focusedTrack = null;
            setNewFragment(FrgmType.fAlbum);
        }
    };

    /* トラッククリック時の処理 */
    public OnItemClickListener trackClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView lv = (ListView) parent;
            focusedTrack = (Track) lv.getItemAtPosition(position);

            // インテントのインスタンス生成
            Intent intent = new Intent(MainActivity.this, PlayActivity.class);
            // トラックIDの受け渡し
            intent.putExtra("track", focusedTrack.getId());
            // PLAYアクティビティ起動
            startActivity(intent);
        }
    };
}
