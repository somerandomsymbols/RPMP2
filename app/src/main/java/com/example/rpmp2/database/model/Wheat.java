package com.example.rpmp2.database.model;

public class Wheat
{
    private int id;
    private int year;
    private int production;
    private int price;

    public Wheat(int year, int production, int price)
    {
        this.year = year;
        this.production = production;
        this.price = price;
    }

    public Wheat(int year, int production)
    {
        this.year = year;
        this.production = production;
        this.price = -1;
    }

    public Wheat(int id, int year, int production, int price)
    {
        this.id = id;
        this.year = year;
        this.production = production;
        this.price = price;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    public int getProduction()
    {
        return production;
    }

    public void setProduction(int production)
    {
        this.production = production;
    }

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }
}
