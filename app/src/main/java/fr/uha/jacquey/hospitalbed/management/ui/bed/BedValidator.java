package fr.uha.jacquey.hospitalbed.management.ui.bed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import fr.uha.jacquey.hospitalbed.R;
import fr.uha.jacquey.hospitalbed.livedata.Transformations;
import fr.uha.jacquey.hospitalbed.management.model.Patient;
import fr.uha.jacquey.hospitalbed.management.model.Service;


public class BedValidator {

    private BedViewModel vm;
    private LiveData<Long> numeroValidator;
    private LiveData<Long> serviceValidator;
    private LiveData<Long> PatientValidator;

    private MediatorLiveData<Boolean> validated;

    public BedValidator(BedViewModel vm) {
        this.vm = vm;

        this.numeroValidator = Transformations.map(vm.getNumero(), v -> validateNumero(v));
        this.serviceValidator = Transformations.map(vm.getService(), v -> validateService(v));
        this.PatientValidator = Transformations.map(vm.getPatient(), v -> validatePatient(v));

        this.validated = new MediatorLiveData<>();
        this.validated.setValue(Boolean.FALSE);
        this.validated.addSource(numeroValidator, bedValidatorObserver);
        this.validated.addSource(serviceValidator, bedValidatorObserver);
        this.validated.addSource(PatientValidator, bedValidatorObserver);
    }

    public LiveData<Long> getNumeroValidator() {
        return numeroValidator;
    }

    public LiveData<Long> getServiceValidator() {
        return serviceValidator;
    }

    public LiveData<Long> getPatientValidator() {
        return PatientValidator;
    }

    public MediatorLiveData<Boolean> getValidated() {
        return validated;
    }

    private Observer<Long> bedValidatorObserver = new Observer<Long>() {
        @Override
        public void onChanged(Long o) {
            boolean valid = true;
            valid = valid && numeroValidator.getValue() != null && numeroValidator.getValue() == 0;
            valid = valid && serviceValidator.getValue() != null && serviceValidator.getValue() == 0;
            valid = valid && PatientValidator.getValue() != null && PatientValidator.getValue() == 0;
            validated.postValue(valid);
        }
    };

    private long validateNumero(long value) {
        if (value==0) return R.string.field_not_null;
        return 0;
    }

    private long validateService(Service value) {
        if (value == null) return R.string.service_not_set;
        return 0;
    }

    private long validatePatient(Patient value) {
        if (value == null) return R.string.Patient_not_set;
        return 0;
    }

}
