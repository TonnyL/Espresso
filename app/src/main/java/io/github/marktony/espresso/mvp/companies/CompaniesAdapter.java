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

package io.github.marktony.espresso.mvp.companies;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.marktony.espresso.R;
import io.github.marktony.espresso.component.FastScrollRecyclerView;
import io.github.marktony.espresso.data.Company;
import io.github.marktony.espresso.interfaze.OnRecyclerViewItemClickListener;

/**
 * Created by lizhaotailang on 2017/3/24.
 */

public class CompaniesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements FastScrollRecyclerView.SectionedAdapter{

    @NonNull
    private final Context context;

    @NonNull
    private final LayoutInflater inflater;

    @NonNull
    private List<Company> list;

    @Nullable
    private OnRecyclerViewItemClickListener listener;

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_WITH_HEADER = 1;

    public CompaniesAdapter(@NonNull Context context, @NonNull List<Company> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {
            return new NormalViewHolder(inflater.inflate(R.layout.item_company, parent, false), listener);
        }
        return new WithHeaderViewHolder(inflater.inflate(R.layout.item_company_with_header, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Company company = list.get(position);
        if (holder instanceof NormalViewHolder) {
            NormalViewHolder cvh = (NormalViewHolder) holder;
            cvh.textViewAvatar.setText(company.getName().substring(0, 1).toUpperCase());
            cvh.textViewCompanyTel.setText(company.getTel());
            cvh.textViewCompanyName.setText(company.getName());
            cvh.avatar.setColorFilter(Color.parseColor(company.getAvatarColor()));
        } else if (holder instanceof WithHeaderViewHolder) {
            WithHeaderViewHolder wh = (WithHeaderViewHolder) holder;
            wh.textViewAvatar.setText(company.getName().substring(0, 1).toUpperCase());
            wh.textViewCompanyTel.setText(company.getTel());
            wh.textViewCompanyName.setText(company.getName());
            wh.stickyHeaderText.setText(getSectionName(position));
            wh.avatar.setColorFilter(Color.parseColor(company.getAvatarColor()));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        if (list.isEmpty()) {
            return "";
        }
        char c = list.get(position).getAlphabet().charAt(0);
        if (Character.isDigit(c)) {
            return "#";
        } else {
            return Character.toString(Character.toUpperCase(c));
        }
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        CircleImageView avatar;
        AppCompatTextView textViewCompanyName;
        AppCompatTextView textViewAvatar;
        AppCompatTextView textViewCompanyTel;

        private OnRecyclerViewItemClickListener listener;

        public NormalViewHolder(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);

            avatar = (CircleImageView) itemView.findViewById(R.id.imageViewAvatar);
            textViewAvatar = (AppCompatTextView) itemView.findViewById(R.id.textViewAvatar);
            textViewCompanyName = (AppCompatTextView) itemView.findViewById(R.id.textViewCompanyName);
            textViewCompanyTel = (AppCompatTextView) itemView.findViewById(R.id.textViewCompanyTel);

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

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || (list.get(position).getAlphabet().charAt(0)
                != list.get(position - 1).getAlphabet().charAt(0))) {
            return TYPE_WITH_HEADER;
        }
        return TYPE_NORMAL;
    }

    public class WithHeaderViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        CircleImageView avatar;
        AppCompatTextView textViewCompanyName;
        AppCompatTextView textViewAvatar;
        AppCompatTextView textViewCompanyTel;
        AppCompatTextView stickyHeaderText;

        private OnRecyclerViewItemClickListener listener;

        public WithHeaderViewHolder(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            avatar = (CircleImageView) itemView.findViewById(R.id.imageViewAvatar);
            textViewAvatar = (AppCompatTextView) itemView.findViewById(R.id.textViewAvatar);
            textViewCompanyName = (AppCompatTextView) itemView.findViewById(R.id.textViewCompanyName);
            textViewCompanyTel = (AppCompatTextView) itemView.findViewById(R.id.textViewCompanyTel);
            stickyHeaderText = (AppCompatTextView) itemView.findViewById(R.id.headerText);

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
