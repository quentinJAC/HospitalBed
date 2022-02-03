package fr.uha.jacquey.hospitalbed.management.ui.patient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.uha.jacquey.hospitalbed.R;
import fr.uha.jacquey.hospitalbed.databinding.PatientListFragmentBinding;
import fr.uha.jacquey.hospitalbed.management.database.AppDatabase;
import fr.uha.jacquey.hospitalbed.management.database.FeedDatabase;
import fr.uha.jacquey.hospitalbed.management.model.Patient;
import fr.uha.jacquey.hospitalbed.ui.ItemSwipeCallback;


public class PatientListFragment extends Fragment {

    private fr.uha.jacquey.hospitalbed.management.ui.Patient.PatientListViewModel mViewModel;
    private PatientAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        PatientListFragmentBinding binding =  PatientListFragmentBinding.inflate(inflater, container, false);
        binding.add.setOnClickListener(
                view -> NavHostFragment.findNavController(this).navigate(R.id.action_navigation_PatientList_to_Patient)
        );
        binding.list.setLayoutManager(new LinearLayoutManager(binding.list.getContext(), LinearLayoutManager.VERTICAL, false));

        DividerItemDecoration divider = new DividerItemDecoration(binding.list.getContext(), DividerItemDecoration.VERTICAL);
        binding.list.addItemDecoration(divider);

        ItemTouchHelper touchHelper = new ItemTouchHelper(
                new ItemSwipeCallback(getContext(), ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, new ItemSwipeCallback.SwipeListener() {
                    @Override
                    public void onSwiped(int direction, int position) {
                        Patient Patient = adapter.collection.get(position);
                        switch (direction) {
                        case ItemTouchHelper.LEFT:
                            mViewModel.deletePatient (Patient);
                            break;
                        case ItemTouchHelper.RIGHT:
                            PatientListFragmentDirections.ActionNavigationPatientListToPatient action = PatientListFragmentDirections.actionNavigationPatientListToPatient();
                            action.setId(Patient.getPid());
                            NavHostFragment.findNavController(PatientListFragment.this).navigate(action);
                            break;
                        }
                    }
                })
        );
        touchHelper.attachToRecyclerView(binding.list);
        adapter = new PatientAdapter();
        binding.list.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(fr.uha.jacquey.hospitalbed.management.ui.Patient.PatientListViewModel.class);

        AppDatabase.isReady().observe(getViewLifecycleOwner(), base -> {
            if (base == null) return;
            mViewModel.setPatientDao(base.getPatientDao());
            mViewModel.getPatients().observe(getViewLifecycleOwner(), Patients -> adapter.setCollection(Patients));
        });
    }

    private class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> {

        private List<Patient> collection;

        private class ViewHolder extends RecyclerView.ViewHolder {
            PatientItemBinding binding;
            public ViewHolder(@NonNull PatientItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
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
            holder.binding.setP(p);
        }

        @Override
        public int getItemCount() {
            return collection == null ? 0 : collection.size();
        }

        public void setCollection(List<Patient> collection) {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return PatientAdapter.this.collection == null ? 0 : PatientAdapter.this.collection.size();
                }

                @Override
                public int getNewListSize() {
                    return collection == null ? 0 : collection.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    Patient newPatient = collection.get(newItemPosition);
                    Patient oldPatient = PatientAdapter.this.collection.get(oldItemPosition);
                    return newPatient.getPatid() == oldPatient.getPatid();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                   Patient newPatient = collection.get(newItemPosition);
                    Patient oldPatient = PatientAdapter.this.collection.get(oldItemPosition);
                    return Patient.compare (newPatient, oldPatient);
                }
            });
            this.collection = collection;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.Patients_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mViewModel.setFilter (query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mViewModel.setFilter (newText);
                return false;
            }
        });
    }

    private boolean doPopulate (){
        FeedDatabase feeder = new FeedDatabase();
        feeder.feed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
        case R.id.populate: return doPopulate();
        }
        return super.onOptionsItemSelected(item);
    }

}