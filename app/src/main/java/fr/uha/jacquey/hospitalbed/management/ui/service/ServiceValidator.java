package fr.uha.jacquey.hospitalbed.management.ui.service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.Date;

import fr.uha.jacquey.hospitalbed.R;
import fr.uha.jacquey.hospitalbed.livedata.Transformations;
import fr.uha.jacquey.hospitalbed.management.model.Gender;

public class ServiceValidator {

    private ServiceViewModel vm;
    private LiveData<Integer> nameValidator;
    private MediatorLiveData<Boolean> validated;

    public ServiceValidator(ServiceViewModel vm) {
        this.vm = vm;
        this.nameValidator = Transformations.map(vm.getName(), v -> validateName(v));

        this.validated = new MediatorLiveData<>();
        this.validated.setValue(Boolean.FALSE);
        this.validated.addSource(nameValidator, serviceValidatorObserver);
    }

    public LiveData<Integer> getNameValidator() {
        return nameValidator;
    }

    public LiveData<Boolean> getValidated() {
        return validated;
    }

    private Observer<Integer> serviceValidatorObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer o) {
            boolean valid = true;
            valid = valid && nameValidator.getValue() != null && nameValidator.getValue() == 0;
            validated.postValue(valid);
        }
    };

    private int validateName(String value) {
        if (value == null) return R.string.field_not_null;
        if (value.isEmpty()) return R.string.field_not_empty;
        return 0;
    }


}