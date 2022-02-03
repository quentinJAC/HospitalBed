package fr.uha.jacquey.hospitalbed.management.database;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import fr.uha.jacquey.hospitalbed.management.model.FullPatient;
import fr.uha.jacquey.hospitalbed.management.model.Patient;

public interface PatientDao {

    @Transaction
    @Query("SELECT * FROM patients WHERE pid = :id")
    LiveData<FullPatient> getFullById (long id);

    @Query("SELECT * FROM patients WHERE pid = :id")
    LiveData<Patient> getPatientById (long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long upsert (Patient patient);

    @Delete
    void delete (Patient patient);

}
