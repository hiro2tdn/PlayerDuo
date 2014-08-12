package jp.gr.java_conf.duo.fragment;

import java.util.List;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.activity.MainActivity;
import jp.gr.java_conf.duo.adapter.ListTrackAdapter;
import jp.gr.java_conf.duo.domain.Album;
import jp.gr.java_conf.duo.domain.Track;
import jp.gr.java_conf.duo.image.ImageGetTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/* 選択アルバムのトラックフラグメント */
public class AlbumTrackFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.album_track, container, false);
        MainActivity activity = (MainActivity) super.getActivity();
        Album album = activity.getFocusedAlbum();

        // タイトル
        TextView album_title = (TextView) view.findViewById(R.id.title);
        album_title.setText(album.getAlbum());

        // アーティスト名
        TextView album_artist = (TextView) view.findViewById(R.id.artist);
        album_artist.setText(album.getArtist());

        // トラック数
        TextView album_tracks = (TextView) view.findViewById(R.id.tracks);
        album_tracks.setText(getResources().getQuantityString(R.plurals.tracks, album.getTracks(), album.getTracks()));

        // アルバムアート
        ImageView album_art = (ImageView) view.findViewById(R.id.albumart);
        String path = album.getAlbumArt();
        album_art.setImageResource(R.drawable.dummy_album_art_slim);
        if (path != null) {
            album_art.setTag(path);
            ImageGetTask task = new ImageGetTask(album_art);
            task.execute(path);
        }

        // トラックリスト
        List<Track> tracks = Track.getItemsByAlbum(getActivity(), album.getAlbumId());
        ListTrackAdapter adapter = new ListTrackAdapter(activity, tracks);
        ListView trackList = (ListView) view.findViewById(R.id.list);
        trackList.setAdapter(adapter);
        trackList.setOnItemClickListener(activity.trackClickListener);

        return view;
    }
}
