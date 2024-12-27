package com.uni.pacmanserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class HistoryEntry {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private long gameScore;
    private String difficultyPlayed;
    private long datePlayed;
    private long timePlayed;

    private int userId;

    // GETTER
    public int getId() {
        return id;
    }

    public long getGameScore() {
        return gameScore;
    }

    public String getDifficultyPlayed() {
        return difficultyPlayed;
    }

    public int getUserId() {
        return userId;
    }

    public long getTimePlayed() {
        return timePlayed;
    }

    public long getDatePlayed() {
        return datePlayed;
    }

    // SETTER

    public void setId(int id) {
        this.id = id;
    }

    public void setGameScore(long gameScore) {
        this.gameScore = gameScore;
    }

    public void setDifficultyPlayed(String difficultyPlayed) {
        this.difficultyPlayed = difficultyPlayed;
    }

    public void setDatePlayed(long datePlayed) {
        this.datePlayed = datePlayed;
    }

    public void setTimePlayed(long timePlayed) {
        this.timePlayed = timePlayed;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
