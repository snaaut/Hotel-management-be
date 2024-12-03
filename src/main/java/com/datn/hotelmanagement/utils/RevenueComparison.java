package com.datn.hotelmanagement.utils;

public class RevenueComparison {
    private double currentMonthRevenue;
    private double previousMonthRevenue;
    private double percentageChange;

    // Getters and setters
    public double getCurrentMonthRevenue() {
        return currentMonthRevenue;
    }

    public void setCurrentMonthRevenue(double currentMonthRevenue) {
        this.currentMonthRevenue = currentMonthRevenue;
    }

    public double getPreviousMonthRevenue() {
        return previousMonthRevenue;
    }

    public void setPreviousMonthRevenue(double previousMonthRevenue) {
        this.previousMonthRevenue = previousMonthRevenue;
    }

    public double getPercentageChange() {
        return percentageChange;
    }

    public void setPercentageChange(double percentageChange) {
        this.percentageChange = percentageChange;
    }
}

