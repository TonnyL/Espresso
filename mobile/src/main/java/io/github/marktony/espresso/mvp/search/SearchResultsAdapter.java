package io.github.marktony.espresso.mvp.search;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.data.Company;
import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.interfaze.OnRecyclerViewItemClickListener;

/**
 * Created by lizhaotailang on 2017/3/26.
 */

public class SearchResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final Context context;

    @NonNull
    private LayoutInflater inflater;

    private List<Package> packages;
    private List<Company> companies;
    private List<ItemWrapper> list;

    private String[] packageStatus;

    private OnRecyclerViewItemClickListener listener;

    public SearchResultsAdapter(@NonNull Context context,
                                @NonNull List<Package> packages,
                                @NonNull List<Company> companies) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.packages = packages;
        this.companies = companies;
        packageStatus = context.getResources().getStringArray(R.array.package_status);
        this.list = new ArrayList<>();
        this.list.add(new ItemWrapper(ItemWrapper.TYPE_CATEGORY));
        if (packages.size() > 0) {
            for (int i = 0; i < packages.size(); i++) {
                ItemWrapper wrapper = new ItemWrapper(ItemWrapper.TYPE_PACKAGE);
                wrapper.index = i;
                list.add(wrapper);
            }
        } else {
            list.add(new ItemWrapper(ItemWrapper.TYPE_EMPTY));
        }

        this.list.add(new ItemWrapper(ItemWrapper.TYPE_CATEGORY));
        if (companies.size() > 0) {
            for (int i = 0; i < companies.size(); i++) {
                ItemWrapper wrapper = new ItemWrapper(ItemWrapper.TYPE_COMPANY);
                wrapper.index = i;
                list.add(wrapper);
            }
        } else {
            list.add(new ItemWrapper(ItemWrapper.TYPE_EMPTY));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case ItemWrapper.TYPE_EMPTY:
                viewHolder = new EmptyHolder(inflater.inflate(R.layout.item_search_result_empty, parent, false));
                break;

            case ItemWrapper.TYPE_CATEGORY:
                viewHolder = new ResultCategoryHolder(inflater.inflate(R.layout.item_search_result_category, parent, false));
                break;
            
            case ItemWrapper.TYPE_PACKAGE:
                viewHolder = new PackageHolder(inflater.inflate(R.layout.item_package, parent, false), listener);
                break;

            case ItemWrapper.TYPE_COMPANY:
                viewHolder = new CompanyHolder(inflater.inflate(R.layout.item_company, parent, false), listener);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemWrapper iw = list.get(position);
        switch (iw.viewType) {
            case ItemWrapper.TYPE_EMPTY:
                EmptyHolder emptyHolder = (EmptyHolder) holder;
                emptyHolder.textView.setText(
                        (position == 1 ? packages == null : companies == null) ?
                        R.string.item_loading :
                        R.string.no_result);
                break;

            case ItemWrapper.TYPE_CATEGORY:
                ResultCategoryHolder categoryHolder = (ResultCategoryHolder) holder;
                categoryHolder.textView.setText(position > 0 ? R.string.search_label_company : R.string.search_label_package);
                break;

            case ItemWrapper.TYPE_PACKAGE:
                PackageHolder packageHolder = (PackageHolder) holder;
                Package pkg = packages.get(iw.index);
                if (pkg.getData() != null && pkg.getData().size() > 0) {
                    int state = Integer.parseInt(pkg.getState());
                    packageHolder.textViewStatus.setText(String.valueOf(packageStatus[state]) + " - " + pkg.getData().get(0).getContext());
                    packageHolder.textViewTime.setText(pkg.getData().get(0).getTime());
                } else {
                    packageHolder.textViewTime.setText("");
                    packageHolder.textViewStatus.setText(R.string.get_status_error);
                }

                if (pkg.isReadable()) {
                    packageHolder.textViewPackageName.setTypeface(null, Typeface.BOLD);
                    packageHolder.textViewTime.setTypeface(null, Typeface.BOLD);
                    packageHolder.textViewStatus.setTypeface(null, Typeface.BOLD);
                } else {
                    packageHolder.textViewPackageName.setTypeface(null, Typeface.NORMAL);
                    packageHolder.textViewTime.setTypeface(null, Typeface.NORMAL);
                    packageHolder.textViewStatus.setTypeface(null, Typeface.NORMAL);
                }

                packageHolder.textViewPackageName.setText(pkg.getName());
                packageHolder.textViewAvatar.setText(pkg.getName().substring(0,1));
                packageHolder.avatar.setImageResource(pkg.getColorAvatar());

                break;

            case ItemWrapper.TYPE_COMPANY:
                Company company = companies.get(iw.index);
                CompanyHolder companyHolder = (CompanyHolder) holder;
                companyHolder.textViewAvatar.setText(company.getName().substring(0, 1).toUpperCase());
                companyHolder.textViewCompanyTel.setText(company.getTel());
                companyHolder.textViewCompanyName.setText(company.getName());
                companyHolder.avatar.setColorFilter(Color.parseColor(company.getAvatarColor()));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).viewType;
    }

    public void updateData(List<Package> packages, List<Company> companies) {
        this.packages.clear();
        this.companies.clear();
        this.list.clear();
        this.list.add(new ItemWrapper(ItemWrapper.TYPE_CATEGORY));
        if (packages.size() > 0) {
            for (int i = 0; i < packages.size(); i++) {
                ItemWrapper wrapper = new ItemWrapper(ItemWrapper.TYPE_PACKAGE);
                wrapper.index = i;
                list.add(wrapper);
                this.packages.add(packages.get(i));
            }
        } else {
            list.add(new ItemWrapper(ItemWrapper.TYPE_EMPTY));
        }

        this.list.add(new ItemWrapper(ItemWrapper.TYPE_CATEGORY));
        if (companies.size() > 0) {
            for (int i = 0; i < companies.size(); i++) {
                ItemWrapper wrapper = new ItemWrapper(ItemWrapper.TYPE_COMPANY);
                wrapper.index = i;
                list.add(wrapper);
                this.companies.add(companies.get(i));
            }
        } else {
            list.add(new ItemWrapper(ItemWrapper.TYPE_EMPTY));
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public int getOriginalIndex(int position) {
        return list.get(position).index;
    }

    private class PackageHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        CircleImageView avatar;
        AppCompatTextView textViewTime, textViewStatus;
        AppCompatTextView textViewPackageName, textViewAvatar;

        private OnRecyclerViewItemClickListener listener;

        PackageHolder(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            avatar = (CircleImageView) itemView.findViewById(R.id.circleImageView);
            textViewPackageName = (AppCompatTextView) itemView.findViewById(R.id.textViewPackageName);
            textViewStatus = (AppCompatTextView) itemView.findViewById(R.id.textViewStatus);
            textViewTime = (AppCompatTextView) itemView.findViewById(R.id.textViewTime);
            textViewAvatar = (AppCompatTextView) itemView.findViewById(R.id.textViewAvatar);
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

    private class ResultCategoryHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textView;

        ResultCategoryHolder(View itemView) {
            super(itemView);
            textView = (AppCompatTextView) itemView.findViewById(R.id.tv_title);
        }

    }

    private class EmptyHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textView;

        EmptyHolder(View itemView) {
            super(itemView);
            textView = (AppCompatTextView) itemView.findViewById(R.id.tv_title);
        }

    }

    private class CompanyHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        CircleImageView avatar;
        AppCompatTextView textViewCompanyName, textViewAvatar, textViewCompanyTel;

        private OnRecyclerViewItemClickListener listener;

        CompanyHolder(View itemView, OnRecyclerViewItemClickListener listener) {
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
            if (listener != null) {
                listener.OnItemClick(v, getLayoutPosition());
            }
        }
    }

    public static class ItemWrapper {

        public final static int TYPE_PACKAGE = 0, TYPE_COMPANY = 1, TYPE_CATEGORY = 2, TYPE_EMPTY = 3;

        public int viewType;

        // Optional
        public int index;

        public ItemWrapper(int viewType) {
            this.viewType = viewType;
        }

    }

}
