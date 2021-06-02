package com.intern.spacex;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AppDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMembers(List<CrewMember> crewMembers);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMember(CrewMember crewMember);

    @Query("DELETE FROM crew_member")
    void deleteAll();

    @Query("SELECT * FROM crew_member")
    LiveData<List<CrewMember>> getAllCrewMember();

}
