package jp.gr.java_conf.duo.artist;

import java.util.List;

import jp.gr.java_conf.duo.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/* アーティスト配列アダプタ */
public class ArtistArrayAdapter extends ArrayAdapter<Artist> {

    public ArtistArrayAdapter(Context context, List<Artist> artistList) {
        super(context, 0, artistList);
    }

    private static class ViewHolder {
        TextView artistNameTextView;
        TextView albumNumTextView;

        public ViewHolder(View view) {
            artistNameTextView = (TextView) view.findViewById(R.id.artist_name);
            albumNumTextView = (TextView) view.findViewById(R.id.album_num);
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
        holder.artistNameTextView.setText(artist.getArtist());
        holder.albumNumTextView.setText(getContext().getResources()
                .getQuantityString(R.plurals.album_num, artist.getAlbumNum(), artist.getAlbumNum()));

        return convertView;
    }
}
