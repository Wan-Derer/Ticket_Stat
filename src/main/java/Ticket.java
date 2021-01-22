package main.java;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Set;

@JsonAutoDetect
public class Ticket {
    // data from JSON
    public String origin;              // origin airport code, for example: "VVO"
    public String origin_name;         // origin city, "Владивосток"
    public String destination;         // destination airport code, "TLV"
    public String destination_name;    // destination city, "Тель-Авив"
    public String departure_date;      // "12.05.18"
    public String departure_time;      // "16:20"
    public String arrival_date;        // "12.05.18"
    public String arrival_time;        // "22:10"
    public String carrier;             //  carrier code, "TK"
    public int stops;                  //  number of changes, 3
    public double price;               //  12400

    // calculated data
    public long transitTime;            // transit time in minutes

    public Set<Ticket> tickets;        // to load json data

    @Override
    public String toString() {
        return "Ticket{" +
                "origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", departure_date='" + departure_date + '\'' +
                ", departure_time='" + departure_time + '\'' +
                ", arrival_date='" + arrival_date + '\'' +
                ", arrival_time='" + arrival_time + '\'' +
                ", transitTime=" + transitTime +
                '}';
    }
}
