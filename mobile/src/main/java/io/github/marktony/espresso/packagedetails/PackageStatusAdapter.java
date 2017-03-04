package io.github.marktony.espresso.packagedetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.github.marktony.espresso.R;
import io.github.marktony.espresso.component.TimelineStep;
import io.github.marktony.espresso.data.PackageStatus;

/**
 * Created by lizhaotailang on 2017/2/12.
 */

public class PackageStatusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final Context context;

    private final LayoutInflater inflater;
    private List<PackageStatus> list;

    public static final int TYPE_NORMAL = 0x00;
    public static final int TYPE_START = 0x01;
    public static final int TYPE_FINISH = 0x02;
    public static final int TYPE_SINGLE = 0x03;

    public PackageStatusAdapter(@NonNull Context context, List<PackageStatus> list) {
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

        PackageStatus item = list.get(position);
        if (getItemViewType(position) == TYPE_SINGLE) {
            ((PackageStatusViewHolder)holder).timeLine.setStartLine(null);
            ((PackageStatusViewHolder)holder).timeLine.setFinishLine(null);
        } else if (getItemViewType(position) == TYPE_START) {
            ((PackageStatusViewHolder)holder).timeLine.setStartLine(null);
        } else if (getItemViewType(position) == TYPE_FINISH) {
            ((PackageStatusViewHolder)holder).timeLine.setFinishLine(null);
        }

        ((PackageStatusViewHolder)holder).textViewTime.setText(item.getTime());
        ((PackageStatusViewHolder)holder).textViewLocation.setText(item.getContext());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && position == list.size() - 1) {
            return TYPE_SINGLE;
        } else if (position == 0) {
            return TYPE_START;
        } else if (position == list.size() - 1) {
            return TYPE_FINISH;
        }
        return TYPE_NORMAL;
    }

    public class PackageStatusViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView textViewLocation;
        private AppCompatTextView textViewTime;
        private TimelineStep timeLine;

        public PackageStatusViewHolder(View itemView) {
            super(itemView);
            textViewLocation = (AppCompatTextView) itemView.findViewById(R.id.textViewLocation);
            textViewTime = (AppCompatTextView) itemView.findViewById(R.id.textViewTime);
            timeLine = (TimelineStep) itemView.findViewById(R.id.timeLine);
        }
    }

}
