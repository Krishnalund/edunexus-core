package org.example;
import java.util.LinkedList;

public class Tag {
    	private String tagName;
    	private LinkedList<Tag> subTags;
        public Tag(String tagName){
            this.tagName=tagName;
            this.subTags=new LinkedList<>();
        }

    public void addSubTag(Tag subTag){
            subTags.add(subTag);
        }
        public LinkedList<Tag> findSimilarTags(String query){
            LinkedList<Tag> result = new LinkedList<>();
            if (tagName.toLowerCase().contains(query.toLowerCase())) {
                result.add(this);
            }
            for (Tag subTag : subTags) {
                result.addAll(subTag.findSimilarTags(query)); // recursion + LinkedList traversal
            }
            return result;
        }
         public String getTagName() {
            return tagName;
        }
}
