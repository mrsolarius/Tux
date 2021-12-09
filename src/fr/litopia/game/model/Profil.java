/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.litopia.game.model;

import java.util.ArrayList;

/**
 *
 * @author zaettal
 */
public class Profil {
    private String name;
    private String birthdate;
    private String avatar;
    private ArrayList<Partie> parties;

    public Profil(String name, String birthdate, String avatar) {
        this.name = name;
        this.birthdate = birthdate;
        this.avatar = avatar;
        this.parties = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public ArrayList<Partie> getParties() {
        return parties;
    }

    public void setParties(ArrayList<Partie> parties) {
        this.parties = parties;
    }


}
