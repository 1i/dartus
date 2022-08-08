package com.onei.dartus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class BirdusS3Client {

    private StringBuilder stringBuilder = new StringBuilder();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public String getResults() {
        List<Model> models = fetchS3Json(LocalDate.now());
        stringBuilder.append("Yesterday had " + models.size() + " sightings. ");

        Map<String, List<Model>> byCountiesSightings = models.stream().collect(Collectors.groupingBy(Model::getCounty));
        List<Map.Entry<String, List<Model>>> sorted = byCountiesSightings.entrySet().stream().sorted(Comparator.comparing(listEntry -> listEntry.getValue().size())).collect(Collectors.toList());

        for (int i = sorted.size() - 1; i > 0; i--) {
            stringBuilder.append("In " + sorted.get(i).getKey() + ". ");
            sorted.get(i).getValue().stream().distinct().forEach(model -> stringBuilder.append(model.getCommonName() + ", "));
        }
        return stringBuilder.toString();
    }

    public String getResultsForCounty(String county) {
        List<Model> models = fetchS3Json(LocalDate.now());

        List<Model> counties = models.stream().filter(model -> model.getCounty().equalsIgnoreCase(county)).collect(Collectors.toList());
        stringBuilder.append(county + " yesterday had " + counties.size() + " sightings. ");

        for (Model location : counties) {
            stringBuilder.append(location.getCommonName() + ", ");
        }
        return stringBuilder.toString();
    }

    public String getResultsForDate(LocalDate date) {
        List<Model> models = fetchS3Json(date);

        Map<String, List<Model>> byCountiesSightings = models.stream().collect(Collectors.groupingBy(Model::getCounty));

        List<Map.Entry<String, List<Model>>> sorted;
        sorted = byCountiesSightings.entrySet().stream().sorted(Comparator.comparing(listEntry -> listEntry.getValue().size())).collect(Collectors.toList());
        stringBuilder.append("Last " + date.getDayOfWeek().toString() + " had " + models.size() + " sightings. ");

        for (int i = sorted.size() - 1; i > 0; i--) {
            stringBuilder.append("In " + sorted.get(i).getKey() + ". ");
            sorted.get(i).getValue().stream().distinct().forEach(model -> stringBuilder.append(model.getCommonName() + ", "));
        }

        return stringBuilder.toString();
    }

    public String getResultsForCountyByDay(String county, LocalDate date) {
        log.debug("Start getResultsForCountyByDay " + county + " " + date);
        List<Model> models = fetchS3Json(date);

        List<Model> counties = models.stream().filter(model -> model.getCounty().equalsIgnoreCase(county)).collect(Collectors.toList());
        stringBuilder.append("Last " + date.getDayOfWeek().toString() + "  ");
        stringBuilder.append(county + " had " + counties.size() + " sightings. ");

        for (Model location : counties) {
            stringBuilder.append(location.getCommonName() + ", ");
        }
        return stringBuilder.toString();
    }

    private List<Model> fetchS3Json(LocalDate date) {
        log.debug("Get Results for [{}]", date);
        String formattedDate;
        try {
            formattedDate = date.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
        } catch (DateTimeParseException dte) {
            log.error("DateTimeParseException ", dte);
            formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yy-MM-dd"));
            log.info("DTPE fetch for [{}]", formattedDate);
        }

        try {
            URL path = new URL("http://birdus.s3-eu-west-1.amazonaws.com/" + formattedDate + ".json");
            return objectMapper.readValue(path, new TypeReference<List<Model>>() {
            });
        } catch (IOException e) {
            log.error(e.toString());
        }
        return Collections.emptyList();
    }
}
