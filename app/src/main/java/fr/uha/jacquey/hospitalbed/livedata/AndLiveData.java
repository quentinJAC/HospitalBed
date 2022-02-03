package fr.uha.jacquey.hospitalbed.livedata;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

public class AndLiveData {

    private LiveData<Boolean>[] liveDataCollection;

    private MediatorLiveData<Boolean> savable;

    public AndLiveData(LiveData<Boolean> ... lds) {
        this.liveDataCollection = lds;

        this.savable = new MediatorLiveData<>();
        this.savable.setValue(Boolean.FALSE);
        for (LiveData<Boolean> ld : liveDataCollection) {
            this.savable.addSource(ld, savableObserver);
        }
    }

    public LiveData<Boolean> getSavable() {
        return savable;
    }

    private Observer<Boolean> savableObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean value) {
            boolean valid = true;
            for (LiveData<Boolean> ld : liveDataCollection) {
                valid = valid && ld.getValue() != null && ld.getValue();
            }
            savable.postValue(valid);
        }
    };

}
