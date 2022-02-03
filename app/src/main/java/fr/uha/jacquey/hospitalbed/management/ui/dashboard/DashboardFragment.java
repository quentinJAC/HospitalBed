package fr.uha.jacquey.hospitalbed.management.ui.dashboard;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


import fr.uha.jacquey.hospitalbed.R;
import fr.uha.jacquey.hospitalbed.management.database.AppDatabase;
import fr.uha.jacquey.hospitalbed.management.database.FeedDatabase;
import fr.uha.jacquey.hospitalbed.databinding.DashboardFragmentBinding;
import fr.uha.jacquey.hospitalbed.management.model.Bed;
import fr.uha.jacquey.hospitalbed.management.model.Patient;
import fr.uha.jacquey.hospitalbed.management.model.Service;

public class DashboardFragment extends Fragment {

    private DashboardViewModel mViewModel;
    private BedAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        DashboardFragmentBinding binding = DashboardFragmentBinding.inflate(inflater, container, false);
        binding.list.setLayoutManager(new LinearLayoutManager(binding.list.getContext(), LinearLayoutManager.VERTICAL, false));

        DividerItemDecoration divider = new DividerItemDecoration(binding.list.getContext(), DividerItemDecoration.VERTICAL);
        binding.list.addItemDecoration(divider);

        adapter = new BedAdapter();
        binding.list.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        AppDatabase.isReady().observe(getViewLifecycleOwner(), base -> {
            if (base == null) return;
            mViewModel.setBedDao(base.getBedDao());
            mViewModel.getBeds().observe(getViewLifecycleOwner(), beds -> adapter.setCollection(beds));
        });
    }

    private class BedAdapter extends RecyclerView.Adapter<BedAdapter.ViewHolder> {


        private class ModelWrapper {
            int type;
            Bed bed;
            Service service;
            Patient patient;

            public ModelWrapper(int type, Bed bed) {
                this.type = type;
                this.bed = bed;
            }

            public ModelWrapper(int type, Service service) {
                this.type = type;
                this.service = service;
            }


            public ModelWrapper(int type, Patient patient) {
                this.type = type;
                this.patient = patient;
            }

        }

        private List<ModelWrapper> collection;

        private class ViewHolder extends RecyclerView.ViewHolder {
            ViewDataBinding binding;

            public ViewHolder(@NonNull ViewDataBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from((parent.getContext())), viewType, parent, false);
            binding.setLifecycleOwner(getViewLifecycleOwner());
            return new ViewHolder(binding);
        }

        @Override
        public int getItemViewType(int position) {
            ModelWrapper wrapper = collection.get(position);
            return wrapper.type;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ModelWrapper wrapper = collection.get(position);
            switch (holder.getItemViewType()) {
                case R.layout.bed_item:
                    holder.binding.setVariable(BR.t, wrapper.bed);
                    break;
                case R.layout.patient_item:
                    holder.binding.setVariable(BR.p, wrapper.patient);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return collection == null ? 0 : collection.size();
        }


        private List<ModelWrapper> translate(List<BedPatientManagement> collection) {
            List<ModelWrapper> translated = new ArrayList<>(collection.size() * 5);
            for (BedPatientManagement bed : collection) {
                translated.add(new ModelWrapper(R.layout.bed_item, bed.bed));
                translated.add(new ModelWrapper(R.layout.patient_item, bed.patient));
            }

            return translated;
        }


        public void setCollection(List<BedPatientManagement> collection) {
            this.collection = translate(collection);
            notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.dashboard_menu, menu);
    }

    private boolean doPopulate() {
        FeedDatabase feeder = new FeedDatabase();
        feeder.feed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.populate:
                return doPopulate();
        }
        return super.onOptionsItemSelected(item);
    }
}

