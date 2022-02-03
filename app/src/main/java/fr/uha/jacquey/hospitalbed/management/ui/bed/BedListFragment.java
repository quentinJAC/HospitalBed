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
import fr.uha.jacquey.hospitalbed.databinding.BedItemBinding;
import fr.uha.jacquey.hospitalbed.databinding.BedListFragmentBinding;
import fr.uha.jacquey.hospitalbed.management.database.AppDatabase;
import fr.uha.jacquey.hospitalbed.management.database.FeedDatabase;
import fr.uha.jacquey.hospitalbed.management.model.Bed;
import fr.uha.jacquey.hospitalbed.ui.ItemSwipeCallback;


public class BedListFragment extends Fragment {

    private BedListViewModel mViewModel;
    private BedAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        BedListFragmentBinding binding =  BedListFragmentBinding.inflate(inflater, container, false);
        binding.add.setOnClickListener(
                view -> NavHostFragment.findNavController(this).navigate(R.id.action_navigation_bedList_to_bed)
        );
        binding.list.setLayoutManager(new LinearLayoutManager(binding.list.getContext(), LinearLayoutManager.VERTICAL, false));

        DividerItemDecoration divider = new DividerItemDecoration(binding.list.getContext(), DividerItemDecoration.VERTICAL);
        binding.list.addItemDecoration(divider);

        ItemTouchHelper touchHelper = new ItemTouchHelper(
                new ItemSwipeCallback(getContext(), ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, new ItemSwipeCallback.SwipeListener() {
                    @Override
                    public void onSwiped(int direction, int position) {
                        Bed bed = adapter.collection.get(position);
                        switch (direction) {
                        case ItemTouchHelper.LEFT:
                            mViewModel.deleteBed(bed);
                            break;
                        case ItemTouchHelper.RIGHT:
                            BedListFragmentDirections.ActionNavigationBedListToBed action = BedListFragmentDirections.actionNavigationBedListToBed();
                            action.setbId(bed.getBedid());
                            NavHostFragment.findNavController(BedListFragment.this).navigate(action);
                            break;
                        }
                    }
                })
        );
        touchHelper.attachToRecyclerView(binding.list);
        adapter = new BedAdapter();
        binding.list.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BedListViewModel.class);

        AppDatabase.isReady().observe(getViewLifecycleOwner(), base -> {
            if (base == null) return;
            mViewModel.setBedDao(base.getBedDao());
            mViewModel.getBeds().observe(getViewLifecycleOwner(), beds -> adapter.setCollection(beds));
        });
    }

    private class BedAdapter extends RecyclerView.Adapter<BedAdapter.ViewHolder> {

        private List<Bed> collection;

        private class ViewHolder extends RecyclerView.ViewHolder {
            BedItemBinding binding;
            public ViewHolder(@NonNull BedItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            BedItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from((parent.getContext())), R.layout.bed_item, parent, false);
            binding.setLifecycleOwner(getViewLifecycleOwner());
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Bed t = collection.get(position);
            holder.binding.setT(t);
        }

        @Override
        public int getItemCount() {
            return collection == null ? 0 : collection.size();
        }

        public void setCollection(List<Bed> collection) {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return BedAdapter.this.collection == null ? 0 : BedAdapter.this.collection.size();
                }

                @Override
                public int getNewListSize() {
                    return collection == null ? 0 : collection.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    Bed newBed = collection.get(newItemPosition);
                    Bed oldBed = BedAdapter.this.collection.get(oldItemPosition);
                    return newBed.getBedid() == oldBed.getBedid();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Bed newBed = collection.get(newItemPosition);
                    Bed oldBed = BedAdapter.this.collection.get(oldItemPosition);
                    return Bed.compare (newBed, oldBed);
                }
            });
            this.collection = collection;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.beds_menu, menu);
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