package fr.uha.jacquey.hospitalbed.management.ui.bed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.EnumMap;

import fr.uha.jacquey.hospitalbed.management.model.Patient;


public class BedComparator {

    private BedViewModel vm;
    private MediatorLiveData<Boolean> modified;
    private enum Field {
        NUMERO,
        PATIENT
    }

    private EnumMap<Field, Boolean> states;

    public BedComparator(BedViewModel vm) {
        this.vm = vm;
        states = new EnumMap<>(Field.class);
        for (Field key : Field.values()) {
            states.put(key, Boolean.FALSE);
        }
        this.modified = new MediatorLiveData<>();
        this.modified.setValue(Boolean.FALSE);
        this.modified.addSource(vm.getNumero(), newValue -> modificationUpdater (Field.NUMERO) );
        this.modified.addSource(vm.getPatient(), newValue -> modificationUpdater (Field.PATIENT) );
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
        case NUMERO: value = compareNumeros(); break;
        case PATIENT: value = comparePatient(); break;
        }
        states.put(key, value);
        modified.postValue(! checkStates(value));
    }

    private boolean compareNumeros () {
        if (vm.getNumero().getValue() == null) {
            if (vm.getBed().getValue() == null) return false;
            return vm.getBed().getValue().getNumero() == 0;
        } else {
            if (vm.getBed().getValue() == null) return false;
            return vm.getNumero().getValue().equals(vm.getBed().getValue().getNumero());
        }
    }


    private boolean comparePatient () {
        if (vm.getPatient().getValue() == null) {
            if (vm.getBed().getValue() == null) return false;
            return vm.getBed().getValue().getPatient() == null;
        } else {
            if (vm.getBed().getValue() == null) return false;
            Patient now = vm.getPatient().getValue();
            Patient initial = vm.getBed().getValue().getPatient();
            if (initial == null) return true;
            if (initial != now) return false;
            return true;
        }
    }


}
