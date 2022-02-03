package fr.uha.jacquey.hospitalbed.management.ui.patient;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.Date;
import java.util.List;
import fr.uha.jacquey.hospitalbed.R;
import fr.uha.jacquey.hospitalbed.livedata.Transformations;
import fr.uha.jacquey.hospitalbed.management.model.Gender;

public class PatientValidator {

    private PatientViewModel vm;
    private LiveData<Integer> firstNameValidator;
    private LiveData<Integer> lastNameValidator;
    private LiveData<Integer> genderValidator;
    private LiveData<Integer> incomingDateValidator;
    private LiveData<Integer> dischargeDateValidator;

    private MediatorLiveData<Boolean> validated;

    public PatientValidator(PatientViewModel vm) {
        this.vm = vm;
        this.firstNameValidator = Transformations.map(vm.getFirstName(), v -> validateFirstName(v));
        this.lastNameValidator = Transformations.map(vm.getLastName(), v -> validateLastName(v));
        this.genderValidator = Transformations.map(vm.getGender(), v -> validateGender(v));
        this.incomingDateValidator = Transformations.map(vm.getIncomingDate(), v -> validateDate(v));
        this.dischargeDateValidator = Transformations.map(vm.getDischargeDate(), v -> validateDate(v));

        this.validated = new MediatorLiveData<>();
        this.validated.setValue(Boolean.FALSE);
        this.validated.addSource(firstNameValidator, PatientValidatorObserver);
        this.validated.addSource(lastNameValidator, PatientValidatorObserver);
        this.validated.addSource(genderValidator, PatientValidatorObserver);
        this.validated.addSource(incomingDateValidator, PatientValidatorObserver);
        this.validated.addSource(dischargeDateValidator, PatientValidatorObserver);
    }

    public LiveData<Integer> getFirstNameValidator() {
        return firstNameValidator;
    }

    public LiveData<Integer> getLastNameValidator() {
        return lastNameValidator;
    }

    public LiveData<Integer> getGenderValidator() {
        return genderValidator;
    }

    public LiveData<Integer> getIncomingDateValidator() {
        return incomingDateValidator;
    }

    public LiveData<Integer> getDischargeDateValidator() {
        return dischargeDateValidator;
    }

    public LiveData<Boolean> getValidated() {
        return validated;
    }

    private Observer<Integer> PatientValidatorObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer o) {
            boolean valid = true;
            valid = valid && firstNameValidator.getValue() != null && firstNameValidator.getValue() == 0;
            valid = valid && lastNameValidator.getValue() != null && lastNameValidator.getValue() == 0;
            valid = valid && genderValidator.getValue() != null && genderValidator.getValue() == 0;
            valid = valid && incomingDateValidator.getValue() != null && incomingDateValidator.getValue() == 0;
            valid = valid && dischargeDateValidator.getValue() != null && dischargeDateValidator.getValue() == 0;
            validated.postValue(valid);
        }
    };

    private int validateFirstName(String value) {
        if (value == null) return R.string.field_not_null;
        if (value.isEmpty()) return R.string.field_not_empty;
        return 0;
    }

    private int validateLastName(String value) {
        if (value == null) return R.string.field_not_null;
        if (value.isEmpty()) return R.string.field_not_empty;
        return 0;
    }

    private int validateGender(Gender value) {
        if (value == null) return R.string.field_not_null;
        switch (value) {
            case GIRL:
                return 0;
            case BOY:
                return 0;
        }
        return R.string.gender_not_valid;
    }

    private int validateDate(Date date) {
        if (date.getDay() < 1) return R.string.day_too_small;
        if (date.getDay() > 31) return R.string.day_too_big;
        if (date.getMonth() < 1) return R.string.month_too_small;
        if (date.getMonth() > 12) return R.string.month_too_big;
        if (((date.getMonth() == 4) || (date.getMonth() == 6) || (date.getMonth() == 9) || (date.getMonth() == 11)) && (date.getDay() > 30))
            return R.string.month_over_30;
        if ((date.getMonth() == 2) && (date.getDay() > 29)) return R.string.february_over_29;
        return 0;
    }

}