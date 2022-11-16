package com.example.fitnesszero_to_hero.Classes;

public class OvelseJunc {

    public String repetition;
    public String modstand;
    public int tid;
    public int kdId;
    public int ovelsesId;
    public String name;

    public OvelseJunc(){}
    public OvelseJunc(String rep, String mod, int ti, String nam){
        this.repetition = rep;
        this.modstand = mod;
        this.tid = ti;
        this.name = nam;
    }
    public OvelseJunc(String rep, String mod, int ti, int kd, int ovelse){
        this.repetition = rep;
        this.modstand = mod;
        this.tid = ti;
        this.kdId = kd;
        this.ovelsesId = ovelse;
    }

}
