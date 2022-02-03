package fr.uha.jacquey.hospitalbed.management.ui.patient;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.uha.jacquey.hospitalbed.livedata.Transformations;


public class PatientListViewModel extends ViewModel {

    private PatientDao PatientDao;
    private MutableLiveData<String> filter;
    private MediatorLiveData<List<Patient>> Patients;

    public void setPatientDao(PatientDao PatientDao) {
        this.PatientDao = PatientDao;
        filter = new MutableLiveData<> ("");
        this.Patients = Transformations.switchMap(filter, f -> f.isEmpty() ? PatientDao.getAll() : PatientDao.getAll());
    }

    public LiveData<List<Patient>> getPatients() {
        return Patients;
    }

    public void deletePatient(Patient Patient) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                PatientDao.delete(Patient);
            }
        });
    }

    public void setFilter(String query) {
        this.filter.postValue(query);
    }

}