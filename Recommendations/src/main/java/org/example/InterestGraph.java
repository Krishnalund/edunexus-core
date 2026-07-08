package org.example;

import java.util.LinkedList;

public class InterestGraph {
    private String interestName;
    private int interestID;
    private LinkedList<InterestGraph> connectedInterests;
    public InterestGraph(int interestID, String interestName){
        this.interestID = interestID;
        this.interestName = interestName;
        this.connectedInterests = new LinkedList<>();
    }
    void addConnections(InterestGraph other){
        connectedInterests.add(other);
    }
    public LinkedList<InterestGraph> findSimilarInterests(String query, int depth){
        LinkedList<InterestGraph> result = new LinkedList<>();
        if (depth <= 0) {
            return result;
        }
        for (InterestGraph interest : connectedInterests) {
            if (interest.interestName.equalsIgnoreCase(query)) {
                result.add(interest);
            }
            result.addAll(interest.findSimilarInterests(query, depth - 1));
        }
        return result;
    }
    public int getInterestID(){
        return interestID;
    }
    public String getInterestName(){
        return interestName;
    }
    public void setInterestID(int id){
        this.interestID = id;
    }
}

