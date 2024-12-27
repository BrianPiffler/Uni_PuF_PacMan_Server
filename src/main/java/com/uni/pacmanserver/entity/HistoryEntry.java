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
    
    private int gameScore;
    private int difficultyPlayed;
    private long datePlayed;
    private long timePlayed;

    private int userId;

    // GETTER
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGameScore() {
        return gameScore;
    }

    public void setGameScore(int gameScore) {
        this.gameScore = gameScore;
    }

    public int getDifficultyPlayed() {
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
    public void setDifficultyPlayed(int difficultyPlayed) {
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
