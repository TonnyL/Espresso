package io.github.marktony.espresso.packages;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.component.CircleImageView;
import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.interfaze.OnRecyclerViewItemClickListener;
import io.github.marktony.espresso.packages.MainActivity;

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

    private String[] packageStatus;

    public PackageAdapter(@NonNull Context context, @NonNull List<Package> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        packageStatus = context.getResources().getStringArray(R.array.package_status);
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
            pvh.textViewStatus.setText(String.valueOf(packageStatus[state]) + " - " + item.getData().get(0).getContext());
            pvh.textViewTime.setText(item.getData().get(0).getTime());
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
            implements View.OnClickListener, View.OnCreateContextMenuListener {

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
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onClick(View v) {
            if (this.listener != null) {
                listener.OnItemClick(v, getLayoutPosition());
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            if (menu != null) {
                ((MainActivity)context).setSelectedPackageId(list.get(getLayoutPosition()).getNumber());
                Package pack = list.get(getLayoutPosition());
                menu.setHeaderTitle(pack.getName());
                if (pack.isUnread()) {
                    menu.add(Menu.NONE, R.id.action_set_read_unread, 0, R.string.set_read);
                } else {
                    menu.add(Menu.NONE, R.id.action_set_read_unread, 0, R.string.set_unread);
                }
                menu.add(Menu.NONE, R.id.action_copy_code, 0, R.string.copy_code);
                menu.add(Menu.NONE, R.id.action_share, 0, R.string.share);
            }
        }
    }

}
