package com.foodcubes.realtime;

public class Data {
    private String meal;
    private String menu;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    String day;

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public Data(String day, String meal, String menu) {
        this.meal = meal;
        this.menu = menu;
    }
}
