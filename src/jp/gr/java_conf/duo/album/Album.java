package jp.gr.java_conf.duo.album;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

public class Album {

    private long id; // アルバムID
    private String album; // アルバム名
    private String albumArt; // アルバムアートのパス
    private String albumKey; // アルバムキー
    private String artist; // アルバムのアーティスト名
    private int trackNum; // アルバムのトラック数

    private static String[] COLUMNS = {
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ALBUM_ART,
            MediaStore.Audio.Albums.ALBUM_KEY,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS };

    public Album(Cursor cursor) {
        id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
        album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
        albumArt = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
        albumKey = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_KEY));
        artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
        trackNum = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
    }

    /* アルバム取得 */
    private static List<Album> getMyItems(Context context, String selection, long id) {
        String mSelection = null;
        String[] selectionArgs = null;

        if (id != 0) {
            mSelection = selection;
            selectionArgs = new String[] { String.valueOf(id) };
        }

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                COLUMNS,
                mSelection,
                selectionArgs,
                "ALBUM ASC");

        List<Album> albums = new ArrayList<Album>();
        while (cursor.moveToNext()) {
            albums.add(new Album(cursor));
        }

        cursor.close();
        return albums;
    }

    /* 全アルバム取得 */
    public static List<Album> getItems(Context context) {
        return getMyItems(context, null, 0);
    }

    /* 指定されたアーティストのアルバム取得 */
    public static List<Album> getItemsByArtistId(Context context, long artistId) {
        String selection = MediaStore.Audio.Media.ARTIST_ID + "= ?";
        return getMyItems(context, selection, artistId);
    }

    /* 指定されたアルバム取得 */
    public static Album getItemByAlbumId(Context context, long albumId) {
        String selection = MediaStore.Audio.Albums._ID + "= ?";
        return getMyItems(context, selection, albumId).get(0);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public String getAlbumKey() {
        return albumKey;
    }

    public void setAlbumKey(String albumKey) {
        this.albumKey = albumKey;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getTrackNum() {
        return trackNum;
    }

    public void setTrackNum(int trackNum) {
        this.trackNum = trackNum;
    }
}
