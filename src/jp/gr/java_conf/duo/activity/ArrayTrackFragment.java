package jp.gr.java_conf.duo.activity;

import java.util.List;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.adapter.ArrayTrackAdapter;
import jp.gr.java_conf.duo.domain.Track;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/* トラック配列フラグメント */
public class ArrayTrackFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.list_track, container, false);
        final MainActivity activity = (MainActivity) super.getActivity();

        // リストの設定
        List<Track> trackList = null;
        if (activity.albumId != 0) {
            trackList = Track.getItemsByAlbumId(activity, activity.albumId);
        } else {
            trackList = Track.getItemsByArtistId(activity, activity.artistId);
        }
        ArrayTrackAdapter adapter = new ArrayTrackAdapter(activity, trackList);
        ListView trackListView = (ListView) view.findViewById(R.id.list_track);
        trackListView.setAdapter(adapter);

        // リストクリック時の動作設定
        trackListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // PLAYアクティビティへ値の受け渡し・起動
                Intent intent = new Intent(activity, PlayActivity.class);
                intent.putExtra(MainActivity.CONST_ARTIST_ID, activity.artistId); // アーティストID
                intent.putExtra(MainActivity.CONST_ALBUM_ID, activity.albumId); // アルバムID
                intent.putExtra(MainActivity.CONST_POSITION, position); // ポジション
                startActivity(intent);
            }
        });

        return view;
    }
}
