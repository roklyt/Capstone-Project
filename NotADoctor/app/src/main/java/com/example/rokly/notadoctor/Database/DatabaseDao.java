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
    LiveData<List<UserEntry>> loadAllUser();

    @Insert
    void insertUser(UserEntry userEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateUser(UserEntry userEntity);

    @Delete
    void deleteUser(UserEntry userEntity);

    @Query("SELECT * FROM user WHERE id = :id")
    LiveData<UserEntry> loadUserById(int id);



    @Insert
    void insertDiagnose(DiagnoseEntry diagnoseEntry);

    @Query("SELECT * FROM diagnose WHERE userId = :userId")
    DiagnoseEntry loadDiagnoseByUserId(int userId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateDiagnose(DiagnoseEntry diagnoseEntry);

    @Delete
    void deleteDiagnose(DiagnoseEntry diagnoseEntry);



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEvidence(EvidenceEntry evidenceEntry);

    @Query("SELECT * FROM evidence WHERE diagnoseId = :diagnoseId")
    LiveData<List<EvidenceEntry>> loadEvidenceByDiagnoseId(int diagnoseId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEvidence(EvidenceEntry evidenceEntry);

    @Delete
    void deleteEvidence(EvidenceEntry evidenceEntry);



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCondition(ConditionEntry conditionEntry);

    @Query("SELECT * FROM condition WHERE diagnoseId = :diagnoseId")
    LiveData<List<ConditionEntry>> loadConditionByDiagnoseId(int diagnoseId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCondition(ConditionEntry ConditionEntry);

    @Delete
    void deleteCondition(ConditionEntry ConditionEntry);

}
