package com.example.dasoniapp;
import java.io.Serializable;

public class UserAccount implements Serializable {
    private String email, phone, name, password, idToken;
    private int bestRhythmScore;
    private int bestNoteScore;

    public UserAccount() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public int getBestRhythmScore() {
        return bestRhythmScore;
    }

    public void setBestRhythmScore(int bestRhythmScore) {
        this.bestRhythmScore = bestRhythmScore;
    }

    public int getBestNoteScore() {
        return bestNoteScore;
    }

    public void setBestNoteScore(int bestNoteScore) {
        this.bestNoteScore = bestNoteScore;
    }

    public UserAccount(String email, String phone, String name, String password)  {
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.password = password;
        this.bestRhythmScore = 0;
        this.bestNoteScore = 0;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void updateBestRhythmScore(int newScore) {
        if (newScore > this.bestRhythmScore) {
            this.bestRhythmScore = newScore;
        }
    }

    public void updateBestNoteScore(int newScore) {
        if (newScore > this.bestNoteScore) {
            this.bestNoteScore = newScore;
        }
    }

}
