package org.example;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SearchEngine {
    	private User[] usersCache;
    	private LinkedList<Post> postsCache;
    	private LinkedList<Resource> resourcesCache;
	public SearchEngine(UserManager userMgr, DiscussionManager discMgr, ResourceManager resMgr) {
		// Caches references from other managers
		this.usersCache = userMgr.getAllUsers();
		this.postsCache = discMgr.getAllPosts();
		this.resourcesCache = resMgr.getAllResources();
	}

	// ===== Method 1: Search Users =====
	// Performs a linear search in usersCache based on username or email
	public User[] searchUsers(String query) {
		List<User> matchedUsers = new ArrayList<>();

		for (User u : usersCache) {
			if (u != null && (
					u.getUsername().toLowerCase().contains(query.toLowerCase()) ||
							u.getEmail().toLowerCase().contains(query.toLowerCase())
			)) {
				matchedUsers.add(u);
			}
		}

		return matchedUsers.toArray(new User[0]);
	}

	// ===== Method 2: Search Posts =====
	// Traverses LinkedList of posts for keyword or tag matches
	public LinkedList<Post> searchPosts(String query, StudyGroup group) {
		LinkedList<Post> results = new LinkedList<>();

		for (Post p : postsCache) {
			boolean matchesGroup = (group == null || p.getGroup().equals(group));
			if (matchesGroup && p.getContent().toLowerCase().contains(query.toLowerCase())) {
				results.add(p);
			}
		}

		return results;
	}

	// ===== Method 3: Search Resources =====
	// Traverses LinkedList of resources for keyword or tag matches
	public LinkedList<Resource> searchResources(String query, StudyGroup group) {
		LinkedList<Resource> results = new LinkedList<>();

		for (Resource r : resourcesCache) {
			boolean matchesGroup = (group == null || r.getGroup().equals(group));
			if (matchesGroup && r.getTitle().toLowerCase().contains(query.toLowerCase())) {
				results.add(r);
			}
		}

		return results;
	}

	// ===== Method 4: Advanced Search =====
	// Dispatches search based on type (users, posts, or resources)
	public Object advancedSearch(String query, String type) {
		switch (type.toLowerCase()) {
			case "users":
				return searchUsers(query);
			case "posts":
				return searchPosts(query, null);
			case "resources":
				return searchResources(query, null);
			default:
				throw new IllegalArgumentException("Invalid search type: " + type);
		}
	}
}


