package fr.uha.jacquey.hospitalbed.management.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import fr.uha.jacquey.hospitalbed.management.database.BedDao;

public class DashboardViewModel extends ViewModel {

    private BedDao bedDao;
    private MediatorLiveData<List<BedServiceManagement>> beds;

    public void setBedDao(BedDao bedDao) {
        this.bedDao = bedDao;
        this.beds = new MediatorLiveData<>();
        this.beds.addSource(bedDao.getAllWithServices(), beds::setValue);
    }

    public LiveData<List<BedServiceManagement>> getBeds() {
        return beds;
    }

}