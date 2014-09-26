package jp.gr.java_conf.duo.activity;

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
import android.widget.Button;
import android.widget.TextView;

/* PLAYアクティビティ */
public class PlayActivity extends FragmentActivity {

    private MediaPlayer mp;
    private List<Track> trackList;
    private int position;
    private long artistId;
    private long albumId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);


        // OSによる停止時の状態を復元
        if (savedInstanceState != null) {
            artistId = savedInstanceState.getLong(MainActivity.CONST_ARTIST_ID);
            albumId = savedInstanceState.getLong(MainActivity.CONST_ALBUM_ID);
            position = savedInstanceState.getInt(MainActivity.CONST_POSITION);
        } else {
            // MainActivityから値の受取
            Intent intent = getIntent();
            artistId = intent.getLongExtra(MainActivity.CONST_ARTIST_ID, 0);
            albumId = intent.getLongExtra(MainActivity.CONST_ALBUM_ID, 0);
            position = intent.getIntExtra(MainActivity.CONST_POSITION, 0);
        }

        // トラックリストの取得
        if (albumId != 0) {
            trackList = Track.getItemsByAlbumId(this, albumId);
        } else if (artistId != 0) {
            trackList = Track.getItemsByArtistId(this, artistId);
        } else {
            trackList = Track.getItems(this);
        }

        // PLAYボタンの動作設定
        final Button mBtnPlay = (Button) findViewById(R.id.btn_play);
        mBtnPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 現在の曲を再生する
                playMusic();
            }
        });

        // STOPボタンの動作設定
        findViewById(R.id.btn_stop).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 曲を停止する
                if (mp != null && mp.isPlaying()) {
                    mp.stop();
                }
            }
        });

        // LEFTボタンの動作設定
        findViewById(R.id.btn_left).setOnClickListener(new OnClickListener() {
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
        });

        // RIGHTボタンの動作設定
        final Button mBtmRight = (Button) findViewById(R.id.btn_right);
        mBtmRight.setOnClickListener(new OnClickListener() {
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
        });

        // MediaPlayerの初期化
        mp = new MediaPlayer();

        // MediaPlayerの演奏準備が完了した時の動作設定
        mp.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 0秒目から曲を再生する
                if (mp != null) {
                    mp.seekTo(0);
                    mp.start();
                }
            }
        });

        // MediaPlayerの演奏が終了した時の動作設定
        mp.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mBtmRight.performClick();
                mBtnPlay.performClick();
            }
        });

        // TextViewの設定
        setTextView();

        // 曲を再生する
        playMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mp != null) {
            mp.reset();
            mp.release();
            mp = null;
        }
    }

    /* TextViewの設定 */
    private void setTextView() {
        Track track = trackList.get(position);
        ((TextView) findViewById(R.id.track_name)).setText(track.getTitle());
        ((TextView) findViewById(R.id.artist_name)).setText(track.getArtist());
        ((TextView) findViewById(R.id.album_name)).setText(track.getAlbum());
        ((TextView) findViewById(R.id.duration)).setText(track.getViewDuration());
        ((TextView) findViewById(R.id.track_no)).setText(
                new StringBuffer().append(position + 1).append("/").append(trackList.size()));
        ((TextView) findViewById(R.id.lyric)).setText(track.getLyric());
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

    /* OSによる停止時の処理 */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // 現在の状態を保存
        outState.putLong(MainActivity.CONST_ARTIST_ID, artistId);
        outState.putLong(MainActivity.CONST_ALBUM_ID, albumId);
        outState.putInt(MainActivity.CONST_POSITION, position);
    }
}
