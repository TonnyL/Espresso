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

package io.github.marktony.espresso.mvp.packagedetails;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.component.Timeline;
import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.data.PackageStatus;
import io.github.marktony.espresso.mvp.companydetails.CompanyDetailActivity;
import io.realm.RealmList;

/**
 * Created by lizhaotailang on 2017/2/12.
 */

public class PackageDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final Context context;

    @NonNull
    private final LayoutInflater inflater;

    @NonNull
    private final Package aPackage;

    private final List<PackageStatus> list;

    public static final int TYPE_HEADER = 0x00;
    public static final int TYPE_NORMAL = 0x01;
    public static final int TYPE_START = 0x02;
    public static final int TYPE_FINISH = 0x03;
    public static final int TYPE_SINGLE = 0x04;

    public PackageDetailsAdapter(@NonNull Context context, @NonNull Package p) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.aPackage = p;
        // Convert the RealmList to a normal List.
        // Pass a RealmList as parameter directly where List is required
        // is NOT a good idea.
        // The code below will make some terrible bugs.
        // view.showPackageDetails(list);
        // One the them is that the first load works perfectly.
        // But when the screen call onPause and back to user again,
        // the list (RecyclerView) is lost and all the data is removed from DB.
        this.list = new ArrayList<>();
        for (PackageStatus status : p.getData()) {
            list.add(status);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(inflater.inflate(R.layout.item_details_header, parent, false));
        }
        return new PackageStatusViewHolder(inflater.inflate(R.layout.item_package_status, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_HEADER) {
            HeaderViewHolder vh = (HeaderViewHolder) holder;
            vh.textViewCompany.setText(aPackage.getCompanyChineseName());
            vh.textViewName.setText(aPackage.getName());
            vh.textViewNumber.setText(aPackage.getNumber());
        } else {
            PackageStatus item = list.get(position - 1);
            PackageStatusViewHolder viewHolder = (PackageStatusViewHolder) holder;

            if (getItemViewType(position) == TYPE_SINGLE) {
                viewHolder.timeLine.setStartLine(null);
                viewHolder.timeLine.setFinishLine(null);
            } else if (getItemViewType(position) == TYPE_START) {
                viewHolder.timeLine.setStartLine(null);
            } else if (getItemViewType(position) == TYPE_FINISH) {
                viewHolder.timeLine.setFinishLine(null);
            }

            viewHolder.textViewTime.setText(item.getTime());
            viewHolder.textViewLocation.setText(item.getContext());

            String phone = item.getPhone();
            if (phone != null) {
                viewHolder.textViewPhone.setText(phone);
            }
            viewHolder.contactCard.setVisibility(phone != null ? View.VISIBLE : View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        // Including a header.
        return aPackage.getData().size() + 1;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == 1 && position == list.size()) {
            // The list may contains only one item.
            return TYPE_SINGLE;
        } else if (position == 1) {
            return TYPE_START;
        } else if (position == list.size()) {
            return TYPE_FINISH;
        }
        return TYPE_NORMAL;
    }

    /**
     * DO NOT cast a RealmList to List or you will got some unexpected bugs.
     * @param list The RealmList.
     */
    public void updateData(@NonNull RealmList<PackageStatus> list) {
        this.list.clear();
        // See {@link PackageDetailsAdapter#PackageDetailsAdapter}
        for (PackageStatus p : list) {
            this.list.add(p);
        }
        notifyDataSetChanged();
    }

    /**
     * The package status view holder of recyclerView.
     */
    public class PackageStatusViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView textViewLocation;
        private AppCompatTextView textViewTime;
        private Timeline timeLine;
        private CardView contactCard;
        private AppCompatTextView textViewPhone;

        public PackageStatusViewHolder(View itemView) {
            super(itemView);
            textViewLocation = (AppCompatTextView) itemView.findViewById(R.id.textViewStatus);
            textViewTime = (AppCompatTextView) itemView.findViewById(R.id.textViewTime);
            timeLine = (Timeline) itemView.findViewById(R.id.timeLine);
            contactCard = (CardView) itemView.findViewById(R.id.contactCard);
            textViewPhone = (AppCompatTextView) itemView.findViewById(R.id.textViewPhone);

            // Just handle the click event in adapter
            // No need to set an {@link OnRecyclerViewItemClickListener}
            contactCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = textViewPhone.getText().toString();
                    if (uri.length() > 0) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + uri));
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    /**
     * A header view holder of recyclerView.
     */
    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textViewCompany;
        AppCompatTextView textViewNumber;
        AppCompatTextView textViewName;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            textViewCompany = (AppCompatTextView) itemView.findViewById(R.id.textViewCompany);
            textViewNumber = (AppCompatTextView) itemView.findViewById(R.id.textViewPackageNumber);
            textViewName = (AppCompatTextView) itemView.findViewById(R.id.textViewName);

            textViewCompany.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (aPackage.getCompany() != null) {
                        Intent intent = new Intent(context, CompanyDetailActivity.class);
                        intent.putExtra(CompanyDetailActivity.COMPANY_ID, aPackage.getCompany());
                        context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((PackageDetailsActivity)context).toBundle());
                    }
                }
            });
        }

    }
}
