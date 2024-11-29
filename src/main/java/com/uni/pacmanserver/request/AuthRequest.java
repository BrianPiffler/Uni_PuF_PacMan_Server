package com.uni.pacmanserver.request;

public class AuthRequest {
    private String username;
    private String password;

    // GETTER
    public String getUsername() {
        return username;
    }
   
    public String getPassword() {
        return password;
    }

    // SETTER
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    
}
