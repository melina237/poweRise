package com.example.powerise.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {com.example.powerise.db.Morning.class}, version = 1, exportSchema = false)
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
                com.example.powerise.db.MorningDao dao = INSTANCE.morningDao();
                dao.deleteAll();

                Morning morning = new com.example.powerise.db.Morning(2);
                dao.insert(morning);
                morning = new com.example.powerise.db.Morning(3);
                dao.insert(morning);
            });
        }
    };

    public abstract MorningDao morningDao();
    private static volatile MorningRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static MorningRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MorningRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    MorningRoomDatabase.class, "alarmApp_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
