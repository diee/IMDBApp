package com.example.dalarcon.imdbapp.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dalarcon.imdbapp.R;
import com.example.dalarcon.imdbapp.adapter.CastAdapter;
import com.example.dalarcon.imdbapp.model.credits.Cast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreditFragment extends Fragment {

    public static final String CAST_LIST = "cast_list";

    @BindView(R.id.rv_credits)
    RecyclerView mRvCredits;

    private CastAdapter mCastAdapter;

    LinearLayoutManager mLinearLayoutManager;

    List<Cast> mCastList;

    public CreditFragment() {
    }

    public static CreditFragment newInstance(ArrayList<Cast> castList) {
        CreditFragment fragment = new CreditFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(CAST_LIST, castList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCastList = getArguments().getParcelableArrayList(CAST_LIST);
        }
        mCastAdapter = new CastAdapter(getActivity());
        mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_credit, container, false);
        ButterKnife.bind(this, view);

        mRvCredits.setAdapter(mCastAdapter);
        mRvCredits.setHasFixedSize(true);
        mRvCredits.setLayoutManager(mLinearLayoutManager);

        mCastAdapter.addAll(mCastList);

        return view;
    }
}
