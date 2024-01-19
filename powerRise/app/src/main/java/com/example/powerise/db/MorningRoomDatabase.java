package com.example.powerise.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.powerise.db.morning.Morning;
import com.example.powerise.db.morning.MorningDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Morning.class}, version = 1, exportSchema = false)
public abstract class MorningRoomDatabase extends RoomDatabase {
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                MorningDao dao = INSTANCE.morningDao();
                
                dao.deleteAll();
                /*
                Morning morning = new Morning(2, "test", "test");
                dao.insert(morning);
                morning = new Morning(3, "test2", "test");
                dao.insert(morning);
                */

            });
        }
    };

    public abstract MorningDao morningDao();
    private static volatile MorningRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static MorningRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MorningRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    MorningRoomDatabase.class, "alarmApp_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
