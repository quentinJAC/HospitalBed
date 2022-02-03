package fr.uha.jacquey.hospitalbed.management.ui.bed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.uha.jacquey.hospitalbed.R;
import fr.uha.jacquey.hospitalbed.databinding.BedFragmentBinding;
import fr.uha.jacquey.hospitalbed.databinding.PatientItemBinding;
import fr.uha.jacquey.hospitalbed.management.database.AppDatabase;
import fr.uha.jacquey.hospitalbed.management.model.Patient;
import fr.uha.jacquey.hospitalbed.management.model.Service;
import fr.uha.jacquey.hospitalbed.management.ui.picker.PatientPickerFragment;
import fr.uha.jacquey.hospitalbed.management.ui.picker.ServicePickerFragment;
import fr.uha.jacquey.hospitalbed.ui.FragmentHelper;


public class BedFragment extends Fragment {

    static private final String TAG = BedFragment.class.getSimpleName();

    private static final String NUMERO = "numero";
    private static final String SERVICE = "service";
    private static final String PATIENT = "Patient";

    private BedViewModel mViewModel;
    private BedFragmentBinding binding;
    private PatientAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getParentFragmentManager().setFragmentResultListener(NUMERO, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey.equals(NUMERO)) {
                    long numero = result.getLong(BedPickerFragment.ID);
                    mViewModel.setNumero(numero);
                }
            }
        });
        getParentFragmentManager().setFragmentResultListener(SERVICE, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey.equals(SERVICE)) {
                    long serviceId = result.getLong(ServicePickerFragment.ID);
                    mViewModel.setService(serviceId);
                }
            }
        });
        getParentFragmentManager().setFragmentResultListener(PATIENT, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey.equals(PATIENT)) {
                    long PatientId = result.getLong(PatientPickerFragment.ID);
                    mViewModel.setPatient(PatientId);
                }
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.bed_fragment, container, false);
        binding.setLifecycleOwner(this);

        binding.setChangeService(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BedFragmentDirections.ActionNavigationBedToServicePicker action = BedFragmentDirections.actionNavigationBedToServicePicker();
                action.setRequestKey(SERVICE);
                Service service = mViewModel.getService().getValue();
                if (service == null) {
                    service = new Service();
                }
                action.setService(service);
                NavHostFragment.findNavController(BedFragment.this).navigate(action);
            }
        });
        binding.setChangePatient(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BedFragmentDirections.ActionNavigationBedToPatientPicker action = BedFragmentDirections.actionNavigationBedToPatientPicker();
                action.setRequestKey(PATIENT);
                NavHostFragment.findNavController(BedFragment.this).navigate(action);
            }
        });
        binding.list.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext(), LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration divider = new DividerItemDecoration(binding.list.getContext(), DividerItemDecoration.VERTICAL);
        binding.list.addItemDecoration(divider);

        adapter = new PatientAdapter();
        binding.list.setAdapter(adapter);

        return binding.getRoot();
    }

    private void rebuildTitle(boolean modified) {
        String title = getResources().getString(R.string.title_bed_edit, modified ? "*" : "");
        FragmentHelper.changeTitle(this, title);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BedViewModel.class);
        mViewModel.setPatientDao(AppDatabase.get().getPatientDao());
        mViewModel.setBedDao(AppDatabase.get().getBedDao());
        mViewModel.setServiceDao(AppDatabase.get().getServiceDao());
        mViewModel.getModified().observe(getViewLifecycleOwner(), v -> rebuildTitle(v));
        mViewModel.getSavable().observe(getViewLifecycleOwner(), v -> {
            FragmentHelper.invalidateOptionsMenu(this);
        });
        mViewModel.getService().observe(getViewLifecycleOwner(), service -> {
            binding.service.setP(service);
        });
        mViewModel.getPatient().observe(getViewLifecycleOwner(), Patient -> {
            adapter.setPatient(Patient);
        });
        long id = BedFragmentArgs.fromBundle(getArguments()).getId();
        if (id == 0) {
            mViewModel.createBed();
        } else {
            mViewModel.setId(id);
        }
        binding.setVm(mViewModel);
    }

    private class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> {

        private List<Patient> collection;

        private class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
            ViewDataBinding binding;

            public ViewHolder(@NonNull ViewDataBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
                binding.getRoot().setOnLongClickListener(this);
            }

            @Override
            public boolean onLongClick(View v) {
                Patient patient = collection.get(getLayoutPosition());
                mViewModel.removePatient(patient);
                return true;
            }

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            PatientItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from((parent.getContext())), R.layout.Patient_item, parent, false);
            binding.setLifecycleOwner(getViewLifecycleOwner());
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Patient p = collection.get(position);
            holder.binding.setVariable(BR.p, p);
            holder.binding.executePendingBindings();
        }

        @Override
        public int getItemCount() {
            return collection == null ? 0 : collection.size();
        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.bed_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.save).setEnabled(mViewModel.getSavable().getValue());
    }

    private boolean doSave() {
        mViewModel.save();
        Navigation.findNavController(getView()).popBackStack();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                return doSave();
        }
        return super.onOptionsItemSelected(item);
    }

}