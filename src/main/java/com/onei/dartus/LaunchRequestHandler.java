
package com.onei.dartus;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.Predicates;

import java.util.Optional;

public class LaunchRequestHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = "LaunchRequest";

        BirdusS3Client birdusS3Client = new BirdusS3Client();

        return input.getResponseBuilder()
                .withSpeech(birdusS3Client.getResults())
                .withSimpleCard(speechText, birdusS3Client.getResults())
                .withReprompt(birdusS3Client.getResults())
                .build();
    }

}