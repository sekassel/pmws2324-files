package de.uniks.pmws2324.nopm.service;

import de.uniks.pmws2324.nopm.model.Message;
import de.uniks.pmws2324.nopm.model.Topic;
import de.uniks.pmws2324.nopm.model.User;
import de.uniks.pmws2324.nopm.model.dtos.CreateMessageDto;
import de.uniks.pmws2324.nopm.model.dtos.LoginDto;
import de.uniks.pmws2324.nopm.model.dtos.MessageDto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ChatService {

    private final ChatApiService chatApiService;
    private final Service modelService;

    public ChatService(ChatApiService chatApiService, Service service) {
        this.modelService = service;
        this.chatApiService = chatApiService;
    }

    public User login(String nickname) {
        LoginDto dto = new LoginDto(nickname);
        LoginDto loginDto = chatApiService.login(dto);
        if (loginDto == null) {
            return null;
        }
        return new User().setNickname(loginDto.nickname());
    }

    public void logout(String nickname) {
        LoginDto dto = new LoginDto(nickname);
        chatApiService.logout(dto);
    }

    public List<Topic> getTopics() {
        return chatApiService.getTopics();
    }

    public Message sendMessage(Topic topic, User sender, String body) {
        CreateMessageDto dto = new CreateMessageDto(sender.getNickname(), body);
        MessageDto messageDto = chatApiService.sendMessage(topic.getTitle(), dto);
        return new Message()
                .setBody(messageDto.body())
                .setSender(sender)
                .setTimestamp(Instant.parse(messageDto.timestamp()))
                .setTopic(topic);
    }

    public void loadMessages(Topic topic, Instant after) {
        List<MessageDto> messagesDto = chatApiService.getMessages(topic.getTitle(), after);
        for (MessageDto messageDto : messagesDto) {
            List<User> allUsers = topic.getAllUsers();
            Message message = new Message()
                    .setBody(messageDto.body())
                    .setTimestamp(Instant.parse(messageDto.timestamp()));
            User sender = null;
            if (! allUsers.isEmpty()) {
                sender = allUsers.stream().filter(user -> user != null && user.getNickname().equals(messageDto.sender())).findFirst().orElse(null);
            }
            if (sender == null || allUsers.isEmpty()) {
                sender = new User().setNickname(messageDto.sender());
            }
            message.setSender(sender).setTopic(topic);
        }
    }

}
