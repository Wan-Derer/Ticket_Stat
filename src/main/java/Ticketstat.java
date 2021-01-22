package main.java;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileReader;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class Ticketstat {
    private static final String TZONES_FILE = "src/main/resources/tzones.properties";

    private static Map<String, ZoneId> timezone = new HashMap<>();

    public static void main(String[] args) throws IOException {
        // load known timezones from file
        FileReader fileReader = new FileReader(TZONES_FILE);
        Properties tzones = new Properties();
        tzones.load(fileReader);
        fileReader.close();

        try {
            // load tickets from file
            // file path in the 2nd command line argument
            fileReader = new FileReader(args[1]);
            ObjectMapper mapper = new ObjectMapper();
            Ticket ticket = mapper.readValue(fileReader, Ticket.class);
            fileReader.close();

            Set<Ticket> tickets = ticket.tickets;

            // calculate transit time for each trip
            List<Long> times = new ArrayList<>();
            for (Ticket item : tickets) {
                String[] date = item.departure_date.split("\\.");
                String[] time = item.departure_time.split(":");
                ZonedDateTime originDT = ZonedDateTime.of(
                        Integer.parseInt(date[2]) + 2000,
                        Integer.parseInt(date[1]),
                        Integer.parseInt(date[0]),
                        Integer.parseInt(time[0]),
                        Integer.parseInt(time[1]), 0, 0,
                        ZoneId.of(tzones.getProperty(item.origin)));

                date = item.arrival_date.split("\\.");
                time = item.arrival_time.split(":");
                ZonedDateTime destDT = ZonedDateTime.of(
                        Integer.parseInt(date[2]) + 2000,
                        Integer.parseInt(date[1]),
                        Integer.parseInt(date[0]),
                        Integer.parseInt(time[0]),
                        Integer.parseInt(time[1]), 0, 0,
                        ZoneId.of(tzones.getProperty(item.destination)));

                long originSec = originDT.toInstant().getEpochSecond();
                long destSec = destDT.toInstant().getEpochSecond();
                long transitTime = (destSec - originSec) / 60;

                item.transitTime = transitTime;
                times.add(transitTime);
            }

            // percentile calculation
            Collections.sort(times);
            System.out.println(times);

            int threshold = Integer.parseInt(args[0]);
            int index = times.size() * threshold / 100 - 1;

            System.out.println(threshold + "th percentile for this set is " +
                    times.get(index) / 60 + " hours " + times.get(index) % 60 + " minutes");

        } catch (Exception e) {
            throw new IllegalArgumentException("Incorrect incoming data");
        }

    }
}
