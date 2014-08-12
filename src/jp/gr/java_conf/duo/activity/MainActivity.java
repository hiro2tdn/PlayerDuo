package jp.gr.java_conf.duo.activity;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.domain.Album;
import jp.gr.java_conf.duo.domain.Artist;
import jp.gr.java_conf.duo.domain.Track;
import jp.gr.java_conf.duo.fragment.AlbumTrackFragment;
import jp.gr.java_conf.duo.fragment.ArtistAlbumFragment;
import jp.gr.java_conf.duo.fragment.OneTrackFragment;
import jp.gr.java_conf.duo.fragment.RootMenuFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/* メインアクティビティ */
public class MainActivity extends FragmentActivity {

    enum FrgmType {
        fRoot, fAlbum, fArtist, fTrack
    }

    private Album focusedAlbum;
    private Artist focusedArtist;
    private Track focusedTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.root, new RootMenuFragment(), "Root");
        ft.commit();

        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /* 指定されたタイプのフラグメントを、現在の画面の上に乗せる　 */
    public void setNewFragment(FrgmType frgmType) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        switch (frgmType) {
        case fRoot:
            ft.replace(R.id.root, new RootMenuFragment(), "Root");
            break;
        case fAlbum:
            ft.replace(R.id.root, new AlbumTrackFragment(), "album");
            break;
        case fArtist:
            ft.replace(R.id.root, new ArtistAlbumFragment(), "artist");
            break;
        case fTrack:
            ft.replace(R.id.root, new OneTrackFragment(), "track");
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

    /* タップしたアルバムをセット */
    public void focusAlbum(Album item) {
        if (item != null) {
            focusedAlbum = item;
        }
    }

    /* タップしたアルバムを取得 */
    public Album getFocusedAlbum() {
        return focusedAlbum;
    }

    /* アルバムをタップ */
    public AdapterView.OnItemClickListener AlbumClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView lv = (ListView) parent;
            focusAlbum((Album) lv.getItemAtPosition(position));
            setNewFragment(FrgmType.fAlbum);
        }
    };

    /* アルバムをロングタップ */
    public AdapterView.OnItemLongClickListener AlbumLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            ListView lv = (ListView) parent;
            Album item = (Album) lv.getItemAtPosition(position);
            Toast.makeText(MainActivity.this, item.getAlbum(), Toast.LENGTH_SHORT).show();
            return true;
        }
    };

    /* タップしたアーティストをセット */
    public void focusArtist(Artist item) {
        if (item != null)
            focusedArtist = item;
    }

    /* タップしたアーティストを取得 */
    public Artist getFocusedArtist() {
        return focusedArtist;
    }

    /* アーティストをタップ */
    public AdapterView.OnItemClickListener ArtistClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView lv = (ListView) parent;
            focusArtist((Artist) lv.getItemAtPosition(position));
            setNewFragment(FrgmType.fArtist);
        }
    };

    /* アーティストをロングタップ */
    public AdapterView.OnItemLongClickListener ArtistLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            ListView lv = (ListView) parent;
            Artist item = (Artist) lv.getItemAtPosition(position);
            Toast.makeText(MainActivity.this, item.getArtist(), Toast.LENGTH_SHORT).show();
            return true;
        }
    };

    /* タップしたトラックをセット */
    public void focusTrack(Track item) {
        if (item != null)
            focusedTrack = item;
    }

    /* タップしたトラックを取得 */
    public Track getFocusedTrack() {
        return focusedTrack;
    }

    /* トラックをタップ */
    public AdapterView.OnItemClickListener TrackClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView lv = (ListView) parent;
            focusTrack((Track) lv.getItemAtPosition(position));
            setNewFragment(FrgmType.fTrack);
        }
    };

    /* トラックをロングタップ */
    public AdapterView.OnItemLongClickListener TrackLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            ListView lv = (ListView) parent;
            Track item = (Track) lv.getItemAtPosition(position);
            Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        }
    };
}
