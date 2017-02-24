package io.github.marktony.espresso.packages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.marktony.espresso.R;

/**
 * Created by lizhaotailang on 2017/2/20.
 */

public class PackagesListFragment extends Fragment
        implements PackagesContract.View{

    public static final String TYPE = "ARG_TYPE";

    public static final int TYPE_ALL = 0, TYPE_ON_THE_WAY = 1, TYPE_DELIVERED = 2;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    private PackagesContract.Presenter presenter;

    public PackagesListFragment() {}

    public static PackagesListFragment newInstance(int type) {
        PackagesListFragment fragment = new PackagesListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        initViews(view);

        return view;
    }

    @Override
    public void initViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }

    @Override
    public void setPresenter(PackagesContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
