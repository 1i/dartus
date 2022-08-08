package com.onei.dartus.intents;


import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.request.Predicates;
import com.onei.dartus.BirdusS3Client;
import com.onei.dartus.Utils;

import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class DayIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.intentName("dayIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        BirdusS3Client birdusS3Client = new BirdusS3Client();

        Request request1 = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request1;
        Map<String, Slot> slots = intentRequest.getIntent().getSlots();
        Slot day = slots.get("day");
        String dayValue = (day != null) ? day.getValue() : "null";
        log.debug("dayValue " + dayValue);
        log.debug("slot day " + day);
        String results = birdusS3Client.getResultsForDate(Utils.getDateFromDay(dayValue));
        return input.getResponseBuilder()
                .withSpeech(results)
                .withSimpleCard("Results for "+dayValue, results)
                .build();
    }

}