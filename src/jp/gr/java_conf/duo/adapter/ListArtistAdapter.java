package jp.gr.java_conf.duo.adapter;

import java.util.List;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.domain.Artist;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListArtistAdapter extends ArrayAdapter<Artist> {

    public ListArtistAdapter(Context context, List<Artist> artists) {
        super(context, 0, artists);
    }

    private static class ViewHolder {
        TextView artistTextView;
        TextView albumsTextView;
        TextView tracksTextView;

        public ViewHolder(View view) {
            artistTextView = (TextView) view.findViewById(R.id.artist);
            albumsTextView = (TextView) view.findViewById(R.id.albums);
            tracksTextView = (TextView) view.findViewById(R.id.tracks);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_artist, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Artist artist = getItem(position);
        holder.artistTextView.setText(artist.getArtist());
        holder.albumsTextView.setText(getContext().getResources().getQuantityString(R.plurals.albums, artist.getAlbums(), artist.getAlbums()));
        holder.tracksTextView.setText(getContext().getResources().getQuantityString(R.plurals.tracks, artist.getTracks(), artist.getTracks()));

        return convertView;
    }
}
