package fr.uha.jacquey.hospitalbed.management.database;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import fr.uha.jacquey.hospitalbed.management.model.Bed;
import fr.uha.jacquey.hospitalbed.management.model.BedPatientAssociation;
import fr.uha.jacquey.hospitalbed.management.model.BedServiceAssociation;
import fr.uha.jacquey.hospitalbed.management.model.FullBed;
import fr.uha.jacquey.hospitalbed.management.model.FullService;
import fr.uha.jacquey.hospitalbed.management.model.Service;

public interface BedDao {


        @Query("SELECT * FROM beds")
        public LiveData<List<Bed>> getAll ();

        @Transaction
        @Query("SELECT * FROM beds")
        public LiveData<List<FullBed>> getAllWithPatients ();

        @Transaction
        @Query("SELECT * FROM beds WHERE bId = :id")
        public LiveData<FullBed> getById (long id);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        public long upsert (Bed bed);

        @Delete
        public void delete (Bed bed);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void addBedPatient(BedPatientAssociation patient);

        @Delete
        void removeBedPatient(BedPatientAssociation patient);

    }
