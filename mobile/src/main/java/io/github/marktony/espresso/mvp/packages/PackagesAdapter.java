/*
 *  Copyright(c) 2017 lizhaotailang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.marktony.espresso.mvp.packages;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.marktony.espresso.R;
import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.interfaze.OnRecyclerViewItemClickListener;

/**
 * Created by lizhaotailang on 2017/2/11.
 */

public class PackagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final Context context;

    @NonNull
    private final LayoutInflater inflater;

    @NonNull
    private List<Package> list;

    @Nullable
    private OnRecyclerViewItemClickListener listener;

    private String[] packageStatus;

    public PackagesAdapter(@NonNull Context context, @NonNull List<Package> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        packageStatus = context.getResources().getStringArray(R.array.package_status);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PackageViewHolder(inflater.inflate(R.layout.item_package, parent, false), listener);
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

        if (item.isReadable()) {
            pvh.textViewPackageName.setTypeface(null, Typeface.BOLD);
            pvh.textViewTime.setTypeface(null, Typeface.BOLD);
            pvh.textViewStatus.setTypeface(null, Typeface.BOLD);
        } else {
            pvh.textViewPackageName.setTypeface(null, Typeface.NORMAL);
            pvh.textViewTime.setTypeface(null, Typeface.NORMAL);
            pvh.textViewStatus.setTypeface(null, Typeface.NORMAL);
        }

        pvh.textViewPackageName.setText(item.getName());
        pvh.textViewAvatar.setText(item.getName().substring(0,1));
        pvh.circleImageViewAvatar.setImageResource(item.getColorAvatar());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Update the data. Keep the data is the latest.
     * @param list The data.
     */
    public void updateData(@NonNull List<Package> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * The view holder of package in home list.
     */
    public class PackageViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener {

        AppCompatTextView textViewPackageName;
        AppCompatTextView textViewTime;
        AppCompatTextView textViewStatus;
        AppCompatTextView textViewAvatar;
        AppCompatTextView textViewRemove;
        ImageView imageViewRemove;
        CircleImageView circleImageViewAvatar;
        LinearLayout layoutMain;
        View wrapperView;

        private OnRecyclerViewItemClickListener listener;

        public PackageViewHolder(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            textViewPackageName = (AppCompatTextView) itemView.findViewById(R.id.textViewPackageName);
            textViewStatus = (AppCompatTextView) itemView.findViewById(R.id.textViewStatus);
            textViewTime = (AppCompatTextView) itemView.findViewById(R.id.textViewTime);
            textViewAvatar = (AppCompatTextView) itemView.findViewById(R.id.textViewAvatar);
            textViewRemove = (AppCompatTextView) itemView.findViewById(R.id.textViewRemove);
            imageViewRemove = (ImageView) itemView.findViewById(R.id.imageViewRemove);
            circleImageViewAvatar = (CircleImageView) itemView.findViewById(R.id.circleImageView);
            layoutMain = (LinearLayout) itemView.findViewById(R.id.layoutPackageItemMain);
            wrapperView = itemView.findViewById(R.id.layoutPackageItem);

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

        /**
         * Create the context menu.
         * @param menu The context menu.
         * @param v The view.
         * @param menuInfo The menu information.
         */
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            if (menu != null) {
                ((MainActivity)context).setSelectedPackageId(list.get(getLayoutPosition()).getNumber());
                Package pack = list.get(getLayoutPosition());
                menu.setHeaderTitle(pack.getName());
                // Set different title according to the data({@link Package#readable})
                if (pack.isReadable()) {
                    menu.add(Menu.NONE, R.id.action_set_readable, 0, R.string.set_read);
                } else {
                    menu.add(Menu.NONE, R.id.action_set_readable, 0, R.string.set_unread);
                }
                menu.add(Menu.NONE, R.id.action_copy_code, 0, R.string.copy_code);
                menu.add(Menu.NONE, R.id.action_share, 0, R.string.share);
            }
        }
    }

}
