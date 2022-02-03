package fr.uha.jacquey.hospitalbed.management.ui.service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.EnumMap;


public class ServiceComparator {

    private ServiceViewModel vm;
    private MediatorLiveData<Boolean> modified;

    private enum Field {
        NAME
    }

    private EnumMap<Field, Boolean> states;

    public ServiceComparator(ServiceViewModel vm) {
        this.vm = vm;
        states = new EnumMap<>(Field.class);
        for (Field key : Field.values()) {
            states.put(key, Boolean.FALSE);
        }
        this.modified = new MediatorLiveData<>();
        this.modified.setValue(Boolean.FALSE);
        this.modified.addSource(vm.getName(), newValue -> modificationUpdater (Field.NAME) );

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
        case NAME: value = compareNames(); break;

        }
        states.put(key, value);
        modified.postValue(! checkStates(value));
    }

    private boolean compareNames () {
        if (vm.getName().getValue() == null) {
            if (vm.getService().getValue() == null) return false;
            return vm.getService().getValue().getName() == null;
        } else {
            if (vm.getService().getValue() == null) return false;
            return vm.getName().getValue().equals(vm.getService().getValue().getName());
        }
    }


}