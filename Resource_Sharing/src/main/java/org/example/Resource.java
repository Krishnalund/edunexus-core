package org.example;

public class Resource {
    private int resourseID;
    private User uploader;
    private StudyGroup group;
    private String title;
    private String filePath;
    private String description;
    private String[] tags;
    Resource(User uploader,StudyGroup group, String title, String filePath, String description){
        this.uploader = uploader;
        this.group = group;
        this.title = title;
        this.filePath = filePath;
        this.description = description;
        this.tags = new String[0];
    }
    void addTags(String tag){
        String newTags[] = new String[tags.length+1];
        for(int i=0; i<tags.length; i++){
            newTags[i] = tags[i];
        }
        newTags[tags.length] = tag;
        tags = newTags;
    }
    public String getFilePath() {
        return filePath;
    }
    public StudyGroup getGroup() {
        return group;
    }
    public String getTitle() {
        return title;
    }

    public User getUploader() {
        return uploader;
    }

    public Object getDescription() {
        return description;
    }
}
