package com.red.conecta2.Models;

import java.util.List;

public class LikesResponse {
    private int totalLikes;
    private List<String> usernames;

    public LikesResponse(int totalLikes, List<String> usernames) {
        this.totalLikes = totalLikes;
        this.usernames = usernames;
    }

    // Getters y Setters
    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }
}
