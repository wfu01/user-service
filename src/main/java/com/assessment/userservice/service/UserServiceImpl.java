package com.assessment.userservice.service;

import com.assessment.userservice.controller.RestApiController;
import com.assessment.userservice.model.User;
import com.assessment.userservice.util.JsonReader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;



@Service("userService")
public class UserServiceImpl implements UserService {

    public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

    private final String dataUrl = "http://jsonplaceholder.typicode.com/posts";

    @Autowired
    private JsonReader jsonReader;

    public UserServiceImpl(JsonReader jsonReader){
        this.jsonReader = jsonReader;
    }


    @Override
    public List<Map<String,Object>> tallyUserIds(){
        try {
            JSONArray jsonArray = jsonReader.readJsonArrayFromUrl(dataUrl);
            Gson gson=new Gson();
            Type listType = new TypeToken<ArrayList<User>>(){}.getType();
            List<User> users = gson.fromJson(jsonArray.toString(), listType);

           Map<Object, Object> map = users.stream()
                    .collect(Collectors.groupingBy(User::getUserId, Collectors.counting()))
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

            List<Map<String,Object>> list = map.entrySet().stream()
                    .map( e -> { Map<String,Object> innerMap = new LinkedHashMap<>();
                                        innerMap.put("userId", e.getKey());
                                        innerMap.put("count", e.getValue());

                                        return innerMap;})
                    .collect(Collectors.toList());

            return list;

        } catch (IOException e) {
            logger.error("Exception while counting unique user id from input data");
            return null;
        }
    }

    @Override
    public List<User> updateUser(int index, String inputText) {
        try {
            JSONArray jsonArray = jsonReader.readJsonArrayFromUrl(dataUrl);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<User>>(){}.getType();
            List<User> users = gson.fromJson(jsonArray.toString(), listType);
            if(index < 0 || inputText == null || inputText.isEmpty()){
                throw new Exception(String.format("Invalid index: %s or inputTex: %s", index, inputText));
            }
            users.stream()
                    .filter(i -> users.indexOf(i) == index)
                    .forEach(e -> {e.setBody(inputText);
                                   e.setTitle(inputText);
                    });
            return users;
        } catch (Exception e) {
            logger.error(String.format("Exception while update user for index: %s inputText: %s", index, inputText));
            return null;
        }
    }

}
