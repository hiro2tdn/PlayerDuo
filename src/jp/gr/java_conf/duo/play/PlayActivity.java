package jp.gr.java_conf.duo.play;

import java.util.List;
import java.util.Locale;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.main.MainActivity;
import jp.gr.java_conf.duo.track.Track;
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

/* プレイアクティビティ */
public class PlayActivity extends FragmentActivity {

    private MediaPlayer mp;
    private boolean isPaused; // 一時停止中フラグ
    private boolean isPlayNext; // 連続再生フラグ
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
            // 呼出元アクティビティから値の受取
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

        // -20sボタンの動作設定
        findViewById(R.id.btn_minus20).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.seekTo(Math.max(0, mp.getCurrentPosition() - 20000)); // 再生時間を20秒戻す
                    setTextView(); // TextViewを設定する
                }
            }
        });

        // -5sボタンの動作設定
        findViewById(R.id.btn_minus5).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.seekTo(Math.max(0, mp.getCurrentPosition() - 5000)); // 再生時間を5秒戻す
                    setTextView(); // TextViewを設定する
                }
            }
        });

        // +5sボタンの動作設定
        findViewById(R.id.btn_plus5).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.seekTo(Math.min(mp.getDuration(), mp.getCurrentPosition() + 5000)); // 再生時間を5秒進める
                    setTextView(); // TextViewを設定する
                }
            }
        });

        // +20sボタンの動作設定
        findViewById(R.id.btn_plus20).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.seekTo(Math.min(mp.getDuration(), mp.getCurrentPosition() + 20000)); // 再生時間を20秒進める
                    setTextView();// TextViewを設定する
                }
            }
        });

        // PLAY・PAUSEボタンの動作設定
        final Button mBtnPlay = (Button) findViewById(R.id.btn_play);
        mBtnPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    // 再生状態を一時停止状態にする
                    mBtnPlay.setText(R.string.btn_play);
                    isPaused = true; // 一時停止中フラグを設定する
                    isPlayNext = false; // 連続再生フラグを解除する
                    mp.pause(); // MPをPaused状態にする
                } else {
                    if (isPaused) {
                        // 一時停止状態を再生状態にする
                        mBtnPlay.setText(R.string.btn_pause);
                        isPaused = false; // 一時停止中フラグを解除する
                        isPlayNext = true; // 連続再生フラグを設定する
                        mp.start(); // MPをPlaybackCompleted状態にする
                    } else {
                        // 停止状態を再生状態にする
                        mBtnPlay.setText(R.string.btn_pause);
                        isPaused = false; // 一時停止中フラグを解除する
                        isPlayNext = true; // 連続再生フラグを設定する
                        playMusic(); // 曲を再生する
                    }
                }
                setTextView(); // TextViewを設定する
            }
        });

        // 左ボタンの動作設定
        findViewById(R.id.btn_left).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 曲の位置を１つ前に設定する
                if (position <= 0) {
                    position = trackList.size() - 1;
                } else {
                    position--;
                }
                moveRightOrLeft(); // 右️ボタン、左ボタンの共通処理
            }
        });

        // 右ボタンの動作設定
        final Button mBtnRight = (Button) findViewById(R.id.btn_right);
        mBtnRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 曲の位置を１つ後に設定する
                if (position >= trackList.size() - 1) {
                    position = 0;
                } else {
                    position++;
                }
                moveRightOrLeft(); // 右ボタン、左ボタンの共通処理
            }
        });

        // MPの初期化
        mp = new MediaPlayer();

        // MPの再生準備が完了した時の動作設定
        mp.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mp != null) {
                    mp.start(); // MPをPlaybackCompleted状態にする
                }
            }
        });

        // MPの再生が終了した時の動作設定
        mp.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (isPlayNext) {
                    mBtnRight.performClick();
                    mBtnPlay.performClick();
                }
            }
        });

        mBtnPlay.performClick();
    }

    /* 新しい曲を再生する */
    private void playMusic() {
        isPaused = false; // 一時停止中フラグを解除する
        isPlayNext = true; // 連続再生フラグを設定する
        mp.reset(); // MPをIdle状態にする
        try {
            mp.setDataSource(getApplicationContext(), trackList.get(position).getUri()); // MPをInitialized状態にする
            mp.prepare(); // MPをPrepared状態にする
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* 右ボタン、左ボタンの共通処理 */
    private void moveRightOrLeft() {
        if (mp.isPlaying()) {
            playMusic(); // 曲を再生する
        } else {
            if (isPaused) {
                isPaused = false; // 一時停止中フラグを解除する
                isPlayNext = false; // 連続再生フラグを解除する
                mp.seekTo(0); // 再生時間を0秒に設定する
                mp.stop(); // MPをStopped状態にする
            }
        }
        setTextView(); // TextViewを設定する
    }

    /* TextViewを設定する */
    private void setTextView() {
        Track track = trackList.get(position);
        ((TextView) findViewById(R.id.track_name)).setText(track.getTitle());
        ((TextView) findViewById(R.id.artist_name)).setText(track.getArtist());
        ((TextView) findViewById(R.id.album_name)).setText(track.getAlbum());
        ((TextView) findViewById(R.id.duration)).setText(makeViewPosition(mp.getCurrentPosition()));
        ((TextView) findViewById(R.id.track_no)).setText(new StringBuffer().append(position + 1).append("/").append(trackList.size()));
        ((TextView) findViewById(R.id.lyric)).setText(track.getLyric());
    }

    private String makeViewPosition(int pos) {
        long dm = pos / 60000;
        long ds = (pos - (dm * 60000)) / 1000;
        return String.format(Locale.getDefault(), "%d:%02d", dm, ds);
    }

    /* Activity停止時の処理 */
    @Override
    protected void onPause() {
        super.onPause();
        if (mp != null) {
            mp.reset(); // MPをIdle状態にする
            mp.release(); // MPをEnd状態にする
            mp = null;
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
