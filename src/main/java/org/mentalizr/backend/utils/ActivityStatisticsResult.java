package org.mentalizr.backend.utils;

import org.mentalizr.serviceObjects.userManagement.ActivityRecordSO;
import org.mentalizr.serviceObjects.userManagement.ActivityRecordCollectionSO;
import org.mentalizr.serviceObjects.userManagement.ProgramStatisticSO;

import java.util.*;
import java.util.stream.Collectors;

public class ActivityStatisticsResult {

    private final String programId;
    private final ActivityRecordCollectionSO messageCollectionSO;
    private final Set<String> activeUsers;
    private final double avgInteractions;
    private final int minInteractions;
    private final int maxInteractions;
    private final Map<String, Integer> userInteractions;

    public ActivityStatisticsResult(String programId, ActivityRecordCollectionSO messageCollectionSO) {
        this.programId = programId;
        this.messageCollectionSO = messageCollectionSO;
        this.activeUsers = calcActiveUsers();
        this.userInteractions = calcUserInteractions();
        this.avgInteractions = calcAvgInteractions();
        this.minInteractions = calcMinInteractions();
        this.maxInteractions = calcMaxInteractions();
    }

    private Set<String> calcActiveUsers() {
        return this.messageCollectionSO.getCollection()
                .stream()
                .map(ActivityRecordSO::getUserId)
                .collect(Collectors.toSet());
    }

    private double calcAvgInteractions() {
        int activeUsers = this.activeUsers.size();

        if (this.messageCollectionSO.getCollection().isEmpty()) {
            return 0;
        } else {
            return (double) this.messageCollectionSO.getCollection().size() / (double) activeUsers;
        }
    }

    private Map<String, Integer> calcUserInteractions() {
        HashMap<String, Integer> userInteractions = new HashMap<>();
        for (String userId: this.activeUsers) {
            int cInteractions = (int) this.messageCollectionSO.getCollection().stream()
                    .filter(activityMessageSO -> Objects.equals(activityMessageSO.getUserId(), userId))
                    .count();
            userInteractions.put(userId, cInteractions);
        }
        return userInteractions;
    }

    private int calcMinInteractions() {
        int minInteractions = Integer.MAX_VALUE;

        if (this.messageCollectionSO.getCollection().isEmpty()) {
            return 0;
        }

        for (String userId: this.activeUsers) {
            int userInteractions = this.userInteractions.get(userId);
            if (userInteractions < minInteractions) {
                minInteractions = userInteractions;
            }
        }
        return minInteractions;
    }

    private int calcMaxInteractions() {
        int maxInteractions = 0;

        for (String userId: this.activeUsers) {
            int userInteractions = this.userInteractions.get(userId);
            if (userInteractions > maxInteractions) {
                maxInteractions = userInteractions;
            }
        }
        return  maxInteractions;
    }

    public ActivityRecordCollectionSO getMessageCollectionSO() {
        return this.messageCollectionSO;
    }

    public ProgramStatisticSO getProgramStatisticSO() {
        return new ProgramStatisticSO(
                this.programId,
                this.activeUsers.size(),
                this.avgInteractions,
                this.minInteractions,
                this.maxInteractions
        );
    }

}
