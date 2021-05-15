package com.flytbasetask.home.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flytbasetask.MainViewModel;
import com.flytbasetask.R;
import com.flytbasetask.core.Calculations;
import com.flytbasetask.databinding.FragmentHomeBinding;
import com.flytbasetask.home.viewmodel.HomeViewModel;

import org.java_websocket.client.WebSocketClient;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment implements View.OnClickListener {

    Calculations calculations;
    FragmentHomeBinding fragmentHomeBinding;
    HomeViewModel homeViewModel;
    private final String[] ANGLES = new String[]{"rad", "deg", "grad"};
    private int angle_index = 0;
    private boolean HYP = false;
    private NavHostFragment navHostFragment;
    private Activity mHostActivity;
    Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentHomeBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_home, container, false);
        View view = fragmentHomeBinding.getRoot();

        init();

        return view;
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext=context;
        mHostActivity = (Activity) context;
    }

    private void init() {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        navHostFragment= (NavHostFragment)getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
       calculations = new Calculations(mHostActivity);
        setOnClickListener();

    }

    private void setOnClickListener() {
        fragmentHomeBinding.btnZero.setOnClickListener(this);
        fragmentHomeBinding.btnOne.setOnClickListener(this);
        fragmentHomeBinding.btnTwo.setOnClickListener(this);
        fragmentHomeBinding.btnThree.setOnClickListener(this);
        fragmentHomeBinding.btnFour.setOnClickListener(this);
        fragmentHomeBinding.btnFive.setOnClickListener(this);
        fragmentHomeBinding.btnSix.setOnClickListener(this);
        fragmentHomeBinding.btnSeven.setOnClickListener(this);
        fragmentHomeBinding.btnEight.setOnClickListener(this);
        fragmentHomeBinding.btnNine.setOnClickListener(this);
        fragmentHomeBinding.btnClear.setOnClickListener(this);
        fragmentHomeBinding.btnDelete.setOnClickListener(this);
        fragmentHomeBinding.btnPlus.setOnClickListener(this);
        fragmentHomeBinding.btnMinus.setOnClickListener(this);
        fragmentHomeBinding.btnMultiply.setOnClickListener(this);
        fragmentHomeBinding.btnDivide.setOnClickListener(this);
        fragmentHomeBinding.btnEqual.setOnClickListener(this);
        fragmentHomeBinding.btnGoToHistory.setOnClickListener(this);
        fragmentHomeBinding.btnDot.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnGoToHistory:
                NavController navController = NavHostFragment.findNavController(HomeFragment.this);
                navController.navigate(R.id.action_homeFragment_to_historyFragment);

                break;
            case R.id.btnClear:
                calculations.clear();
                fragmentHomeBinding.txtMainTitle.setText("0");
                fragmentHomeBinding.txtSubTitle.setText(calculations.calculate(calculations.numbers));
                break;

            case R.id.btnDelete:
                calculations.bs();
                fragmentHomeBinding.txtMainTitle.setText(calculations.getCurrentNo());
                fragmentHomeBinding.txtSubTitle.setText(calculations.calculate(calculations.numbers));
                break;

            case R.id.btnDivide:
                calculations.operatorClicked("/");
                fragmentHomeBinding.txtMainTitle.setText(calculations.getCurrentNo());
                fragmentHomeBinding.txtSubTitle.setText(calculations.calculate(calculations.numbers));
                break;

            case R.id.btnDot:
                calculations.decimalClicked();
                fragmentHomeBinding.txtMainTitle.setText(calculations.getCurrentNo());
                fragmentHomeBinding.txtSubTitle.setText(calculations.calculate(calculations.numbers));
                break;

            case R.id.btnEqual:
                Log.e("=_pressed","=_pressed");
                ArrayList<String> expression = calculations.numbers;
                calculations.evaluateAnswer();
                fragmentHomeBinding.txtMainTitle.setText(calculations.answer);
                fragmentHomeBinding.txtSubTitle.setText("");
                break;

            case R.id.btnMinus:
                calculations.operatorClicked("-");
                fragmentHomeBinding.txtMainTitle.setText(calculations.getCurrentNo());
                fragmentHomeBinding.txtSubTitle.setText(calculations.calculate(calculations.numbers));
                break;

            case R.id.btnMultiply:
                calculations.operatorClicked("*");
                fragmentHomeBinding.txtMainTitle.setText(calculations.getCurrentNo());
                fragmentHomeBinding.txtSubTitle.setText(calculations.calculate(calculations.numbers));
                break;

            case R.id.btnPlus:
                Log.e("+_pressed","+_pressed");
                calculations.operatorClicked("+");
                fragmentHomeBinding.txtMainTitle.setText(calculations.getCurrentNo());
                fragmentHomeBinding.txtSubTitle.setText(calculations.calculate(calculations.numbers));
                break;

            case R.id.btnZero:
                calculations.numberClicked("0");
                fragmentHomeBinding.txtMainTitle.setText(calculations.getCurrentNo());
                fragmentHomeBinding.txtSubTitle.setText(calculations.calculate(calculations.numbers));
                break;

            case R.id.btnOne:
                calculations.numberClicked("1");
                fragmentHomeBinding.txtMainTitle.setText(calculations.getCurrentNo());
                fragmentHomeBinding.txtSubTitle.setText(calculations.calculate(calculations.numbers));
                break;

            case R.id.btnTwo:
                calculations.numberClicked("2");
                fragmentHomeBinding.txtMainTitle.setText(calculations.getCurrentNo());
                fragmentHomeBinding.txtSubTitle.setText(calculations.calculate(calculations.numbers));
                break;

            case R.id.btnThree:
                calculations.numberClicked("3");
                fragmentHomeBinding.txtMainTitle.setText(calculations.getCurrentNo());
                fragmentHomeBinding.txtSubTitle.setText(calculations.calculate(calculations.numbers));
                break;

            case R.id.btnFour:
                Log.e("4_pressed","4_pressed");
                calculations.numberClicked("4");
                fragmentHomeBinding.txtMainTitle.setText(calculations.getCurrentNo());
                fragmentHomeBinding.txtSubTitle.setText(calculations.calculate(calculations.numbers));
                break;

            case R.id.btnFive:
                calculations.numberClicked("5");
                fragmentHomeBinding.txtMainTitle.setText(calculations.getCurrentNo());
                fragmentHomeBinding.txtSubTitle.setText(calculations.calculate(calculations.numbers));
                break;

            case R.id.btnSix:
                Log.e("6_pressed","6_pressed");
                calculations.numberClicked("6");
                fragmentHomeBinding.txtMainTitle.setText(calculations.getCurrentNo());
                fragmentHomeBinding.txtSubTitle.setText(calculations.calculate(calculations.numbers));
                break;

            case R.id.btnSeven:
                calculations.numberClicked("7");
                fragmentHomeBinding.txtMainTitle.setText(calculations.getCurrentNo());
                fragmentHomeBinding.txtSubTitle.setText(calculations.calculate(calculations.numbers));
                break;

            case R.id.btnEight:
                calculations.numberClicked("8");
                fragmentHomeBinding.txtMainTitle.setText(calculations.getCurrentNo());
                fragmentHomeBinding.txtSubTitle.setText(calculations.calculate(calculations.numbers));
                break;

            case R.id.btnNine:
                calculations.numberClicked("9");
                fragmentHomeBinding.txtMainTitle.setText(calculations.getCurrentNo());
                fragmentHomeBinding.txtSubTitle.setText(calculations.calculate(calculations.numbers));
                break;
        }
    }
}