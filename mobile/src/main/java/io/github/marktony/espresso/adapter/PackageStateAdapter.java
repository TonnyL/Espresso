package io.github.marktony.espresso.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.component.CircleImageView;
import io.github.marktony.espresso.entity.PackageState;

/**
 * Created by lizhaotailang on 2017/2/11.
 */

public class PackageStateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final Context context;
    private final LayoutInflater inflater;
    private List<PackageState> list;

    public PackageStateAdapter(@NonNull Context context, List<PackageState> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PackageStateViewHolder(inflater.inflate(R.layout.package_state_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PackageState item = list.get(position);
        ((PackageStateViewHolder)holder).textViewStatus.setText(item.getData().get(0).getContext());
        ((PackageStateViewHolder)holder).textViewPackageName.setText(String.valueOf(position));
        ((PackageStateViewHolder)holder).textViewTime.setText(item.getData().get(0).getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PackageStateViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imageView;
        private TextView textViewPackageName;
        private TextView textViewStatus;
        private TextView textViewTime;

        public PackageStateViewHolder(View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.imageView);
            textViewPackageName = (TextView) itemView.findViewById(R.id.textViewPackageName);
            textViewStatus = (TextView) itemView.findViewById(R.id.textViewStatus);
            textViewTime = (TextView) itemView.findViewById(R.id.textViewTime);
        }
    }

}
