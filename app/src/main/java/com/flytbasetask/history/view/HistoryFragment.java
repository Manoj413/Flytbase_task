package com.flytbasetask.history.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flytbasetask.Holder;
import com.flytbasetask.R;
import com.flytbasetask.ResultModel;
import com.flytbasetask.databinding.FragmentHistoryBinding;
import com.flytbasetask.history.adapter.HistoryAdapter;
import com.flytbasetask.history.viewmodel.HistoryViewModel;

import org.java_websocket.client.WebSocketClient;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private HistoryViewModel historyViewModel;
    private FragmentHistoryBinding historyBinding;
    private NavHostFragment navHostFragment;
    private Activity mHostActivity;
    Context mContext;
    private WebSocketClient mWebSocketClient;
    HistoryAdapter historyAdapter;
    ArrayList<ResultModel> resultModelArrayList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext=context;
        mHostActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        historyBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_history, container, false);
        View view = historyBinding.getRoot();

        init();
        if (Holder.getInstance().getItemArray()!=null){
            setHistoryAdapter(Holder.getInstance().getItemArray());
        }

        // connectWebSocket();
        return view;
    }
    private void init() {
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        navHostFragment= (NavHostFragment)getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

       // action =  AqiListFragmentDirections.actionAqiListFragmentToCityWiseAqiFragment();
        attachRecyclerView();


    }
    private void attachRecyclerView() {
        historyAdapter = new HistoryAdapter(getContext(),resultModelArrayList);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        historyBinding.rVHistory.setLayoutManager(layoutManager);
        historyBinding.rVHistory.setAdapter(historyAdapter);
    }
    private void setHistoryAdapter(ArrayList<ResultModel> results) {
        historyAdapter.setResultData(results);

    }

}