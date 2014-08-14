package jp.gr.java_conf.duo.domain;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

public class Artist {

    private long id;
    private String artist;
    private String artistKey;
    private int albums;
    private int tracks;

    /* アーティスト取得時に取得する情報 */
    public static final String[] COLUMNS = {
        MediaStore.Audio.Artists._ID,
        MediaStore.Audio.Artists.ARTIST,
        MediaStore.Audio.Artists.ARTIST_KEY,
        MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
        MediaStore.Audio.Artists.NUMBER_OF_TRACKS };

    public Artist(Cursor cursor) {
        id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Artists._ID));
        artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
        artistKey = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST_KEY));
        albums = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));
        tracks = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtistKey() {
        return artistKey;
    }

    public void setArtistKey(String artistKey) {
        this.artistKey = artistKey;
    }

    public int getAlbums() {
        return albums;
    }

    public void setAlbums(int albums) {
        this.albums = albums;
    }

    public int getTracks() {
        return tracks;
    }

    public void setTracks(int tracks) {
        this.tracks = tracks;
    }

    /* 全アーティスト取得 */
    public static List<Artist> getItems(Context activity) {
        ContentResolver resolver = activity.getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                Artist.COLUMNS,
                null,
                null,
                "ARTIST  ASC");

        List<Artist> artists = new ArrayList<Artist>();
        while (cursor.moveToNext()) {
            artists.add(new Artist(cursor));
        }

        cursor.close();
        return artists;
    }

    /* 指定されたアーティスト取得 */
    public static Artist getItemByArtistId(Context activity, long id) {
        ContentResolver resolver = activity.getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                Artist.COLUMNS,
                MediaStore.Audio.Artists._ID + "= ?",
                new String[] { String.valueOf(id) },
                null);

        cursor.moveToNext();
        Artist artist = new Artist(cursor);
        cursor.close();
        return artist;
    }
}
