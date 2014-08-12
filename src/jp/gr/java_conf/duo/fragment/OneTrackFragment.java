package jp.gr.java_conf.duo.fragment;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.activity.MainActivity;
import jp.gr.java_conf.duo.domain.Track;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/* １トラックフラグメント */
public class OneTrackFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.one_track, container, false);
        MainActivity activity = (MainActivity) super.getActivity();
        Track track = activity.getFocusedTrack();

        // タイトル
        TextView track_title = (TextView) view.findViewById(R.id.title);
        track_title.setText(track.getTitle());

        // アーティスト名
        TextView track_artist = (TextView) view.findViewById(R.id.artist);
        track_artist.setText(track.getArtist());

        // 再生時間
        TextView track_duration = (TextView) view.findViewById(R.id.duration);
        track_duration.setText(track.getStrDuration());

        // 歌詞
        TextView track_lyrics = (TextView) view.findViewById(R.id.lyrics);
        track_lyrics.setText(track.getLyrics());

        return view;
    }
}
