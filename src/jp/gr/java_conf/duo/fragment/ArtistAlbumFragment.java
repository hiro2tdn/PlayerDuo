package jp.gr.java_conf.duo.fragment;

import java.util.List;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.activity.MainActivity;
import jp.gr.java_conf.duo.adapter.ListAlbumAdapter;
import jp.gr.java_conf.duo.domain.Album;
import jp.gr.java_conf.duo.domain.Artist;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/* 選択アーティストのアルバムフラグメント */
public class ArtistAlbumFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.artist_album, container, false);
        MainActivity activity = (MainActivity) super.getActivity();
        Artist artist = activity.getFocusedArtist();

        // タイトル
        TextView artist_title = (TextView) view.findViewById(R.id.title);
        artist_title.setText(artist.getArtist());

        // アルバム数
        TextView artist_albums = (TextView) view.findViewById(R.id.albums);
        artist_albums.setText(getResources().getQuantityString(R.plurals.albums, artist.getAlbums(), artist.getAlbums()));

        // トラック数
        TextView artist_tracks = (TextView) view.findViewById(R.id.tracks);
        artist_tracks.setText(getResources().getQuantityString(R.plurals.tracks, artist.getTracks(), artist.getTracks()));

        // アルバムリスト
        List<Album> albums = Album.getItemsByArtistId(getActivity(), artist.getId());
        ListAlbumAdapter adapter = new ListAlbumAdapter(activity, albums);
        ListView albumList = (ListView) view.findViewById(R.id.list);
        albumList.setAdapter(adapter);
        albumList.setOnItemClickListener(activity.albumClickListener);

        return view;
    }
}
