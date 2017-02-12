package io.github.marktony.espresso.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.entity.Package;

/**
 * Created by lizhaotailang on 2017/2/12.
 */

public class PackageStatusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final Context context;

    private final LayoutInflater inflater;
    private List<Package.Data> list;

    public PackageStatusAdapter(@NonNull Context context, List<Package.Data> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PackageStatusViewHolder(inflater.inflate(R.layout.package_status_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Package.Data item = list.get(position);
        ((PackageStatusViewHolder)holder).textViewTime.setText(item.getTime());
        ((PackageStatusViewHolder)holder).textViewLocation.setText(item.getContext());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PackageStatusViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView textViewLocation;
        private AppCompatTextView textViewTime;

        public PackageStatusViewHolder(View itemView) {
            super(itemView);
            textViewLocation = (AppCompatTextView) itemView.findViewById(R.id.textViewLocation);
            textViewTime = (AppCompatTextView) itemView.findViewById(R.id.textViewTime);
        }
    }

}
