package com.example.weatherapi.entity;

public enum UserRole {
    USER(3, 1, 10),
    ADMIN(10, 5, 10);



    public final Integer bucketCapacity;
    public final Integer refillGreedyTokens;
    public final Integer refillGreedyDurationSeconds;

    UserRole(Integer bucketCapacity, Integer refillGreedyTokens, Integer refillGreedyDurationSeconds) {
        this.bucketCapacity = bucketCapacity;
        this.refillGreedyTokens = refillGreedyTokens;
        this.refillGreedyDurationSeconds = refillGreedyDurationSeconds;
    }
}
