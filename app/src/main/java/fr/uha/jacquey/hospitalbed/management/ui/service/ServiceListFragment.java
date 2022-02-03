package fr.uha.jacquey.hospitalbed.management.ui.service;

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
import fr.uha.jacquey.hospitalbed.databinding.ServiceItemBinding;
import fr.uha.jacquey.hospitalbed.databinding.ServiceListFragmentBinding;
import fr.uha.jacquey.hospitalbed.management.database.AppDatabase;
import fr.uha.jacquey.hospitalbed.management.database.FeedDatabase;
import fr.uha.jacquey.hospitalbed.management.model.Service;
import fr.uha.jacquey.hospitalbed.ui.ItemSwipeCallback;


public class ServiceListFragment extends Fragment {

    private ServiceListViewModel mViewModel;
    private ServiceAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ServiceListFragmentBinding binding =  ServiceListFragmentBinding.inflate(inflater, container, false);
        binding.add.setOnClickListener(
                view -> NavHostFragment.findNavController(this).navigate(R.id.action_navigation_serviceList_to_service)
        );
        binding.list.setLayoutManager(new LinearLayoutManager(binding.list.getContext(), LinearLayoutManager.VERTICAL, false));

        DividerItemDecoration divider = new DividerItemDecoration(binding.list.getContext(), DividerItemDecoration.VERTICAL);
        binding.list.addItemDecoration(divider);

        ItemTouchHelper touchHelper = new ItemTouchHelper(
                new ItemSwipeCallback(getContext(), ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, new ItemSwipeCallback.SwipeListener() {
                    @Override
                    public void onSwiped(int direction, int position) {
                        Service service = adapter.collection.get(position);
                        switch (direction) {
                        case ItemTouchHelper.LEFT:
                            mViewModel.deleteService (service);
                            break;
                        case ItemTouchHelper.RIGHT:
                            ServiceListFragmentDirections.ActionNavigationServiceListToService action = ServiceListFragmentDirections.actionNavigationServiceListToService();
                            action.setId(service.getsId());
                            NavHostFragment.findNavController(ServiceListFragment.this).navigate(action);
                            break;
                        }
                    }
                })
        );
        touchHelper.attachToRecyclerView(binding.list);
        adapter = new ServiceAdapter();
        binding.list.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ServiceListViewModel.class);

        AppDatabase.isReady().observe(getViewLifecycleOwner(), base -> {
            if (base == null) return;
            mViewModel.setServiceDao(base.getServiceDao());
            mViewModel.getServices().observe(getViewLifecycleOwner(), services -> adapter.setCollection(services));
        });
    }

    private class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

        private List<Service> collection;

        private class ViewHolder extends RecyclerView.ViewHolder {
            ServiceItemBinding binding;
            public ViewHolder(@NonNull ServiceItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ServiceItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from((parent.getContext())), R.layout.service_item, parent, false);
            binding.setLifecycleOwner(getViewLifecycleOwner());
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Service p = collection.get(position);
            holder.binding.setP(p);
        }

        @Override
        public int getItemCount() {
            return collection == null ? 0 : collection.size();
        }

        public void setCollection(List<Service> collection) {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return ServiceAdapter.this.collection == null ? 0 : ServiceAdapter.this.collection.size();
                }

                @Override
                public int getNewListSize() {
                    return collection == null ? 0 : collection.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    Service newService = collection.get(newItemPosition);
                    Service oldService = ServiceAdapter.this.collection.get(oldItemPosition);
                    return newService.getsId() == oldService.getsId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Service newService = collection.get(newItemPosition);
                    Service oldService = ServiceAdapter.this.collection.get(oldItemPosition);
                    return Service.compare (newService, oldService);
                }
            });
            this.collection = collection;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.services_menu, menu);
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