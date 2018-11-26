package com.example.rokly.notadoctor.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DatabaseDao {

    @Query("SELECT * FROM user ORDER BY name")
    LiveData<List<UserEntry>> loadAllFavorites();

    @Insert
    void insertFavorite(UserEntry userEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavorite(UserEntry userEntity);

    @Delete
    void deleteFavorite(UserEntry userEntity);

    @Query("SELECT * FROM user WHERE name = :name")
    LiveData<UserEntry> loadUserByName(String name);
}
