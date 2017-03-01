package io.github.marktony.espresso.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.component.CircleImageView;
import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.interfaze.OnRecyclerViewItemClickListener;

/**
 * Created by lizhaotailang on 2017/2/11.
 */

public class PackageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final Context context;

    @NonNull
    private final LayoutInflater inflater;

    @NonNull
    private List<Package> list;

    @Nullable
    private OnRecyclerViewItemClickListener listener;

    public PackageAdapter(@NonNull Context context, @NonNull List<Package> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PackageViewHolder(inflater.inflate(R.layout.package_item, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Package item = list.get(position);
        PackageViewHolder pvh = (PackageViewHolder) holder;

        if (item.getData() != null && item.getData().size() > 0) {

            int state = Integer.parseInt(item.getState());
            if (state == Package.STATUS_DELIVERED) {
                pvh.textViewStatus.setText(String.valueOf(context.getString(R.string.delivered) + " - " + item.getData().get(0).getContext()));
                pvh.textViewTime.setText(item.getData().get(0).getTime());
            } else {
                pvh.textViewTime.setText(item.getData().get(0).getTime());
                pvh.textViewStatus.setText(String.valueOf(context.getString(R.string.on_the_way) + " - " + item.getData().get(0).getContext()));
            }

        } else {
            pvh.textViewTime.setText("");
            pvh.textViewStatus.setText(R.string.get_status_error);
        }

        pvh.textViewPackageName.setText(item.getName());
        pvh.textViewAvatar.setText(item.getName().substring(0,1));
        pvh.circleImageViewAvatar.setImageResource(item.getColorAvatar());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnRecyclerViewItemOnClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public void updateData(@NonNull List<Package> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class PackageViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private AppCompatTextView textViewPackageName;
        private AppCompatTextView textViewStatus;
        private AppCompatTextView textViewTime;
        private AppCompatTextView textViewAvatar;
        private CircleImageView circleImageViewAvatar;

        private OnRecyclerViewItemClickListener listener;

        public PackageViewHolder(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            textViewPackageName = (AppCompatTextView) itemView.findViewById(R.id.textViewPackageName);
            textViewStatus = (AppCompatTextView) itemView.findViewById(R.id.textViewStatus);
            textViewTime = (AppCompatTextView) itemView.findViewById(R.id.textViewTime);
            textViewAvatar = (AppCompatTextView) itemView.findViewById(R.id.textViewAvatar);
            circleImageViewAvatar = (CircleImageView) itemView.findViewById(R.id.circleImageView);

            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (this.listener != null) {
                listener.OnItemClick(v, getLayoutPosition());
            }
        }
    }

}
