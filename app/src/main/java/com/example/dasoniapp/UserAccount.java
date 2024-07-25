package com.example.dasoniapp;
import android.os.Build;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserAccount implements Serializable {
    private String email, phone, name, password, idToken;
    private int bestRhythmScore;
    private int bestNoteScore;

    private String model;
    private boolean admin, accountStatus;
    private String dateSignUp, dateAccess, timeFirstAccess, timeExit;

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
        this.model = Build.MODEL;
        this.admin = false;
        this.accountStatus = true;
        this.dateSignUp = getCurrentDate();
        this.dateAccess = "";
        this.timeFirstAccess = "";
        this.timeExit = "";
    }

    // Method to get the current date in the required format
    // Use it for dateSignUp and call for dateAccess
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Method to get the current time in the required format
    // Call for timeFirstAccess timeExit
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
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

    public String getModel() { return model; }
    public boolean isAdmin() { return admin; }
    public boolean isAccountStatus() { return accountStatus; }
    public String getDateSignUp() { return dateSignUp; }
    public String getDateAccess() { return dateAccess; }
    public String getTimeFirstAccess() { return timeFirstAccess; }
    public String getTimeExit() { return timeExit; }

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

    // Build.MODEL, use Library to get market name
    public void setModel(String model) { this.model = model; }
    public void setAdmin(boolean admin) { this.admin = admin; }
    public void setAccountStatus(boolean accountStatus) { this.accountStatus = accountStatus; }

    // Implement serverTimestamp() within RegisterActivity
    public void setDateSignUp(String dateSignUp) { this.dateSignUp = dateSignUp; }

    public void setDateAccess() {
        this.dateAccess = getCurrentDate();
    }

    public void setTimeFirstAccess() {
        this.timeFirstAccess = getCurrentTime();
    }
    public void setTimeExit() {
        this.timeExit = getCurrentTime();
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