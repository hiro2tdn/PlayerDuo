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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/* トラック配列フラグメント */
public class ArrayTrackFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.list_track, container, false);
        final MainActivity activity = (MainActivity) super.getActivity();

        // トラックリストを設定する
        List<Track> trackList = Track.getItems(activity);
        ArrayTrackAdapter adapter = new ArrayTrackAdapter(activity, trackList);
        ListView trackListView = (ListView) view.findViewById(R.id.list_track);
        trackListView.setAdapter(adapter);

        // リストクリック時の動作設定
        trackListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // クリックした情報を取得する
                ListView lv = (ListView) parent;
                Track track = (Track) lv.getItemAtPosition(position);

                // PLAYアクティビティへ値の受け渡し・起動
                Intent intent = new Intent(activity, PlayActivity.class);
                intent.putExtra(MainActivity.CONST_ARTIST_ID, track.getArtistId()); // アーティストID
                intent.putExtra(MainActivity.CONST_ALBUM_ID, track.getAlbumId()); // アルバムID

                // トラックリストを再取得して再取得後のポジションを設定する
                List<Track> trackList = Track.getItemsByAlbumId(activity, track.getAlbumId());
                int positionNew = 0;
                for (Track trackNew : trackList) {
                    if (track.getId() == trackNew.getId()) {
                        intent.putExtra(MainActivity.CONST_POSITION, positionNew); // ポジション
                        break;
                    }
                    positionNew++;
                }

                startActivity(intent);
            }
        });

        return view;
    }
}
