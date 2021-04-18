package com.example.parkingsystem;

import java.io.Serializable;

public class Booking implements Serializable {
    private String user;
    private String garage;
    private Car car;
    private int startTime;
    private int endTime;
    private int hours;
    private int slotNo;
    private String bookingID;
    private String source, destination, currentlyAt;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCurrentlyAt() {
        return currentlyAt;
    }

    public void setCurrentlyAt(String currentlyAt) {
        this.currentlyAt = currentlyAt;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getGarage() {
        return garage;
    }

    public void setGarage(String garage) {
        this.garage = garage;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car=car;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(int slotNo) {
        this.slotNo = slotNo;
    }
}
