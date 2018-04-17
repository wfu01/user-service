package com.assessment.userservice.controller;


import com.assessment.userservice.model.User;
import com.assessment.userservice.service.UserService;
import com.assessment.userservice.util.CustomError;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RestApiController {

	public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

    @Autowired
    UserService userService;


	@RequestMapping(value = "/user/get/", method = RequestMethod.GET)
	public @ResponseBody
    ResponseEntity<List<Map<String,Object>>> tallyUniqueUserIds() {
        List<Map<String,Object>> tallyResult = userService.tallyUserIds();
        if(tallyResult == null || tallyResult.isEmpty()){
            return new ResponseEntity(new CustomError("Unable to tally unique user id"),
                    HttpStatus.NOT_FOUND);
        }
		return new ResponseEntity<List<Map<String,Object>>>(tallyResult, HttpStatus.OK);
	}


    @RequestMapping(value = "/user/update/{index}/{inputText}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateTitleAndBodyByIndex(@PathVariable("index") int index, @PathVariable("inputText") String inputText) {
        List<User> users = userService.updateUser(index, inputText);
        if (users == null || users.isEmpty()) {
            return new ResponseEntity(new CustomError(String.format("Unable to upate. User for index: %s input text: %s",index, inputText)),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

}