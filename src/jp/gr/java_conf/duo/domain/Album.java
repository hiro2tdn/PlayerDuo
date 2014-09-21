package jp.gr.java_conf.duo.domain;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

public class Album {

    private long id;            // アルバムID
    private String album;       // アルバム名
    private String albumArt;    // アルバムアートのパス
    private String albumKey;    // アルバムキー
    private String artist;      // アルバムのアーティスト名
    private int tracks;         // アルバムのトラック数

    /* アルバム取得時に取得する情報 */
    public static final String[] COLUMNS = {
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
        tracks = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
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

    public int getTracks() {
        return tracks;
    }

    public void setTracks(int tracks) {
        this.tracks = tracks;
    }

    /* 全アルバム取得 */
    public static List<Album> getItems(Context activity) {
        ContentResolver resolver = activity.getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                Album.COLUMNS,
                null,
                null,
                "ALBUM  ASC");

        List<Album> albums = new ArrayList<Album>();
        while (cursor.moveToNext()) {
            albums.add(new Album(cursor));
        }

        cursor.close();
        return albums;
    }

    /* 指定されたアーティストのアルバム取得 */
    public static List<Album> getItemsByArtistId(Context activity, long id) {
        ContentResolver resolver = activity.getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                Album.COLUMNS,
                MediaStore.Audio.Media.ARTIST_ID + "= ?",
                new String[] { String.valueOf(id) },
                "ALBUM  ASC");

        List<Album> albums = new ArrayList<Album>();
        while (cursor.moveToNext()) {
            albums.add(new Album(cursor));
        }

        cursor.close();
        return albums;
    }

    /* 指定されたアルバム取得 */
    public static Album getItemByAlbumId(Context activity, long id) {
        ContentResolver resolver = activity.getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                Album.COLUMNS,
                MediaStore.Audio.Albums._ID + "= ?",
                new String[] { String.valueOf(id) },
                null);

        cursor.moveToNext();
        Album album = new Album(cursor);
        cursor.close();
        return album;
    }
}
