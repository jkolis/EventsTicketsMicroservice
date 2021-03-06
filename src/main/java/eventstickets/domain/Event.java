package eventstickets.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //TODO
    private long id;
    private String name;
    private String eventAddress; //TODO
    private String eventType;
    private String artist;
    private int premiumTicketsNumber;
    private int regularTicketsNumber;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;
    private String organizer;
    private int resignationPeriod; //w dniach
    private String status;
    private int columns;
    private int rows;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getPremiumTicketsNumber() {
        return premiumTicketsNumber;
    }

    public void setPremiumTicketsNumber(int premiumTicketsNumber) {
        this.premiumTicketsNumber = premiumTicketsNumber;
    }

    public int getRegularTicketsNumber() {
        return regularTicketsNumber;
    }

    public void setRegularTicketsNumber(int regularTicketsNumber) {
        this.regularTicketsNumber = regularTicketsNumber;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getResignationPeriod() {
        return resignationPeriod;
    }

    public void setResignationPeriod(int resignationPeriod) {
        this.resignationPeriod = resignationPeriod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
}


