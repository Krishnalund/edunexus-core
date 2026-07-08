package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class RecommendationEngine {
    public Map<User, InterestGraph> userInterests;
    private Connection dbConnection;
   public  RecommendationEngine(Connection dbConnection) throws SQLException{
        this.dbConnection = dbConnection;
        this.userInterests = new HashMap<>();
    }
    public LinkedList<StudyGroup> recommendGroups(User user) {
        LinkedList<StudyGroup> result = new LinkedList<>();
        findGroupsRecursively(user, result, new LinkedList<>());
        return result;
    }
    private void findGroupsRecursively(User user, LinkedList<StudyGroup> result, LinkedList<User> visited) {
        if (visited.contains(user)) return;
        visited.add(user);

        InterestGraph graph = userInterests.get(user);
        if (graph != null) {
        }

        for (User other : userInterests.keySet()) {
            if (!visited.contains(other)) {
                findGroupsRecursively(other, result, visited);
            }
        }
    }
    public LinkedList<User> recommendBuddies(User user) {
        LinkedList<User> result = new LinkedList<>();
        findBuddiesRecursively(user, result, new LinkedList<>());
        return result;
    }
    private void findBuddiesRecursively(User user, LinkedList<User> result, LinkedList<User> visited) {
        if (visited.contains(user)) return;
        visited.add(user);

        InterestGraph graph = userInterests.get(user);
        if (graph != null) {
            for (User other : userInterests.keySet()) {
                if (!visited.contains(other)) {
                    InterestGraph otherGraph = userInterests.get(other);
                    if (otherGraph != null && graph == otherGraph) {
                        result.add(other);
                    }
                    findBuddiesRecursively(other, result, visited);
                }
            }
        }
    }
}
