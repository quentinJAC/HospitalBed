package fr.uha.jacquey.hospitalbed.management.ui.patient;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.uha.jacquey.hospitalbed.livedata.AndLiveData;
import fr.uha.jacquey.hospitalbed.livedata.Transformations;
import fr.uha.jacquey.hospitalbed.management.database.PatientDao;
import fr.uha.jacquey.hospitalbed.management.model.Gender;
import fr.uha.jacquey.hospitalbed.management.model.Patient;

public class PatientViewModel extends ViewModel {

    static private final String TAG = PatientViewModel.class.getSimpleName();

    private PatientDao PatientDao;
    private MutableLiveData<Long> id = new MutableLiveData<>();
    private LiveData<Patient> Patient;
    private MutableLiveData<String> firstName;
    private MutableLiveData<String> lastName;
    private MutableLiveData<Gender> gender;
    private MutableLiveData<Date> incomingDate;
    private MutableLiveData<Date> dischargeDate;
    private MediatorLiveData<Double> bmi;

    private PatientComparator comparator;
    private PatientValidator validator;
    private AndLiveData savable;

    private LiveData<Integer> bmiValidator;

    public void setPatientDao(PatientDao PatientDao) {
        this.PatientDao = PatientDao;
        this.Patient = Transformations.switchMap(id, p -> PatientDao.getById(p));
        this.firstName = Transformations.map(Patient, p -> p.getFirstName());
        this.lastName = Transformations.map(Patient, p -> p.getLastName());
        this.gender = Transformations.map(Patient, p -> p.getGender());
        this.incomingDate = Transformations.map(Patient, p -> p.getIncomingDate());
        this.dischargeDate = Transformations.map(Patient, p -> p.getDischargeDate());

        comparator = new PatientComparator(this);
        validator = new PatientValidator(this);
        savable = new AndLiveData(comparator.getModified(), validator.getValidated());

    }

    public LiveData<Patient> getPatient() {
        return Patient;
    }

    public MutableLiveData<String> getFirstName() {
        return firstName;
    }

    public MutableLiveData<String> getLastName() {
        return lastName;
    }

    public LiveData<Gender> getGender() {
        return gender;
    }

    public void setGender (Gender gender) {
        this.gender.postValue(gender);
    }

    public MutableLiveData<Date> getIncomingDate() {
        return incomingDate;
    }

    public MutableLiveData<Date> getDischargeDate() {
        return dischargeDate;
    }

    public LiveData<Boolean> getModified() {
        return comparator.getModified();
    }

    public LiveData<Integer> getFirstNameValidator() {
        return validator.getFirstNameValidator();
    }

    public LiveData<Integer> getLastNameValidator() {
        return validator.getLastNameValidator();
    }

    public LiveData<Integer> getGenderValidator() {
        return validator.getGenderValidator();
    }

    public LiveData<Integer> getIncomingDateValidator() {
        return validator.getIncomingDateValidator();
    }

    public LiveData<Integer> getDischargeDateValidator() {
        return validator.getDischargeDateValidator();
    }
    public LiveData<Boolean> getValidated() {
        return validator.getValidated();
    }

    public LiveData<Boolean> getSavable() {
        return savable.getSavable();
    }

    public void save() {
        Patient Patient = new Patient(
                id.getValue(),
                firstName.getValue(),
                lastName.getValue(),
                gender.getValue(),
                incomingDate.getValue(),
                dischargeDate.getValue()
        );
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
               PatientDao.upsert(Patient);}
        });
    }

    public void setId(long id) {
        this.id.postValue(id);
    }

    public void createPatient() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Patient p = new Patient();
                long id = PatientDao.upsert(p);
                setId(id);
            }
        });

    }

}