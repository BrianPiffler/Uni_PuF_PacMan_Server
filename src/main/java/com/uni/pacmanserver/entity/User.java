package com.uni.pacmanserver.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_user") // User ist Schlüsselwort in H2 daher Tabellennamen ändern
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true,nullable = false)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @OneToMany
    @JoinColumn(name = "userId")
    private Set<HistoryEntry> historyEntries;

    // GETTER
    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public Set<HistoryEntry> getHistoryEntries() {
        return historyEntries;
    }

    // SETTER

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHistoryEntries(Set<HistoryEntry> historyEntries) {
        this.historyEntries = historyEntries;
    }


    
}
