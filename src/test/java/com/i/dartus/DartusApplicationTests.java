package com.i.dartus;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onei.dartus.BirdusS3Client;
import com.onei.dartus.Model;

@SpringBootTest
class DartusApplicationTests {

	private BirdusS3Client birdusS3Client = new BirdusS3Client();

	@Test
    public void getJson() throws Exception {
		System.out.println("getJson");

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yy-MM-dd"));
        URL path = new URL("http://birdus.s3-eu-west-1.amazonaws.com/" + today + ".json");

        List<Model> models;
        ObjectMapper objectMapper = new ObjectMapper();
        models = objectMapper.readValue(path, new TypeReference<List<Model>>() {
        });
		System.out.println(models);

        assertNotNull(models);
    }

	@Test
    public void getResultsForTest() {
        String results = birdusS3Client.getResultsForCounty("cork");

        System.out.println(results);
    }

    @Test
    public void getResultsForDay() {
        String results = birdusS3Client.getResultsForDate(LocalDate.now());
    }

    @Test
    public void getDayOfWeek() {
        String date = "tuesday";
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(date.toUpperCase());

        int differenceOfDays = today.minus(dayOfWeek.getValue()).getValue();
        LocalDate expectedDate = LocalDate.now().minusDays(differenceOfDays);
        String results = birdusS3Client.getResultsForDate(expectedDate);
        System.out.println(results);
    }

    @Test
    public void getLocationByDayOfWeek() {
        LocalDate date = LocalDate.now().minusDays(4);
        String county = "dublin";

        String results = birdusS3Client.getResultsForCountyByDay(county, date);
        assertNotNull(results);
        assertTrue(results.contains("dublin") && results.contains("sightings"));
    }

    @Test
    public void getLocationByDayOfWeekCork() {
        LocalDate date = LocalDate.now().minusDays(7);
        String county = "cork";

        String results = birdusS3Client.getResultsForCountyByDay(county, date);
        assertNotNull(results);
        assertTrue(results.contains("cork") && results.contains("sightings"));
    }



}
