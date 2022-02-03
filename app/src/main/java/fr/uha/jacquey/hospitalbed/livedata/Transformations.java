package fr.uha.jacquey.hospitalbed.livedata;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

public class Transformations {

    private Transformations() {
    }

    // forward to the original implementation so only one import is required in the viewmodel
    public static <X, Y> MediatorLiveData<Y> map(@NonNull LiveData<X> source, @NonNull final Function<X, Y> mapFunction) {
        return (MediatorLiveData<Y>) androidx.lifecycle.Transformations.map(source, mapFunction);
    }

    @MainThread
    @NonNull
    public static <X, Y> MediatorLiveData<Y> mapIfPreviousNull(@NonNull LiveData<X> source, @NonNull final Function<X, Y> mapFunction) {
        final MediatorLiveData<Y> result = new MediatorLiveData<>();
        result.addSource(source, new Observer<X>() {
            public void onChanged(@Nullable X x) {
                if (result.getValue() == null) {
                    result.setValue(mapFunction.apply(x));
                }
            }
        });
        return result;
    }

    // forward to the original implementation so only one import is required in the viewmodel
    public static <X, Y> MediatorLiveData<Y> switchMap(@NonNull LiveData<X> source, @NonNull final Function<X, LiveData<Y>> switchMapFunction) {
        return (MediatorLiveData<Y>) androidx.lifecycle.Transformations.switchMap(source, switchMapFunction);
    }

    @MainThread
    @NonNull
    public static <X, Y> MediatorLiveData<List<Y>> addToList(
            @NonNull LiveData<X> source,
            @NonNull final Function<X, LiveData<Y>> switchMapFunction) {
        final MediatorLiveData<List<Y>> result = new MediatorLiveData<>();
        result.addSource(source, new Observer<X>() {
            LiveData<Y> mSource;

            @Override
            public void onChanged(@Nullable X x) {
                LiveData<Y> newLiveData = switchMapFunction.apply(x);
                if (mSource == newLiveData) {
                    return;
                }
                if (mSource != null) {
                    result.removeSource(mSource);
                }
                mSource = newLiveData;
                if (mSource != null) {
                    result.addSource(mSource, new Observer<Y>() {
                        @Override
                        public void onChanged(@Nullable Y y) {
                            result.getValue().add(y);
                            result.setValue(result.getValue());
                        }
                    });
                }
            }
        });
        return result;
    }

}
