package com.example.sporterz_mobile.models;

public class MatchesDataClass {
    private String dateTime;
    private String stadium;
    private String stadiumCity;
    private String homeTeam;
    private String homeTeamScore;
    private String homeTeamLogo;
    private String awayTeam;
    private String awayTeamScore;
    private String awayTeamLogo;

    public MatchesDataClass(String dateTime, String stadium, String stadiumCity, String homeTeam, String homeTeamScore, String homeTeamLogo, String awayTeam, String awayTeamScore, String awayTeamLogo) {
        this.dateTime = dateTime;
        this.stadium = stadium;
        this.stadiumCity = stadiumCity;
        this.homeTeam = homeTeam;
        this.homeTeamScore = homeTeamScore;
        this.homeTeamLogo = homeTeamLogo;
        this.awayTeam = awayTeam;
        this.awayTeamScore = awayTeamScore;
        this.awayTeamLogo = awayTeamLogo;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getStadium() {
        return stadium;
    }

    public String getStadiumCity() {
        return stadiumCity;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getHomeTeamScore() {
        return homeTeamScore;
    }

    public String getHomeTeamLogo() {
        return homeTeamLogo;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public String getAwayTeamScore() {
        return awayTeamScore;
    }

    public String getAwayTeamLogo() {
        return awayTeamLogo;
    }
}