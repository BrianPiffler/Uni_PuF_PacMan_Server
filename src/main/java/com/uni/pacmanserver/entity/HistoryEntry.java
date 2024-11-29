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
    private int datePlayed;
    private int timePlayed;

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


    // SETTER
    public void setDifficultyPlayed(int difficultyPlayed) {
        this.difficultyPlayed = difficultyPlayed;
    }

    public int getDatePlayed() {
        return datePlayed;
    }

    public void setDatePlayed(int datePlayed) {
        this.datePlayed = datePlayed;
    }

    public int getTimePlayed() {
        return timePlayed;
    }

    public void setTimePlayed(int timePlayed) {
        this.timePlayed = timePlayed;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
