package com.assessment.userservice.service;

import com.assessment.userservice.model.User;
import org.json.JSONArray;

import java.util.List;
import java.util.Map;

public interface UserService {

    List<User> updateUser(int index, String inputText);

    List<Map<String,Object>> tallyUserIds();
}
