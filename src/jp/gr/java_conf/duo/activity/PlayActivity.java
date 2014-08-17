package jp.gr.java_conf.duo.activity;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.domain.Track;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/* PLAYアクティビティ */
public class PlayActivity extends FragmentActivity {

    private MediaPlayer mp;
    private List<Track> tracks;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // 再生するトラックを取得
        Intent intent = getIntent();

        long trackId = intent.getLongExtra("TRACK_ID", 0);
        long albumId = intent.getLongExtra("ALBUM_ID", 0);
        long artistId = intent.getLongExtra("ARTIST_ID", 0);

        // 前アクティビティからの値の受取含め、いい書き方に直したい
        if (trackId != 0) {
            tracks = new ArrayList<Track>();
            tracks.add(Track.getItemByTrackId(this, trackId));
        } else if (albumId != 0) {
            tracks = Track.getItemsByAlbumId(this, albumId);
        } else if (artistId != 0) {
            tracks = Track.getItemsByArtistId(this, artistId);
        } else {
            tracks = Track.getItems(this);
        }

        // ボタンの動作設定
        findViewById(R.id.playButton).setOnClickListener(playClickListener);
        findViewById(R.id.stopButton).setOnClickListener(stopClickListener);
        findViewById(R.id.leftButton).setOnClickListener(leftClickListener);
        findViewById(R.id.rightButton).setOnClickListener(rightClickListener);

        // MeidaPlayerの動作設定
        mp = new MediaPlayer();
        mp.setOnCompletionListener(playCompListener);
        mp.setOnPreparedListener(playPreparedListener);
        mp.setOnErrorListener(playErrorListener);

        // 再生
        playMusic();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mp != null) {
            mp.reset();
            mp.release();
        }
    }
    private void playMusic() {
        // タイトル
        TextView track_title = (TextView) findViewById(R.id.title);
        track_title.setText(tracks.get(position).getTitle());

        // アーティスト名
        TextView track_artist = (TextView) findViewById(R.id.artist);
        track_artist.setText(tracks.get(position).getArtist());

        // 再生時間
        TextView track_duration = (TextView) findViewById(R.id.duration);
        track_duration.setText(tracks.get(position).getStrDuration());

        // 歌詞
        TextView track_lyrics = (TextView) findViewById(R.id.lyrics);
        track_lyrics.setText(tracks.get(position).getLyrics());

        mp.reset();
        try {
            mp.setDataSource(getApplicationContext(), tracks.get(position).getUri());
            mp.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /* 演奏の準備が完了したら呼ばれる。 */
    private OnPreparedListener playPreparedListener = new OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            if (mp != null) {
                mp.seekTo(0);
                mp.start();
            }
        }
    };

    /* 演奏が終了したら呼ばれる。 */
    private OnCompletionListener playCompListener = new OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            // TODO 常に全曲リピートなのを何とかする
            if (position >= tracks.size() - 1) {
                position = 0;
            } else {
                position++;
            }

            playMusic();
        }
    };

    /* エラーが発生した時に呼ばれる。 */
    private OnErrorListener playErrorListener = new OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
        }
    };

    /* PLAYボタンクリック時の処理 */
    private OnClickListener playClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            playMusic();
        }
    };

    /* STOPボタンクリック時の処理 */
    private OnClickListener stopClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mp != null) {
                mp.stop();
            }
        }
    };

    /* LEFTボタンクリック時の処理 */
    private OnClickListener leftClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (position <= 0) {
                position = tracks.size() - 1;
            } else {
                position--;
            }

            playMusic();
        }
    };

    /* RIGHTボタンクリック時の処理 */
    private OnClickListener rightClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (position >= tracks.size() - 1) {
                position = 0;
            } else {
                position++;
            }

            playMusic();
        }
    };
}
