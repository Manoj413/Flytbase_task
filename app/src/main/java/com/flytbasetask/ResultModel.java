package com.flytbasetask;

import java.io.Serializable;

public class ResultModel implements Serializable {
    String input, result;

    public ResultModel(String input, String result) {
        this.input = input;
        this.result = result;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ResultModel{" +
                "input='" + input + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
