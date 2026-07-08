package org.example;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;

public class ResourceManager {
    private LinkedList<Resource> resources;
    private Connection dbConnection;
    public ResourceManager(Connection dbConnection) throws SQLException {
        this.resources = new LinkedList<>();
        this.dbConnection = dbConnection;
    }
    public Resource uploadResource(StudyGroup group, User user, String title ,String filePath, String description) throws IOException, SQLException {
        Resource resource = new Resource(user,group , title, filePath, description);
        resources.add(resource);
        return resource;
    }
    public LinkedList<Resource> viewResources(StudyGroup group) {
        LinkedList<Resource> groupResources = new LinkedList<>();
        for (Resource resource : resources) {
            groupResources.add(resource);
        }
        return groupResources;
    }
    public String downloadResource(Resource resource) throws IOException {
        return resource != null ? resource.getFilePath() : null;
    }
    LinkedList<Resource> getAllResources(){
        return resources;
    }

}
