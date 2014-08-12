package jp.gr.java_conf.duo.fragment;

import java.util.List;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.activity.MainActivity;
import jp.gr.java_conf.duo.adapter.ListAlbumAdapter;
import jp.gr.java_conf.duo.domain.Album;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/* 全アルバムフラグメント */
public class AllAlbumsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.all_albums, container, false);
        MainActivity activity = (MainActivity) super.getActivity();

        // アルバムリスト
        List<Album> albums = Album.getItems(activity);
        ListAlbumAdapter adapter = new ListAlbumAdapter(activity, albums);
        ListView albumList = (ListView) view.findViewById(R.id.list);
        albumList.setAdapter(adapter);
        albumList.setOnItemClickListener(activity.AlbumClickListener);
        albumList.setOnItemLongClickListener(activity.AlbumLongClickListener);

        return view;
    }
}
