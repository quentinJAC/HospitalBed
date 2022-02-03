package fr.uha.jacquey.hospitalbed.management.database;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import androidx.room.Database;
import androidx.room.TypeConverters;

import fr.uha.jacquey.hospitalbed.database.DatabaseTypeConverters;
import fr.uha.jacquey.hospitalbed.management.model.Bed;
import fr.uha.jacquey.hospitalbed.management.model.BedPatientAssociation;
import fr.uha.jacquey.hospitalbed.management.model.BedServiceAssociation;
import fr.uha.jacquey.hospitalbed.management.model.Patient;
import fr.uha.jacquey.hospitalbed.management.model.Service;


@TypeConverters({DatabaseTypeConverters.class})
@Database(entities = { Bed.class, Patient.class, Service.class, BedServiceAssociation.class,  BedPatientAssociation.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance = null;
    private static MutableLiveData<AppDatabase> ready = new MutableLiveData<>(null);

    static public void create(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, "hospital.db").build();
            ready.postValue(instance);
        }
    }

    public static LiveData<AppDatabase> isReady() {
        return ready;
    }

    static public AppDatabase get () {
        return instance;
    }

    public abstract BedDao getBedDao ();
    public abstract PatientDao getPatientDao();
    public abstract ServiceDao getServiceDao ();

}

