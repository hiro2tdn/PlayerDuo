package jp.gr.java_conf.duo.adapter;

import java.util.List;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.domain.Track;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/* トラック配列アダプタ */
public class ArrayTrackAdapter extends ArrayAdapter<Track> {

    public ArrayTrackAdapter(Context context, List<Track> trackList) {
        super(context, 0, trackList);
    }

    private static class ViewHolder {
        TextView trackNameTextView;
        TextView albumNameTextView;

        public ViewHolder(View view) {
            trackNameTextView = (TextView) view.findViewById(R.id.track_name);
            albumNameTextView = (TextView) view.findViewById(R.id.album_name);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_track, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Track track = getItem(position);
        holder.trackNameTextView.setText(track.getTitle());
        holder.albumNameTextView.setText(track.getAlbum());

        return convertView;
    }
}
