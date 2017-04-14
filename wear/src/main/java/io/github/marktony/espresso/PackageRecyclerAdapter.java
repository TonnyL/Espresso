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

package io.github.marktony.espresso;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WearableRecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.github.marktony.espresso.data.Package;

/**
 * Created by lizhaotailang on 2017/3/31.
 */

public class PackageRecyclerAdapter extends WearableRecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Package> mList;

    public PackageRecyclerAdapter(@NonNull List<Package> list) {
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_package, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Package p = mList.get(position);
        ViewHolder vh = (ViewHolder) holder;
        if (p != null) {
            vh.textViewAvatar.setText(p.getName().substring(0, 1));
            vh.textViewName.setText(p.getName());
            if (p.getData() != null && p.getData().size() > 0) {
                vh.textViewStatus.setText(p.getData().get(0).getContext());
            } else {
                vh.textViewStatus.setText("Can not get the latest status.");
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends WearableRecyclerView.ViewHolder {

        private TextView textViewAvatar;
        private TextView textViewName;
        private TextView textViewStatus;
        private CircledImageView avatar;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewAvatar = (TextView) itemView.findViewById(R.id.textViewAvatar);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewStatus = (TextView) itemView.findViewById(R.id.textViewStatus);
            avatar = (CircledImageView) itemView.findViewById(R.id.avatar);
        }
    }

}
