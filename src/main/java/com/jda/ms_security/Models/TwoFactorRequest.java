package com.jda.ms_security.Models;

public class TwoFactorRequest {
    private String email;
    private int twoFactorCode;

    // Constructor
    public TwoFactorRequest(String email, int twoFactorCode) {
        this.email = email;
        this.twoFactorCode = twoFactorCode;
    }

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTwoFactorCode() {
        return twoFactorCode;
    }

    public void setTwoFactorCode(int twoFactorCode) {
        this.twoFactorCode = twoFactorCode;
    }
}