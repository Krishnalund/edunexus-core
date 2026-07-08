package org.example;
import org.example.User;
import org.example.StudyGroup;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;

public class LeaderboardManager {
    private Contributor[] contributors;
    private Connection dbConnection;
    public LeaderboardManager(Connection dbConnection) throws SQLException {
        this.dbConnection = dbConnection;
        this.contributors = new Contributor[0];
    }
    public void updateContribution(User user, int points) throws SQLException{
        boolean found = false;
        for(int i=0; i<contributors.length; i++){
            if(contributors[i] != null && contributors[i].getUser()== user){
                contributors[i].addPoints(points);
                found = true;
                break;
            }
        }
        if(!found){
            Contributor newContributors[] = new Contributor[contributors.length+1];
            for(int i=0; i<contributors.length; i++){
                newContributors[i] = contributors[i];
            }
            newContributors[contributors.length] = new Contributor(user, points);
            contributors = newContributors;
        }
    }
    public Contributor[] getLeaderboard(StudyGroup group, int topN){
        Contributor[] sorted = Arrays.copyOf(contributors, contributors.length);

        Arrays.sort(sorted, new Comparator<Contributor>() {
            public int compare(Contributor c1, Contributor c2){
                return Integer.compare(c1.getPoints(), c2.getPoints());
            }
        });
        if (topN > sorted.length) {
            topN = sorted.length;
        }

        Contributor[] top = new Contributor[topN];
        for (int i = 0; i < topN; i++) {
            top[i] = sorted[i];
        }

        return top;
    }

}
