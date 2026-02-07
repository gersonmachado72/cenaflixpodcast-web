package com.cenaflixpodcast.model.dto;

import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public class LoginResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String username;
    private Collection<? extends GrantedAuthority> authorities;
    
    public LoginResponse() {}
    
    public LoginResponse(String accessToken, String tokenType, 
                        String username, Collection<? extends GrantedAuthority> authorities) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.username = username;
        this.authorities = authorities;
    }
    
    // Getters e Setters
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    
    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) { 
        this.authorities = authorities; 
    }
}
