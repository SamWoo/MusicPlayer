package sam.musicplayer.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import sam.musicplayer.Bean.MusicBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MUSIC_BEAN".
*/
public class MusicBeanDao extends AbstractDao<MusicBean, Long> {

    public static final String TABLENAME = "MUSIC_BEAN";

    /**
     * Properties of entity MusicBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Songname = new Property(1, String.class, "songname", false, "SONGNAME");
        public final static Property Seconds = new Property(2, int.class, "seconds", false, "SECONDS");
        public final static Property Albummid = new Property(3, String.class, "albummid", false, "ALBUMMID");
        public final static Property Songid = new Property(4, int.class, "songid", false, "SONGID");
        public final static Property Singerid = new Property(5, int.class, "singerid", false, "SINGERID");
        public final static Property Albumpic_big = new Property(6, String.class, "albumpic_big", false, "ALBUMPIC_BIG");
        public final static Property Albumpic_small = new Property(7, String.class, "albumpic_small", false, "ALBUMPIC_SMALL");
        public final static Property DownUrl = new Property(8, String.class, "downUrl", false, "DOWN_URL");
        public final static Property Url = new Property(9, String.class, "url", false, "URL");
        public final static Property Singername = new Property(10, String.class, "singername", false, "SINGERNAME");
        public final static Property Albumid = new Property(11, int.class, "albumid", false, "ALBUMID");
        public final static Property Type = new Property(12, int.class, "type", false, "TYPE");
        public final static Property IsCollected = new Property(13, boolean.class, "isCollected", false, "IS_COLLECTED");
    }


    public MusicBeanDao(DaoConfig config) {
        super(config);
    }
    
    public MusicBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MUSIC_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"SONGNAME\" TEXT," + // 1: songname
                "\"SECONDS\" INTEGER NOT NULL ," + // 2: seconds
                "\"ALBUMMID\" TEXT," + // 3: albummid
                "\"SONGID\" INTEGER NOT NULL ," + // 4: songid
                "\"SINGERID\" INTEGER NOT NULL ," + // 5: singerid
                "\"ALBUMPIC_BIG\" TEXT," + // 6: albumpic_big
                "\"ALBUMPIC_SMALL\" TEXT," + // 7: albumpic_small
                "\"DOWN_URL\" TEXT," + // 8: downUrl
                "\"URL\" TEXT," + // 9: url
                "\"SINGERNAME\" TEXT," + // 10: singername
                "\"ALBUMID\" INTEGER NOT NULL ," + // 11: albumid
                "\"TYPE\" INTEGER NOT NULL ," + // 12: type
                "\"IS_COLLECTED\" INTEGER NOT NULL );"); // 13: isCollected
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MUSIC_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MusicBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String songname = entity.getSongname();
        if (songname != null) {
            stmt.bindString(2, songname);
        }
        stmt.bindLong(3, entity.getSeconds());
 
        String albummid = entity.getAlbummid();
        if (albummid != null) {
            stmt.bindString(4, albummid);
        }
        stmt.bindLong(5, entity.getSongid());
        stmt.bindLong(6, entity.getSingerid());
 
        String albumpic_big = entity.getAlbumpic_big();
        if (albumpic_big != null) {
            stmt.bindString(7, albumpic_big);
        }
 
        String albumpic_small = entity.getAlbumpic_small();
        if (albumpic_small != null) {
            stmt.bindString(8, albumpic_small);
        }
 
        String downUrl = entity.getDownUrl();
        if (downUrl != null) {
            stmt.bindString(9, downUrl);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(10, url);
        }
 
        String singername = entity.getSingername();
        if (singername != null) {
            stmt.bindString(11, singername);
        }
        stmt.bindLong(12, entity.getAlbumid());
        stmt.bindLong(13, entity.getType());
        stmt.bindLong(14, entity.getIsCollected() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MusicBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String songname = entity.getSongname();
        if (songname != null) {
            stmt.bindString(2, songname);
        }
        stmt.bindLong(3, entity.getSeconds());
 
        String albummid = entity.getAlbummid();
        if (albummid != null) {
            stmt.bindString(4, albummid);
        }
        stmt.bindLong(5, entity.getSongid());
        stmt.bindLong(6, entity.getSingerid());
 
        String albumpic_big = entity.getAlbumpic_big();
        if (albumpic_big != null) {
            stmt.bindString(7, albumpic_big);
        }
 
        String albumpic_small = entity.getAlbumpic_small();
        if (albumpic_small != null) {
            stmt.bindString(8, albumpic_small);
        }
 
        String downUrl = entity.getDownUrl();
        if (downUrl != null) {
            stmt.bindString(9, downUrl);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(10, url);
        }
 
        String singername = entity.getSingername();
        if (singername != null) {
            stmt.bindString(11, singername);
        }
        stmt.bindLong(12, entity.getAlbumid());
        stmt.bindLong(13, entity.getType());
        stmt.bindLong(14, entity.getIsCollected() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public MusicBean readEntity(Cursor cursor, int offset) {
        MusicBean entity = new MusicBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // songname
            cursor.getInt(offset + 2), // seconds
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // albummid
            cursor.getInt(offset + 4), // songid
            cursor.getInt(offset + 5), // singerid
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // albumpic_big
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // albumpic_small
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // downUrl
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // url
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // singername
            cursor.getInt(offset + 11), // albumid
            cursor.getInt(offset + 12), // type
            cursor.getShort(offset + 13) != 0 // isCollected
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MusicBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSongname(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSeconds(cursor.getInt(offset + 2));
        entity.setAlbummid(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSongid(cursor.getInt(offset + 4));
        entity.setSingerid(cursor.getInt(offset + 5));
        entity.setAlbumpic_big(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setAlbumpic_small(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setDownUrl(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setUrl(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setSingername(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setAlbumid(cursor.getInt(offset + 11));
        entity.setType(cursor.getInt(offset + 12));
        entity.setIsCollected(cursor.getShort(offset + 13) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(MusicBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(MusicBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MusicBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
