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

        long artistId = intent.getLongExtra("artist", -1);
        long albumId = intent.getLongExtra("album", -1);
        long trackid = intent.getLongExtra("track", -1);

        if (artistId != -1) {
            tracks = Track.getItemsByArtistId(this, artistId);
        } else if (albumId != -1) {
            tracks = Track.getItemsByAlbumId(this, albumId);
        } else if (trackid != -1) {
            tracks = new ArrayList<Track>();
            tracks.add(Track.getItemsByTrackId(this, trackid));
        } else {
            tracks = Track.getItems(this);
        }

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

        // PLAYボタンの動作設定
        findViewById(R.id.playButton).setOnClickListener(new OnClickListener() {
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
                    mp.setDataSource(getApplicationContext(), tracks.get(position).getUri());
                    mp.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // STOPボタンの動作設定
        findViewById(R.id.stopButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp != null) {
                    mp.pause();
                }
            }
        });
    }

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
