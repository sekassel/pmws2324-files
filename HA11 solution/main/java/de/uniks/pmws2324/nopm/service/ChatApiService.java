package de.uniks.pmws2324.nopm.service;

import de.uniks.pmws2324.nopm.model.Topic;
import de.uniks.pmws2324.nopm.model.dtos.CreateMessageDto;
import de.uniks.pmws2324.nopm.model.dtos.LoginDto;
import de.uniks.pmws2324.nopm.model.dtos.MessageDto;
import javafx.scene.control.Alert;
import kong.unirest.GetRequest;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static de.uniks.pmws2324.nopm.Constants.REST_SERVER_URL;


public class ChatApiService {
    static
    {
        Unirest.config().addDefaultHeader("Content-Type", "application/json");
    }

    public LoginDto login(LoginDto dto) {
        HttpResponse<JsonNode> json = Unirest.post(REST_SERVER_URL + "/auth/login")
                .body(dto.toJson().toString()).asJson();
        try {
            return LoginDto.fromJson(json.getBody().getObject());
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, json.getBody().getObject().toString()).showAndWait();
            return null;
        }
    }

    public void logout(LoginDto dto) {
        HttpResponse<JsonNode> json = Unirest.post(REST_SERVER_URL + "/auth/logout")
                .body(dto.toJson().toString()).asJson();
        if(!json.isSuccess()) {
            new Alert(Alert.AlertType.ERROR, json.getBody().getObject().toString()).showAndWait();
        }
    }

    public List<Topic> getTopics() {
        HttpResponse<JsonNode> json = Unirest.get(REST_SERVER_URL + "/topics").asJson();
        try {
            JSONArray array = json.getBody().getArray();
            return Topic.fromJsonArray(array);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, json.getBody().getObject().toString()).showAndWait();
            return Collections.emptyList();
        }
    }

    public MessageDto sendMessage(String topic, CreateMessageDto dto) {
        HttpResponse<JsonNode> json = Unirest.post(REST_SERVER_URL + "/topics/" + topic + "/messages")
                .body(dto.toJson().toString()).asJson();
        try {
            return MessageDto.fromJson(json.getBody().getObject());
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, json.getBody().getObject().toString()).showAndWait();
            return null;
        }
    }

    // after could be null
    public List<MessageDto> getMessages(String topic, Instant after) {
        GetRequest getRequest = Unirest.get(REST_SERVER_URL + "/topics/" + topic + "/messages");
        if(after != null) {
            getRequest.queryString("after", after.toString());
        }
        HttpResponse<JsonNode> json = getRequest.asJson();
        try {
            JSONArray array = json.getBody().getArray();
            return MessageDto.fromJsonArray(array);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, json.getBody().getObject().toString()).showAndWait();
            return Collections.emptyList();
        }
    }
}
