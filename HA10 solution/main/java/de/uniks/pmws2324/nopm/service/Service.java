package de.uniks.pmws2324.nopm.service;

import de.uniks.pmws2324.nopm.model.Topic;
import de.uniks.pmws2324.nopm.model.User;

import java.util.ArrayList;

public class Service {
    private User user;
    ArrayList<Topic> topics = new ArrayList<>();
    public Service() {
        // ToDo load from server
        topics.add(new Topic().setTitle("All"));
        topics.add(new Topic().setTitle("Java"));
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Topic> getTopics() {
        return topics;
    }
}
