package jp.gr.java_conf.duo.activity;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.domain.Track;
import jp.gr.java_conf.duo.fragment.ListTrackFragment;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/* PLAYアクティビティ */
public class PlayActivity extends FragmentActivity {

    private static final String SLASH = "/";
    private static final String BUNDLE_POSITION = "POSITION";
    // private static final String BUNDLE_FLG_PLAY = "FLG_PLAY";

    private MediaPlayer mp = null;
    private List<Track> trackList = null;
    private int position = 0;
    private boolean flgPlay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // MainActivityから値の受取
        Intent intent = getIntent();
        long trackId = intent.getLongExtra(ListTrackFragment.EXTRA_TRACK_ID, 0);
        long albumId = intent.getLongExtra(MainActivity.EXTRA_ALBUM_ID, 0);
        long artistId = intent.getLongExtra(MainActivity.EXTRA_ARTIST_ID, 0);

        // トラックリストの取得
        if (trackId != 0) {
            trackList = new ArrayList<Track>();
            trackList.add(Track.getItemByTrackId(this, trackId));
        } else if (albumId != 0) {
            trackList = Track.getItemsByAlbumId(this, albumId);
        } else if (artistId != 0) {
            trackList = Track.getItemsByArtistId(this, artistId);
        } else {
            trackList = Track.getItems(this);
        }

        // TextViewの設定
        setTextView();

        // ボタンの動作設定
        findViewById(R.id.btn_play).setOnClickListener(mPlayClickListener);
        findViewById(R.id.btn_stop).setOnClickListener(mStopClickListener);
        findViewById(R.id.btn_left).setOnClickListener(mLeftClickListener);
        findViewById(R.id.btn_right).setOnClickListener(mRrightClickListener);

        // MediaPlayerの動作設定
        mp = new MediaPlayer();
        mp.setOnCompletionListener(mPlayCompListener);
        mp.setOnPreparedListener(mPlayPreparedListener);

        // 初期化、または、OSによる停止時の値を復元
        if (savedInstanceState == null) {
            position = 0;
            // flgPlay = true;
        } else {
            position = savedInstanceState.getInt(BUNDLE_POSITION);
            // flgPlay = savedInstanceState.getBoolean(BUNDLE_FLG_PLAY);
        }

        // 最初の曲を再生する
        playMusic();
    }

    /* 戻るボタン押下時の処理 */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // 曲を停止する
        if (mp != null) {
            mp.release();
        }
    }

    /* OSによる停止時の処理 */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // 現在のインスタンス変数を保存
        outState.putInt(BUNDLE_POSITION, position);
        // outState.putBoolean(BUNDLE_FLG_PLAY, flgPlay);
    }

    /* TextViewの設定 */
    private void setTextView() {
        Track track = trackList.get(position);
        ((TextView) findViewById(R.id.track_name)).setText(track.getTitle());
        ((TextView) findViewById(R.id.artist_name)).setText(track.getArtist());
        ((TextView) findViewById(R.id.album_name)).setText(track.getAlbum());
        ((TextView) findViewById(R.id.duration)).setText(track.getViewDuration());
        ((TextView) findViewById(R.id.track_no)).setText(
                new StringBuffer().append(position + 1).append(SLASH).append(trackList.size()));
        ((TextView) findViewById(R.id.lyric)).setText(track.getLyric());
    }

    /* 曲を再生する */
    private void playMusic() {
        // 曲の設定・準備
        flgPlay = true;
        mp.reset();
        try {
            mp.setDataSource(getApplicationContext(), trackList.get(position).getUri());
            mp.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /* MediaPlayerの演奏準備が完了した時の処理 */
    private OnPreparedListener mPlayPreparedListener = new OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            // 0秒目から曲を再生する
            if (mp != null) {
                mp.seekTo(0);
                mp.start();
            }
        }
    };

    /* MediaPlayerの演奏が終了した時の処理 */
    private OnCompletionListener mPlayCompListener = new OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            // 次の曲を再生する
            if (flgPlay) {
                if (position >= trackList.size() - 1) {
                    position = 0;
                } else {
                    position++;
                }

                // TextViewの設定
                setTextView();

                // 次の曲を再生する
                playMusic();
            }
        }
    };

    /* PLAYボタンクリック時の処理 */
    private OnClickListener mPlayClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // 現在の曲を再生する
            playMusic();
        }
    };

    /* STOPボタンクリック時の処理 */
    private OnClickListener mStopClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            flgPlay = false;
            // 曲を停止する
            if (mp != null && mp.isPlaying()) {
                mp.stop();
            }
        }
    };

    /* LEFTボタンクリック時の処理 */
    private OnClickListener mLeftClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // 前の曲に遷移する
            if (position <= 0) {
                position = trackList.size() - 1;
            } else {
                position--;
            }

            // TextViewの設定
            setTextView();

            // 再生中の場合、前の曲を再生する
            if (mp != null && mp.isPlaying()) {
                playMusic();
            }
        }
    };

    /* RIGHTボタンクリック時の処理 */
    private OnClickListener mRrightClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // 次の曲に遷移する
            if (position >= trackList.size() - 1) {
                position = 0;
            } else {
                position++;
            }

            // TextViewの設定
            setTextView();

            // 再生中の場合、次の曲を再生する
            if (mp != null && mp.isPlaying()) {
                playMusic();
            }
        }
    };
}
