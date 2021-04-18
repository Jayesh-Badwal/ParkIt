package com.example.parkingsystem;



public class UserHelperClass {
    String fullName, email, phoneNo, password,Address,licenseP,model,rate,openhr,closehr,slotsno, cardNumber, cardExpiryDate, cardCVV;

    public UserHelperClass() {
    }

    public UserHelperClass(String fullName, String email, String phoneNo, String password,String Address,String licenseP,String model) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNo = phoneNo;
        this.password = password;
        this.Address= Address;
        this.model=model;
        this.licenseP=licenseP;
    }

    public UserHelperClass(String fullName, String email, String phoneNo, String password, String address, String rate, String openhr, String closehr, String slotsno, String cardNumber, String cardExpiryDate, String cardCVV) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNo = phoneNo;
        this.password = password;
        Address = address;
        this.rate = rate;
        this.openhr = openhr;
        this.closehr = closehr;
        this.slotsno = slotsno;
        this.cardNumber = cardNumber;
        this.cardExpiryDate = cardExpiryDate;
        this.cardCVV = cardCVV;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardExpiryDate() {
        return cardExpiryDate;
    }

    public void setCardExpiryDate(String cardExpiryDate) {
        this.cardExpiryDate = cardExpiryDate;
    }

    public String getCardCVV() {
        return cardCVV;
    }

    public void setCardCVV(String cardCVV) {
        this.cardCVV = cardCVV;
    }

    public String getAddress(){
        return Address;
    }

    public void setAddress(String Address){
        this.Address=Address;
    }
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getLicenseP(){
        return licenseP;
    }

    public void setLicenseP(String licenseP){
        this.licenseP=licenseP;
    }
    public String getModel(){
        return model;
    }

    public void setModel(String model){
        this.model=model;
    }

    public String getRate(){
        return rate;
    }
    public void setRate(String rate){
        this.rate=rate;
    }
    public String getOpenhr(){
        return openhr;
    }
    public void setOpenhr(String openhr){
        this.openhr=openhr;
    }
    public String getClosehr(){
        return closehr;
    }

    public void setClosehr(String closehr){
        this.closehr=closehr;
    }
    public String getSlotsno(){
        return slotsno;
    }

    public void setSlotsno(String slotsno){
        this.slotsno=slotsno;
    }
}
