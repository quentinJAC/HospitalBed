package fr.uha.jacquey.hospitalbed.management.ui.bed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.uha.jacquey.hospitalbed.database.DeltaUtil;
import fr.uha.jacquey.hospitalbed.livedata.AndLiveData;
import fr.uha.jacquey.hospitalbed.livedata.Transformations;
import fr.uha.jacquey.hospitalbed.management.database.BedDao;
import fr.uha.jacquey.hospitalbed.management.database.PatientDao;
import fr.uha.jacquey.hospitalbed.management.database.ServiceDao;
import fr.uha.jacquey.hospitalbed.management.model.Bed;
import fr.uha.jacquey.hospitalbed.management.model.BedPatientAssociation;
import fr.uha.jacquey.hospitalbed.management.model.Patient;
import fr.uha.jacquey.hospitalbed.management.model.Service;


public class BedViewModel extends ViewModel {

    static private final String TAG = BedViewModel.class.getSimpleName();

    private PatientDao patientDao;
    private ServiceDao serviceDao;

    private BedDao bedDao;
    private MutableLiveData<Long> id = new MutableLiveData<>();
    private LiveData<Bed> bed;
    private MutableLiveData<Long> numero;
    private MediatorLiveData<Service> service;
    private MediatorLiveData<Patient> patient;

    private BedComparator comparator;
    private BedValidator validator;
    private AndLiveData savable;

    public void setPatientDao(PatientDao patientDao) {
        this.patientDao = patientDao;
    }



    public void setBedDao(BedDao bedDao) {
        this.bedDao = bedDao;
        this.bed = Transformations.switchMap(id, p -> bedDao.getById(p));
        this.numero = Transformations.map(bed, t -> t.getNumero());
        this.patient = Transformations.map(bed, t -> t.getPatient());

        comparator = new BedComparator(this);
        validator = new BedValidator(this);
        savable = new AndLiveData(comparator.getModified(), validator.getValidated());
    }

    LiveData<Bed> getBed() {
        return bed;
    }

    public MutableLiveData<Long> getNumero() {
        return numero;
    }

    public MutableLiveData<Patient> getPatient() {
        return patient;
    }

    public LiveData<Boolean> getModified() {
        return comparator.getModified();
    }

    public LiveData<Long> getNumeroValidator() {
        return validator.getNumeroValidator();
    }


    public LiveData<Long> getPatientValidator() {
        return validator.getPatientValidator();
    }

    public LiveData<Boolean> getValidated() {
        return validator.getValidated();
    }

    public LiveData<Boolean> getSavable() {
        return savable.getSavable();
    }

    public void setPatient(long PatientId) {
        LiveData<Patient> pp = PatientDao.getById(PatientId);
        pp.observeForever(
                new Observer<Patient>() {
                    @Override
                    public void onChanged(Patient newPatient) {
                        pp.removeObserver(this);
                        Patient.postValue(newPatient);
                    }
                }
        );
    }


    public void setNumero(long numero) {
        getNumero().postValue(numero);
    }

    public void save() {
        Bed bed= new Bed(
                numero.getValue(),
                patient.getValue()
        );
        long bedId = this.id.getValue();
        DeltaUtil<Patient, BedPatientAssociation> delta = new DeltaUtil<Patient, BedPatientAssociation>() {
            @Override
            protected long getId(Patient patient) {
                return patient.getPid();
            }

            @Override
            protected boolean same(Patient initial, Patient now) {
                return true;
            }

            @Override
            protected BedPatientAssociation createFor(Patient patient) {
                return new BedPatientAssociation(bedId, patient.getPid());
            }

        };
        Patient oldPatient = this.bed.getValue().getPatient();
        Patient newPatient = this.patient.getValue();
        Patient.compare(oldPatient, newPatient);
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                bedDao.upsert(bed);
                bedDao.removeBedPatient((BedPatientAssociation) delta.getToRemove());
                bedDao.addBedPatient((BedPatientAssociation) delta.getToAdd());
            }
        });
    }

    public void setId(long id) {
        this.id.postValue(id);
    }

    public void createBed() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Bed bed = new Bed ();
                long id = bedDao.upsert(bed);
                setId(id);
            }
        });

    }

}