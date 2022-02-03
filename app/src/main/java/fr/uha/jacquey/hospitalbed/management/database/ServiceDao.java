package fr.uha.jacquey.hospitalbed.management.database;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import fr.uha.jacquey.hospitalbed.management.model.BedServiceAssociation;
import fr.uha.jacquey.hospitalbed.management.model.FullService;
import fr.uha.jacquey.hospitalbed.management.model.Service;

public interface ServiceDao {


    @Query("SELECT * FROM services")
    public LiveData<List<Service>> getAll ();

    @Transaction
    @Query("SELECT * FROM services")
    public LiveData<List<FullService>> getAllWithBeds ();

    @Transaction
    @Query("SELECT * FROM services WHERE sId = :id")
    public LiveData<FullService> getById (long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long upsert (Service service);

    @Delete
    public void delete (Service service);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addServiceBed(BedServiceAssociation bed);

    @Delete
    void removeServiceBed(BedServiceAssociation bed);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addServiceBed(List<BedServiceAssociation> beds);

    @Delete
    void removeServiceBed(List<BedServiceAssociation> beds);

}
