package com.dasoniapp.musicmate;
import java.io.Serializable;

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
        this.model = "";
        this.admin = false;
        this.accountStatus = true;
        this.dateSignUp = "";
        this.dateAccess = "";
        this.timeFirstAccess = "";
        this.timeExit = "";
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

    // Implement serverTimestamp() within MainActivity
    public void setDateAccess(String dateAccess) { this.dateAccess = dateAccess; }
    // Implement serverTimestamp() within MainActivity
    public void setTimeFirstAccess(String timeFirstAccess) { this.timeFirstAccess = timeFirstAccess; }
    // Implement serverTimestamp() within MainActivity, override onStop()
    public void setTimeExit(String timeExit) { this.timeExit = timeExit; }


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
