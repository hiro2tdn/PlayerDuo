package jp.gr.java_conf.duo.fragment;

import java.util.List;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.activity.MainActivity;
import jp.gr.java_conf.duo.adapter.ListArtistAdapter;
import jp.gr.java_conf.duo.domain.Artist;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/* アーティストフラグメント */
public class ArtistsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.artists, container, false);
        MainActivity activity = (MainActivity) super.getActivity();

        // アーティストリスト
        List<Artist> artistList = Artist.getItems(activity);
        ListArtistAdapter adapter = new ListArtistAdapter(activity, artistList);
        ListView artistListView = (ListView) view.findViewById(R.id.list);
        artistListView.setAdapter(adapter);
        artistListView.setOnItemClickListener(activity.artistClickListener);

        return view;
    }
}
