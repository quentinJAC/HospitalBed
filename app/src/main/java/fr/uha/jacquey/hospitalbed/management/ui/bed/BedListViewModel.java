package fr.uha.jacquey.hospitalbed.management.ui.bed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.uha.jacquey.hospitalbed.management.database.BedDao;
import fr.uha.jacquey.hospitalbed.management.model.Bed;


public class BedListViewModel extends ViewModel {

    private BedDao bedDao;
    private MediatorLiveData<List<Bed>> beds;

    public void setBedDao(BedDao bedDao) {
        this.bedDao = bedDao;
        this.beds = new MediatorLiveData<>();
        this.beds.addSource(bedDao.getAll(), beds::setValue);
    }

    public LiveData<List<Bed>> getBeds() {
        return beds;
    }

    public void deleteBed(Bed bed) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                bedDao.delete(bed);
            }
        });
    }

}