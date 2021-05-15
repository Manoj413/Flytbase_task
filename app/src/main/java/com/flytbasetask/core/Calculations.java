package com.flytbasetask.core;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.flytbasetask.Holder;
import com.flytbasetask.ResultModel;

import java.util.ArrayList;

public class Calculations {

    final private ArrayList<String> binaryOperators = new ArrayList<>();
    ArrayList<String> calculator_input = new ArrayList<>();

    private Context ctx;

    public ArrayList<String> numbers = new ArrayList<>();
    public String answer,store_input;
    StringBuilder stringBuilder;

    public boolean isExp = false;

    public String currentStatus = "null";

    public String getStore_input() {
        return store_input;
    }



    public void setStore_input(String store_input) {
        if(this.store_input!=null){
            this.store_input.concat(store_input);
        }else {
            this.store_input = store_input;
        }

        if(this.stringBuilder!=null){
            this.stringBuilder.append(store_input);
        }
    }

    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }


    public Calculations(Context ct){
        ctx = ct;
        this.stringBuilder= new StringBuilder();

        binaryOperators.add("*");
        binaryOperators.add("/");
        binaryOperators.add("+");
        binaryOperators.add("-");

    }

    public boolean clear(){
        try {
            numbers.clear();
            answer = "0";
            calculate(numbers);
        } catch (Exception e){
            calculate(numbers);
            return false;
        }
        currentStatus = "null";
        return true;
    }

    public boolean bs(){
        if (currentStatus.equals("num")){
            try {
                numbers.set(numbers.size()-1, formatNumber(numbers.get(numbers.size()-1).substring(0, numbers.get(numbers.size()-1).length()-1)));
            } catch (Exception e){
                numbers.set(numbers.size()-1, "0");
                return false;
            }
        }
        calculate(numbers);
        return true;
    }

    public boolean numberClicked(String number){
        switch (currentStatus) {
            case "null":
                numbers.add(number);
                break;
            case "num":
                numbers.set(numbers.size() - 1, formatNumber(numbers.get(numbers.size() - 1) + number));
                break;
            case "const":
                numbers.add("*");
                numbers.add(number);
                break;
            case "unary":
                numbers.add("*");
                numbers.add(number);
                break;
            case "binary":
                numbers.add(number);
                break;
            case "po":
                numbers.add(number);
                break;
            case "pc":
                numbers.add("*");
                numbers.add(number);
                break;
            default :
                Toast.makeText(ctx, "Error while numberClicked(), currentStatus = " + currentStatus , Toast.LENGTH_SHORT).show();
                break;
        }
        currentStatus = "num";
        calculate(numbers);
        return true;
    }

    public boolean decimalClicked(){
        if (currentStatus.equals("num")){
            if (numbers.get(numbers.size()-1).contains(".")){
                return false;
            } else {
                numbers.set(numbers.size()-1, numbers.get(numbers.size()-1) + ".");
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean operatorClicked(String operator) {
        try {
            if (operator.equals("±")){
                numbers.set(numbers.size()-1, formatNumber(String.valueOf((Double.parseDouble(numbers.get(numbers.size()-1)) * -1))));
                calculate(numbers);
                return true;
            }
        } catch (Exception e){
            calculate(numbers);
            return false;
        }

       if (binaryOperators.contains(operator)){
            switch (currentStatus){
                case "null":
                    numbers.add("0");
                    numbers.add(operator);
                    currentStatus = "binary";
                    break;
                case "num":
                    numbers.add(operator);
                    currentStatus = "binary";
                    break;
                case "const":
                    numbers.add(operator);
                    currentStatus = "binary";
                    break;
                case "unary":
                    numbers.add(operator);
                    currentStatus = "binary";
                    break;
                case "binary":
                    numbers.set(numbers.size()-1, operator);
                    currentStatus = "binary";
                    break;
                case "po":
                    break;
                case "pc":
                    numbers.add(operator);
                    currentStatus = "binary";
                    break;
                default :
                    Toast.makeText(ctx, "Error while binary, currentStatus = " + currentStatus , Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        return false;
    }



    private boolean canCloseParent(){
        int bracketOffset = 0;
        for (String number : numbers){
            if (number.equals("(")){
                bracketOffset++;
            }
            if (number.equals(")")){
                bracketOffset--;
            }
        }
        return (bracketOffset > 0);
    }


    public String getCurrentNo(){
        if (!(currentStatus.equals("num"))){
            return "0";
        } else {
            return numbers.get(numbers.size()-1);
        }
    }

    public boolean evaluateAnswer(){
        if (numbers.contains("(")){
            if (canCloseParent()){
                int bracketOffset = 0;
                for (String number : numbers){
                    if (number.equals("(")){
                        bracketOffset++;
                    }
                    if (number.equals(")")){
                        bracketOffset--;
                    }
                }
                for (int i = 0 ; i < bracketOffset ; i++){
                    numbers.add(")");
                }
            }
        }

        answer = doBODMAS(numbers);

        calculate(numbers);
        return true;
    }

    public String calculate(ArrayList<String> numbers){
        String num = "";

        for (String number : numbers){
            num += number + " ";
        }

        return num;
    }

    @NonNull
    private String doBODMAS(ArrayList<String> numbers)
    {

        while (numbers.size() != 1){
            calculate(numbers);
            if (numbers.contains("(")){
                int bracketOffset = 0;
                for (int i = numbers.size()-1 ; i >= 0 ; i--){
                }
            } else {

                if (contains(binaryOperators, numbers)){
                    for (int l=0;l<numbers.size();l++){
                        setStore_input(numbers.get(l));
                    }
                    for (int i = 0 ; i < binaryOperators.size() ; i++){
                        if (numbers.contains(binaryOperators.get(i))){
                            for (int j = 0 ; j < numbers.size() ; j++){
                                if (numbers.get(j).equals(binaryOperators.get(i))){
                                    String ans = (evaluateBinary(numbers.get(j-1), numbers.get(j), numbers.get(j+1)));
                                    numbers.remove(j-1);
                                    numbers.remove(j-1);
                                    numbers.set(j-1, ans);
                                    i = 0;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        currentStatus = "num";
                ArrayList<ResultModel> resultModelArrayList = new ArrayList<>();
        ResultModel resultModel;



        resultModel = new ResultModel(getStringBuilder().toString(),numbers.get(0));
        resultModelArrayList.add(resultModel);
        stringBuilder.setLength(0);
        Holder.getInstance().addItemToArray(resultModel);
        return formatNumber(numbers.get(0));
    }

    private boolean contains(ArrayList<String> source, ArrayList<String> target){
        for (String val : target){
            if (source.contains(val)){
                return true;
            }
        }
        return false;
    }


    private String formatNumber(String number){
        if (number == null || number.equals("") || number.isEmpty()){
            return "0";
        }
        try {
            if (Double.parseDouble(number) == Long.parseLong(number.split("\\.")[0])){
                return String.valueOf(Long.parseLong(number.split("\\.")[0]));
            } else {
                return String.valueOf(Double.parseDouble(number));
            }
        } catch (NumberFormatException nfe){
            try {
                return String.valueOf(Double.parseDouble(number));
            } catch (NumberFormatException nfe2){
                try {
                    number = number.substring(0, number.length()-1);
                    return String.valueOf(Double.parseDouble(number));
                } catch (Exception e){
                    return number;
                }
            }
        }
    }



    private String evaluateBinary(String number1, String operation, String number2){
        String answer = "error";
        switch (operation){
            case "/":
                answer = division(number1, number2);
                break;
            case "*":
                answer = multiplication(number1, number2);
                break;
            case "+":
                answer = addition(number1, number2);
                break;
            case "-":
                answer = subtraction(number1, number2);
                break;
            default:
                Toast.makeText(ctx, "error in evaluating Binary operation : " + operation, Toast.LENGTH_SHORT).show();
                break;
        }

        try {
            return formatNumber(answer);
        } catch (Exception e){
            return answer;
        }
    }


    @NonNull
    private String division(String num1, String num2){
        return String.valueOf((Double.parseDouble(num1) / Double.parseDouble(num2)));
    }

    @NonNull
    private String multiplication(String num1, String num2){
        return String.valueOf((Double.parseDouble(num1) * Double.parseDouble(num2)));
    }

    @NonNull
    private String addition(String num1, String num2){
        return String.valueOf((Double.parseDouble(num1) + Double.parseDouble(num2)));
    }

    @NonNull
    private String subtraction(String num1, String num2){
        return String.valueOf((Double.parseDouble(num1) - Double.parseDouble(num2)));
    }
}