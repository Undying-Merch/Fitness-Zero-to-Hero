package com.example.fitnesszero_to_hero.Classes;

public class Abonnoment {

    public int abonId;
    public int price;
    public String name;
    public String description;

    public Abonnoment(){}
    public Abonnoment(int Price, String Name, String Desc){
        this.price = Price;
        this.name = Name;
        this.description = Desc;
    }
    public Abonnoment(int id, int Price, String Name, String Desc){
        this.abonId = id;
        this.price = Price;
        this.name = Name;
        this.description = Desc;
    }
}
