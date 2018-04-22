package microservice.domain;

import javax.persistence.*;

@Entity
@Table(name="event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //TODO
    private long id;
    private String name;
    private String eventAddress; //TODO
    private String eventType;
    private String artist;
    private int premiumTicketsNumber;
    private int regularTicketsNumber;
    private String date; //TODO date
    private String organizer;
    private String resignationPeriod; //todo date
    private String status;


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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getResignationPeriod() {
        return resignationPeriod;
    }

    public void setResignationPeriod(String resignationPeriod) {
        this.resignationPeriod = resignationPeriod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


