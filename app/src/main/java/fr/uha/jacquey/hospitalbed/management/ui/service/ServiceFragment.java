package fr.uha.jacquey.hospitalbed.management.ui.service;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import fr.uha.jacquey.hospitalbed.R;
import fr.uha.jacquey.hospitalbed.databinding.ServiceFragmentBinding;
import fr.uha.jacquey.hospitalbed.management.database.AppDatabase;
import fr.uha.jacquey.hospitalbed.ui.FragmentHelper;


public class ServiceFragment extends Fragment {

    static private final String TAG = ServiceFragment.class.getSimpleName();

    private ServiceViewModel mViewModel;
    private ServiceFragmentBinding binding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.service_fragment, container, false);
        binding.setLifecycleOwner(this);
    }

    private void rebuildTitle(boolean modified) {
        String title = getResources().getString(R.string.title_service_edit, modified ? "*" : "");
        FragmentHelper.changeTitle(this, title);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ServiceViewModel.class);
        mViewModel.setServiceDao(AppDatabase.get().getServiceDao());
        mViewModel.getModified().observe(getViewLifecycleOwner(), v -> rebuildTitle(v));
        mViewModel.getSavable().observe(getViewLifecycleOwner(), v -> {
            FragmentHelper.invalidateOptionsMenu(this);
        });
        long id = ServiceFragmentArgs.fromBundle(getArguments()).getId();
        if (id == 0) {
            mViewModel.createService();
        } else {
            mViewModel.setId(id);
        }
        binding.setVm(mViewModel);
    }

}