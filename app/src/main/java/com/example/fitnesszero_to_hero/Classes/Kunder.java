package com.example.fitnesszero_to_hero.Classes;

import kotlinx.coroutines.channels.ActorKt;

public class Kunder {

public int kundeID;
public String name;
public String phone;
public String mail;
public boolean active;


Kunder(){}

Kunder(int id, String Name, String Phone, String Mail, boolean Active){
    this.kundeID = id;
    this.name = Name;
    this.phone = Phone;
    this.mail = Mail;
    this.active = Active;
}
}
