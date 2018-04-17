package com.assessment.userservice;

import com.assessment.userservice.model.User;
import com.assessment.userservice.service.UserService;
import com.assessment.userservice.service.UserServiceImpl;
import com.assessment.userservice.util.JsonReader;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.json.JSONArray;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {

    private JsonReader jsonReader;
    private UserService userService;
    private final String dataUrl = "http://jsonplaceholder.typicode.com/posts";

    public JSONArray data;

    public JSONArray mockData() throws JSONException {
        JSONArray array = new JSONArray();
        JSONObject item1 = new JSONObject();
        item1.put("userId", "1");
        item1.put("id", 1);
        item1.put("title", "test title 1");
        item1.put("body", "test body 1");

        JSONObject item2 = new JSONObject();
        item2.put("userId", "2");
        item2.put("id", 2);
        item2.put("title", "test title 2");
        item2.put("body", "test body 2");

        JSONObject item3 = new JSONObject();
        item3.put("userId", "2");
        item3.put("id", 3);
        item3.put("title", "test title 3");
        item3.put("body", "test body 3");

        JSONObject item4 = new JSONObject();
        item4.put("userId", "4");
        item4.put("id", 4);
        item4.put("title", "test title 4");
        item4.put("body", "test body 4");
        array.put(item1);
        array.put(item2);
        array.put(item3);
        array.put(item4);

        return array;
    }

    @Before
    public void setup() throws JSONException {

        data = mockData();
        jsonReader = Mockito.mock(JsonReader.class);
        userService = new UserServiceImpl(jsonReader);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnCorrectUniqueUserIdCount() throws IOException, JSONException {
        Mockito.when(jsonReader.readJsonArrayFromUrl(dataUrl)).thenReturn(data);
        List<Map<String,Object>> tallyResult = userService.tallyUserIds();
        tallyResult.stream()
                    .forEach(le -> {
                                if(le.get("userId") == Integer.toString(1)){
                                    assertEquals(le.get("count"),1);
                                }
                                if(le.get("userId") == Integer.toString(2)){
                                    assertEquals(le.get("count"),2);
                                }
                                if(le.get("userId") == Integer.toString(4)){
                                    assertEquals(le.get("count"),1);
                                } });
    }

    @Test
    public void shouldUpdateUserBodyAndTitleForInputIndexAndText() throws IOException, JSONException{
        Mockito.when(jsonReader.readJsonArrayFromUrl(dataUrl)).thenReturn(data);
        List<User> users = userService.updateUser(2, "update title for id 3");
        assertEquals(users.get(2).getId(), 3);
        assertEquals(users.get(2).getUserId(), 2);
        assertEquals(users.get(2).getBody(),"update title for id 3");
        assertEquals(users.get(2).getTitle(),"update title for id 3");

    }
}
