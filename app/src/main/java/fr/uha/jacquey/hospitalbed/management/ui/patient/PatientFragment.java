package fr.uha.jacquey.hospitalbed.management.ui.patient;

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
import fr.uha.jacquey.hospitalbed.databinding.PatientFragmentBinding;
import fr.uha.jacquey.hospitalbed.management.database.AppDatabase;
import fr.uha.jacquey.hospitalbed.ui.FragmentHelper;


public class PatientFragment extends Fragment {

    static private final String TAG = PatientFragment.class.getSimpleName();

    private PatientViewModel mViewModel;
    private PatientFragmentBinding binding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.Patient_fragment, container, false);
        binding.setLifecycleOwner(this);
    }

    private void rebuildTitle(boolean modified) {
        String title = getResources().getString(R.string.title_Patient_edit, modified ? "*" : "");
        FragmentHelper.changeTitle(this, title);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PatientViewModel.class);
        mViewModel.setPatientDao(AppDatabase.get().getPatientDao());
        mViewModel.getModified().observe(getViewLifecycleOwner(), v -> rebuildTitle(v));
        mViewModel.getSavable().observe(getViewLifecycleOwner(), v -> {
            FragmentHelper.invalidateOptionsMenu(this);
        });
        long id = PatienFragmentArgs.fromBundle(getArguments()).getId();
        if (id == 0) {
            mViewModel.createPatient();
        } else {
            mViewModel.setId(id);
        }
        binding.setVm(mViewModel);
    }

}