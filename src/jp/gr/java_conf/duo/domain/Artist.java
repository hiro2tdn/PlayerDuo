package jp.gr.java_conf.duo.domain;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

public class Artist {

    private long id; // アーティストID
    private String artist; // アーティスト名
    private String artistKey; // アーティストキー
    private int albumNum; // アーティストのアルバム数
    private int trackNum; // アーティストのトラック数

    private static String[] COLUMNS = {
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.ARTIST_KEY,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS };

    public Artist(Cursor cursor) {
        id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Artists._ID));
        artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
        artistKey = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST_KEY));
        albumNum = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));
        trackNum = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
    }

    /* アーティスト取得 */
    private static List<Artist> getMyItems(Context context, String selection, long id) {
        String mSelection = null;
        String[] selectionArgs = null;

        if (id != 0) {
            mSelection = selection;
            selectionArgs = new String[] { String.valueOf(id) };
        }

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                COLUMNS,
                mSelection,
                selectionArgs,
                "ARTIST ASC");

        List<Artist> artists = new ArrayList<Artist>();
        while (cursor.moveToNext()) {
            artists.add(new Artist(cursor));
        }

        cursor.close();
        return artists;
    }

    /* 全アーティスト取得 */
    public static List<Artist> getItems(Context context) {
        return getMyItems(context, null, 0);
    }

    /* 指定されたアーティスト取得 */
    public static Artist getItemByArtistId(Context context, long id) {
        String selection = MediaStore.Audio.Artists._ID + "= ?";
        return getMyItems(context, selection, id).get(0);
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

    public int getAlbumNum() {
        return albumNum;
    }

    public void setAlbumNum(int albumNum) {
        this.albumNum = albumNum;
    }

    public int getTrackNum() {
        return trackNum;
    }

    public void setTrackNum(int trackNum) {
        this.trackNum = trackNum;
    }
}
