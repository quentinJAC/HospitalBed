package fr.uha.jacquey.hospitalbed.management.ui.picker;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.uha.jacquey.hospitalbed.R;
import fr.uha.jacquey.hospitalbed.databinding.PatientPickerFragmentBinding;
import fr.uha.jacquey.hospitalbed.databinding.PatientPickerItemBinding;
import fr.uha.jacquey.hospitalbed.management.database.AppDatabase;


public class PatientPickerFragment extends DialogFragment {

    static public final String ID = "id";
    private PatientPickerFragmentBinding binding;
    private PatientAdapter adapter;
    private String requestKey;
    private float level;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        PatientPickerFragmentArgs arg = PatientPickerFragmentArgs.fromBundle(getArguments());
        requestKey = arg.getRequestKey();
        level = arg.getLevel();
        binding = PatientPickerFragmentBinding.inflate(inflater, container, false);
        binding.list.setLayoutManager(new LinearLayoutManager(binding.list.getContext(), RecyclerView.VERTICAL, false));
        adapter = new PatientAdapter ();
        binding.list.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppDatabase.get().getPatientDao().getAll().observe(getViewLifecycleOwner(), p -> adapter.setCollection((p));
    }


    @Override
    public void onResume() {
        Window window = getDialog().getWindow();
        Point point = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(point);
        window.setLayout((int) (point.x * 0.75), (int) (point.y * 0.75));
        window.setGravity (Gravity.CENTER);
        super.onResume();
    }

    private class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> {

        private List<PatientPicker> collection;

        private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            PatientPickerItemBinding binding;
            public ViewHolder(@NonNull PatientPickerItemBinding binding) {
                super(binding.getRoot());
                binding.getRoot().setOnClickListener(this);
                this.binding = binding;
            }

            @Override
            public void onClick(View v) {
                PatientPicker Patient = collection.get(getBindingAdapterPosition());
                Bundle result = new Bundle();
                result.putLong(ID, Patient.Patient.getPatId();
                getParentFragmentManager().setFragmentResult(requestKey, result);
                dismiss();
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            PatientPickerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from((parent.getContext())), R.layout.Patient_picker_item, parent, false);
            binding.setLifecycleOwner(getViewLifecycleOwner());
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            PatientPicker p = collection.get(position);
            holder.binding.setP(p);
        }

        @Override
        public int getItemCount() {
            return collection == null ? 0 : collection.size();
        }

        public void setCollection(List<PatientPicker> collection) {
            this.collection = collection;
            notifyDataSetChanged();
        }
    }

}
