package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.AnswerDetailsResponse;
import com.upgrad.quora.api.model.AnswerEditRequest;
import com.upgrad.quora.api.model.AnswerEditResponse;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.entity.Answer;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/*")
public class AnswerController {

    @Autowired
    AnswerService answerService;
    @Autowired
    QuestionBusinessService questionBusinessService;


    @RequestMapping(method = RequestMethod.GET, path = "answer/all/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDetailsResponse> getAnswers(@RequestHeader("authorization") final String authorization, @PathVariable("questionId") final String questionId) throws AuthorizationFailedException, InvalidQuestionException {

        List<Answer> answers=answerService.getAnswersForQuestionId(questionId, authorization);
        String uuid=questionId;
        String answerContent="";
        String questionContent="";


        for (Answer answer:answers) {
            answerContent+=answerContent+answer.getAnswer();
            questionContent=answer.getQuestion().getContent();
        }
        AnswerDetailsResponse answerDetailsResponse= new AnswerDetailsResponse();
        answerDetailsResponse.setId(uuid);
        answerDetailsResponse.setQuestionContent(questionContent);
        answerDetailsResponse.setAnswerContent(answerContent);


        return new ResponseEntity<AnswerDetailsResponse>(answerDetailsResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/answer/edit/{answerId}",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswerContent(@RequestHeader("authorization") final String authorization, @PathVariable("answerId") final String answeruuid, @RequestBody AnswerEditRequest answerEditRequest) throws AuthorizationFailedException, AnswerNotFoundException {

        AnswerEditResponse answerEditResponse= new AnswerEditResponse().id( answerService.editAnswerContent(answeruuid,answerEditRequest.getContent(),authorization)).status("ANSWER EDITED");
        return new ResponseEntity<AnswerEditResponse>(answerEditResponse, HttpStatus.OK);
    }

}
