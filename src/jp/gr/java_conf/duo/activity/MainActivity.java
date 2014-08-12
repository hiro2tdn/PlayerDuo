package jp.gr.java_conf.duo.activity;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.domain.Album;
import jp.gr.java_conf.duo.domain.Artist;
import jp.gr.java_conf.duo.domain.Track;
import jp.gr.java_conf.duo.fragment.AlbumTrackFragment;
import jp.gr.java_conf.duo.fragment.ArtistAlbumFragment;
import jp.gr.java_conf.duo.fragment.OneTrackFragment;
import jp.gr.java_conf.duo.fragment.RootMenuFragment;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

/* メインアクティビティ */
public class MainActivity extends FragmentActivity {

    enum FrgmType {
        fRoot, fAlbum, fArtist, fTrack
    }

    private Album focusedAlbum;
    private Artist focusedArtist;
    private Track focusedTrack;
    private MediaPlayer mp;
    private Button playButton;
    private Button stopButton;

    /* タップしたアルバムをセット */
    public void setFocusedAlbum(Album album) {
        if (album != null) {
            focusedAlbum = album;
        }
    }

    /* タップしたアルバムを取得 */
    public Album getFocusedAlbum() {
        return focusedAlbum;
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

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.root, new RootMenuFragment(), "Root");
        ft.commit();

        setContentView(R.layout.activity_main);

        playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(playClickListener);

        stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(stopClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /* 指定されたタイプのフラグメントを、現在の画面の上に乗せる */
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

    /* アルバムクリック時の処理 */
    public OnItemClickListener albumClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView lv = (ListView) parent;
            setFocusedAlbum((Album) lv.getItemAtPosition(position));
            setNewFragment(FrgmType.fAlbum);
        }
    };

    /* アーティストクリック時の処理 */
    public OnItemClickListener artistClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView lv = (ListView) parent;
            setFocusedArtist((Artist) lv.getItemAtPosition(position));
            setNewFragment(FrgmType.fArtist);
        }
    };

    /* トラッククリック時の処理 */
    public OnItemClickListener trackClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView lv = (ListView) parent;
            setFocusedTrack((Track) lv.getItemAtPosition(position));
            setNewFragment(FrgmType.fTrack);
        }
    };

    /* PLAYクリック時の処理 */
    private OnClickListener playClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mp != null) {
                mp.release();
            }
            mp = new MediaPlayer();
            mp.setOnCompletionListener(playCompListener);
            mp.setOnPreparedListener(playPreparedListener);
            mp.setOnErrorListener(playErrorListener);

            try {
                mp.setDataSource(getApplicationContext(), getFocusedTrack().getUri());
                mp.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /* STOPクリック時の処理 */
    private OnClickListener stopClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mp != null) {
                mp.pause();
            }
        }
    };

    /* 演奏の準備が完了したら呼ばれる。 */
    private OnPreparedListener playPreparedListener = new OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer ThisMP) {
            if (mp == ThisMP) {
                mp.start();
            }
        }
    };

    /* 演奏が終了したら呼ばれる。 */
    private OnCompletionListener playCompListener = new OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer ThisMP) {
            if (mp == ThisMP) {
                mp.release();
                ;
            }
        }
    };

    /* エラーが発生した時に呼ばれる。 */
    private OnErrorListener playErrorListener = new OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer ThisMP, int what, int extra) {
            return false;
        }
    };
}
