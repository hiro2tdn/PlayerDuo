package jp.gr.java_conf.duo.activity;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.domain.Track;
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

    private MediaPlayer mp;
    private List<Track> trackList;
    private int position;
    private boolean flgPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // 再生するトラックを取得
        Intent intent = getIntent();

        long trackId = intent.getLongExtra("TRACK_ID", 0);
        long albumId = intent.getLongExtra("ALBUM_ID", 0);
        long artistId = intent.getLongExtra("ARTIST_ID", 0);

        // TODO 前アクティビティからの値の受取含め、いい書き方に直したい
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

        // 初期化、または、OSによる停止時の値を復元
        if (savedInstanceState == null) {
            position = 0;
            flgPlay = false;
        } else  {
            position = savedInstanceState.getInt("POSITION");
            flgPlay = savedInstanceState.getBoolean("FLG_PLAY");
        }

        // TextViewの設定
        setTextView();

        // ボタンの動作設定
        findViewById(R.id.playButton).setOnClickListener(playClickListener);
        findViewById(R.id.stopButton).setOnClickListener(stopClickListener);
        findViewById(R.id.leftButton).setOnClickListener(leftClickListener);
        findViewById(R.id.rightButton).setOnClickListener(rightClickListener);

        // MediaPlayerの動作設定
        mp = new MediaPlayer();
        mp.setOnCompletionListener(playCompListener);
        mp.setOnPreparedListener(playPreparedListener);
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
        outState.putInt("POSITION", position);
        outState.putBoolean("FLG_PLAY", flgPlay);
    }

    /* TextViewの設定 */
    private void setTextView() {
        Track track = trackList.get(position);
        ((TextView) findViewById(R.id.title)).setText(track.getTitle());
        ((TextView) findViewById(R.id.artist)).setText(track.getArtist());
        ((TextView) findViewById(R.id.album)).setText(track.getAlbum());
        ((TextView) findViewById(R.id.duration)).setText(track.getStrDuration());
        String strTracks = new StringBuffer().append(position + 1).append("/").append(trackList.size()).toString();
        ((TextView) findViewById(R.id.tracks)).setText(strTracks);
        ((TextView) findViewById(R.id.lyrics)).setText(track.getLyrics());
    }

    /* 曲を再生する */
    private void playMusic() {
        // 曲の設定・準備
        mp.reset();
        try {
            mp.setDataSource(getApplicationContext(), trackList.get(position).getUri());
            mp.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /* MediaPlayerの演奏準備が完了した時の処理 */
    private OnPreparedListener playPreparedListener = new OnPreparedListener() {
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
    private OnCompletionListener playCompListener = new OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer thisMp) {

            // 次の曲を再生する
            if (flgPlay) {
                if (position >= trackList.size() - 1) {
                    position = 0;
                } else {
                    position++;
                }

                // TextViewの設定
                setTextView();

                // 曲を再生する
                playMusic();
            }
        }
    };

    /* PLAYボタンクリック時の処理 */
    private OnClickListener playClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // 曲を再生する
            flgPlay = true;
            playMusic();
        }
    };

    /* STOPボタンクリック時の処理 */
    private OnClickListener stopClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // 曲を停止する
            if (mp != null && mp.isPlaying()) {
                flgPlay = false;
                mp.stop();
            }
        }
    };

    /* LEFTボタンクリック時の処理 */
    private OnClickListener leftClickListener = new OnClickListener() {
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

            // 再生中の場合、曲を再生する
            if (mp != null && mp.isPlaying()) {
                playMusic();
            }
        }
    };

    /* RIGHTボタンクリック時の処理 */
    private OnClickListener rightClickListener = new OnClickListener() {
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

            // 再生中の場合、曲を再生する
            if (mp != null && mp.isPlaying()) {
                playMusic();
            }
        }
    };
}
