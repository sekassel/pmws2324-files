package de.uniks.pmws2324.nopm.model;

import org.fulib.builder.ClassModelDecorator;
import org.fulib.builder.ClassModelManager;
import org.fulib.builder.reflect.Link;

import java.util.List;

public class GenModel implements ClassModelDecorator {

    class Message {
        String body;
        java.time.Instant timestamp;

        @Link("messages")
        Topic topic;

        @Link("messages")
        User sender;
    }

    class Topic {
        String title;

        @Link("topic")
        List<Message> messages;
    }

    class User {
        String nickname;

        @Link("sender")
        List<Message> messages;

    }

    @Override
    public void decorate(ClassModelManager m) {
        m.haveNestedClasses(GenModel.class);
    }
}
