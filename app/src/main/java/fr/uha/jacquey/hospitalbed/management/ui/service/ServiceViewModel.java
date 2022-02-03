package fr.uha.jacquey.hospitalbed.management.ui.service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.uha.jacquey.hospitalbed.livedata.AndLiveData;
import fr.uha.jacquey.hospitalbed.livedata.Transformations;
import fr.uha.jacquey.hospitalbed.management.database.ServiceDao;
import fr.uha.jacquey.hospitalbed.management.model.Service;

public class ServiceViewModel extends ViewModel {

    static private final String TAG = ServiceViewModel.class.getSimpleName();

    private ServiceDao serviceDao;
    private MutableLiveData<Long> id = new MutableLiveData<>();
    private LiveData<Service> service;
    private MutableLiveData<String> name;

    private ServiceComparator comparator;
    private ServiceValidator validator;
    private AndLiveData savable;


    public void setServiceDao(ServiceDao serviceDao) {
        this.serviceDao = serviceDao;
        this.service = Transformations.switchMap(id, p -> serviceDao.getById(p));
        this.name = Transformations.map(service, p -> p.getName());

        comparator = new ServiceComparator(this);
        validator = new ServiceValidator(this);
        savable = new AndLiveData(comparator.getModified(), validator.getValidated());

    }

    public LiveData<Service> getService() {
        return service;
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public LiveData<Boolean> getModified() {
        return comparator.getModified();
    }

    public LiveData<Integer> getNameValidator() {
        return validator.getNameValidator();
    }

    public LiveData<Boolean> getValidated() {
        return validator.getValidated();
    }

    public LiveData<Boolean> getSavable() {
        return savable.getSavable();
    }

    public void save() {
        Service service = new Service(
                name.getValue()
        );
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
               ServiceDao.upsert(service);}
        });
    }

    public void setId(long id) {
        this.id.postValue(id);
    }

    public void createService() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Service p = new Service();
                long id = ServiceDao.upsert(p);
                setId(id);
            }
        });

    }

}