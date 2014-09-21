package jp.gr.java_conf.duo.adapter;

import java.util.List;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.domain.Track;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListTrackAdapter extends ArrayAdapter<Track> {

    public ListTrackAdapter(Context context, List<Track> tracks) {
        super(context, 0, tracks);
    }

    private static class ViewHolder {
        TextView titleTextView;
        TextView artistTextView;
        TextView albumTextView;
        TextView durationTextView;

        public ViewHolder(View view) {
            titleTextView = (TextView) view.findViewById(R.id.track_name);
            artistTextView = (TextView) view.findViewById(R.id.artist_name);
            albumTextView = (TextView) view.findViewById(R.id.album_name);
            durationTextView = (TextView) view.findViewById(R.id.duration);
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
        holder.titleTextView.setText(track.getTitle());
        holder.artistTextView.setText(track.getArtist());
        holder.albumTextView.setText(track.getAlbum());
        holder.durationTextView.setText(track.getViewDuration());

        return convertView;
    }
}
