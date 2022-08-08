package com.onei.dartus.intents;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.onei.dartus.BirdusS3Client;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

@Slf4j
public class FallbackIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.FallbackIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        log.debug("Input {}",input);
        String speechText = "Did you want the sightings for yesterday? If not ask for Help. ";
        BirdusS3Client birdusS3Client = new BirdusS3Client();
        String results = birdusS3Client.getResults();
        return input.getResponseBuilder()
                .withSpeech(speechText + results)
                .withSimpleCard("Need Help?", speechText)
                .build();
    }
}