package fr.uha.jacquey.hospitalbed.management.ui.patient;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class PatientComparator {

    private PatientViewModel vm;
    private MediatorLiveData<Boolean> modified;

    private enum Field {
        FIRSTNAME,
        LASTNAME,
        GENDER,
        INCOMINGDATE,
        DISCHARGEDATE
    }

    private EnumMap<Field, Boolean> states;

    public PatientComparator(fr.uha.jacquey.hospitalbed.management.ui.Patient.PatientViewModel vm) {
        this.vm = vm;
        states = new EnumMap<>(Field.class);
        for (Field key : Field.values()) {
            states.put(key, Boolean.FALSE);
        }
        this.modified = new MediatorLiveData<>();
        this.modified.setValue(Boolean.FALSE);
        this.modified.addSource(vm.getFirstName(), newValue -> modificationUpdater (Field.FIRSTNAME) );
        this.modified.addSource(vm.getLastName(), newValue -> modificationUpdater (Field.LASTNAME) );
        this.modified.addSource(vm.getGender(), newValue -> modificationUpdater (Field.GENDER) );
        this.modified.addSource(vm.getIncomingDate(), newValue -> modificationUpdater (Field.INCOMINGDATE) );
        this.modified.addSource(vm.getDischargeDate(), newValue -> modificationUpdater (Field.DISCHARGEDATE) );

    }

    public LiveData<Boolean> getModified() {
        return modified;
    }

    private boolean checkStates (boolean lastUpdate) {
        if (! lastUpdate) return false;
        for (Field key : Field.values()) {
            if (! states.get(key)) return false;
        }
        return true;
    }

    private void modificationUpdater (Field key) {
        boolean value = false;
        switch (key) {
        case FIRSTNAME: value = compareFirstNames(); break;
        case LASTNAME: value = compareLastNames(); break;
        case GENDER: value = compareGenders(); break;
        case INCOMINGDATE: value = compareIncomingDates(); break;
        case DISCHARGEDATE: value = compareDischargeDates(); break;

        }
        states.put(key, value);
        modified.postValue(! checkStates(value));
    }

    private boolean compareFirstNames () {
        if (vm.getFirstName().getValue() == null) {
            if (vm.getPatient().getValue() == null) return false;
            return vm.getPatient().getValue().getFirstName() == null;
        } else {
            if (vm.getPatient().getValue() == null) return false;
            return vm.getFirstName().getValue().equals(vm.getPatient().getValue().getFirstName());
        }
    }

    private boolean compareLastNames () {
        if (vm.getLastName().getValue() == null) {
            if (vm.getPatient().getValue() == null) return false;
            return vm.getPatient().getValue().getLastName() == null;
        } else {
            if (vm.getPatient().getValue() == null) return false;
            return vm.getLastName().getValue().equals(vm.getPatient().getValue().getLastName());
        }
    }

    private boolean compareGenders () {
        if (vm.getGender().getValue() == null) {
            if (vm.getPatient().getValue() == null) return false;
            return vm.getPatient().getValue().getGender() == null;
        } else {
            if (vm.getPatient().getValue() == null) return false;
            if (vm.getPatient().getValue().getGender() == null) return false;
            return vm.getGender().getValue().ordinal() == vm.getPatient().getValue().getGender().ordinal();
        }
    }

    private boolean compareIncomingDates () {
        if (vm.getIncomingDate().getValue() == null) {
            if (vm.getPatient().getValue() == null) return false;
            return false;
        } else {
            if (vm.getPatient().getValue() == null) return false;
            return vm.getIncomingDate().getValue() == vm.getPatient().getValue().getIncomingDate();
        }
    }

    private boolean compareDischargeDates () {
        if (vm.getDischargeDate().getValue() == null) {
            if (vm.getPatient().getValue() == null) return false;
            return false;
        } else {
            if (vm.getPatient().getValue() == null) return false;
            return vm.getDischargeDate().getValue() == vm.getPatient().getValue().getDischargeDate();
        }
    }

}