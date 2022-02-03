package fr.uha.jacquey.hospitalbed.management.ui.service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.uha.jacquey.hospitalbed.livedata.Transformations;
import fr.uha.jacquey.hospitalbed.management.database.ServiceDao;
import fr.uha.jacquey.hospitalbed.management.model.Service;


public class ServiceListViewModel extends ViewModel {

    private ServiceDao serviceDao;
    private MutableLiveData<String> filter;
    private MediatorLiveData<List<Service>> service;

    public void setServiceDao(ServiceDao serviceDao) {
        this.serviceDao = serviceDao;
        filter = new MutableLiveData<> ("");
        this.service = Transformations.switchMap(filter, f -> f.isEmpty() ? serviceDao.getAll() : serviceDao.getAll());
    }

    public LiveData<List<Service>> getServices() {
        return service;
    }

    public void deleteService(Service service) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                serviceDao.delete(service);
            }
        });
    }

    public void setFilter(String query) {
        this.filter.postValue(query);
    }

}