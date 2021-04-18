package com.example.parkingsystem;

import java.io.Serializable;

public class Car implements Serializable {

    String model;
    String regNo;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }
}
