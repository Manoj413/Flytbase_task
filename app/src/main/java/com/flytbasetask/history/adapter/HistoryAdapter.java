package com.flytbasetask.history.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flytbasetask.ResultModel;
import com.flytbasetask.databinding.AdapterHistoryBinding;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>
{
    private Context context;
    private ArrayList<ResultModel> resultModelArrayList = new ArrayList<>();
    AdapterHistoryBinding historyBinding;


    public HistoryAdapter(Context context,ArrayList<ResultModel> resultModelArrayList) {
        this.context = context;
        this.resultModelArrayList = resultModelArrayList;
    }

    public HistoryAdapter() {
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       /* View view = LayoutInflater.from(context).inflate(R.layout.adapter_prediction_category,parent,false);
        return new CategoryAdapter.ViewHolder(view);*/
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        historyBinding = AdapterHistoryBinding.inflate(layoutInflater, parent, false);
        return new HistoryAdapter.ViewHolder(historyBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {

        holder.bind(resultModelArrayList.get(position));

    }


    @Override
    public int getItemCount() {
        return resultModelArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(String.valueOf(resultModelArrayList.get(position)));
    }

    public void setResultData(ArrayList<ResultModel>  mResultArrayList){
        resultModelArrayList = mResultArrayList;
        notifyDataSetChanged();

    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ResultModel resultModel;

        private AdapterHistoryBinding mBinding;
        ViewHolder(AdapterHistoryBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(ResultModel data)
        {
            this.resultModel = data;
            mBinding.txtOperation.setText(resultModel.getInput());
            mBinding.txtOperationResult.setText("Result- "+resultModel.getResult());


        }


        @Override
        public void onClick(View view) {
            switch (view.getId())
            {

            }
        }
    }

}



