package jp.gr.java_conf.duo.adapter;

import java.util.List;

import jp.gr.java_conf.duo.R;
import jp.gr.java_conf.duo.domain.Album;
import jp.gr.java_conf.duo.image.ImageCache;
import jp.gr.java_conf.duo.image.ImageGetTask;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAlbumAdapter extends ArrayAdapter<Album> {

    public ListAlbumAdapter(Context context, List<Album> albums) {
        super(context, 0, albums);
    }

    private static class ViewHolder {
        TextView albumTextView;
        TextView artistTextView;
        TextView tracksTextView;
        ImageView artworkImageView;

        public ViewHolder(View view) {
            albumTextView = (TextView) view.findViewById(R.id.album_name);
            artistTextView = (TextView) view.findViewById(R.id.artist_name);
            tracksTextView = (TextView) view.findViewById(R.id.track_num);
            artworkImageView = (ImageView) view.findViewById(R.id.albumart);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_album, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Album album = getItem(position);
        holder.albumTextView.setText(album.getAlbum());
        holder.artistTextView.setText(album.getArtist());
        holder.tracksTextView.setText(getContext().getResources().getQuantityString(R.plurals.track_num, album.getTracks(), album.getTracks()));

        String path = album.getAlbumArt();
        holder.artworkImageView.setImageResource(R.drawable.dummy_album_art_slim_gray);
        if (path == null) {
            path = String.valueOf(R.drawable.dummy_album_art_slim);
            Bitmap bitmap = ImageCache.getImage(path);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.dummy_album_art_slim);
                ImageCache.setImage(path, bitmap);
            }
        }

        holder.artworkImageView.setTag(path);
        ImageGetTask task = new ImageGetTask(holder.artworkImageView);
        task.execute(path);

        return convertView;
    }
}
