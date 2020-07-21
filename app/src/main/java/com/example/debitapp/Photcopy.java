package com.example.debitapp;

public class Photcopy {
    private String name;
    private double total_photocopy;
    private double total_print;
    private double total_tranfer;
    private double price;

    public Photcopy(String name, double total_photocopy, double total_print, double total_tranfer, double price) {
        this.name = name;
        this.total_photocopy = total_photocopy;
        this.total_print = total_print;
        this.total_tranfer = total_tranfer;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotal_photocopy() {
        return total_photocopy;
    }

    public void setTotal_photocopy(double total_photocopy) {
        this.total_photocopy = total_photocopy;
    }

    public double getTotal_print() {
        return total_print;
    }

    public void setTotal_print(double total_print) {
        this.total_print = total_print;
    }

    public double getTotal_tranfer() {
        return total_tranfer;
    }

    public void setTotal_tranfer(double total_tranfer) {
        this.total_tranfer = total_tranfer;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
