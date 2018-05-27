package eventstickets.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import eventstickets.domain.Constants;
import eventstickets.domain.Event;
import eventstickets.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
public class EventEndpoint {

    @Autowired
    EventService eventService;

    //FU10 - Dodawanie wydarzeń
    @RequestMapping(method = RequestMethod.POST,
            value = "/",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                    MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Response createEventAndTickets(Event event) {
        eventService.createEvent(event);
        eventService.addTickets(event);
        return Response.trueStatus();
    }

    //FU11 - Anulowanie wydarzeń
    @RequestMapping(method = RequestMethod.POST,
            value = "/status/{eventid}",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                    MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Response updateEventStatus(@PathVariable long eventid, @RequestParam String status) {
        Event event = eventService.getEvent(eventid);
        if (event != null) {
            event.setStatus(status);
            eventService.updateEvent(event);
            return Response.trueStatus();
        }
        return Response.falseStatus();
    }

    //FU5 - Wyszukiwanie wydarzeń
    @RequestMapping(method = RequestMethod.GET,
            value = "/show",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseErrorHandler showEvents() throws JsonProcessingException {
        List<Event> events = eventService.showEvents();
        return new ResponseErrorHandler<>(HttpStatus.OK.value(), events, true, 0,
                "Events", "All events", "http://kolis.pl");
    }

    //FU12 - Dodawanie dodatkowych biletów do wydarzeń
    @RequestMapping(method = RequestMethod.GET,
            value = "/{eventid}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseErrorHandler showEventInfo(@PathVariable long eventid) throws JsonProcessingException {
        Event event = eventService.getEvent(eventid);
        if (event == null) {
            return new ResponseErrorHandler<>(HttpStatus.NOT_FOUND.value(), "", false,  101,
                    "No such event", "Event with id " + eventid + " doesn't exist", Constants.URL);
        }
        return new ResponseErrorHandler<>(HttpStatus.OK.value(), event, true, 0,
                "Events", "All events", Constants.URL);
    }

    //FU9 - Anulowanie rezerwacji zamówień
    @RequestMapping(method = RequestMethod.GET,
            value = "/resignation/{eventid}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseErrorHandler checkResignationTime(@PathVariable long eventid) {
        Event event = eventService.getEvent(eventid);
        if (event == null) {
            return new ResponseErrorHandler<>(HttpStatus.NOT_FOUND.value(), "", false,  101,
                    "No such event", "Event with id " + eventid + " doesn't exist", Constants.URL);
        }
        boolean result = eventService.checkResignation(eventid);
        if (result)
        return new ResponseErrorHandler<>(200, "", true, 100, "Can resign",
                "You still can resign from this event", Constants.URL);
        return new ResponseErrorHandler<>(200, "", false, 107, "You can't resign",
                "Resignation time for this event has expired", Constants.URL);
    }

    //FU11 - Anulowanie wydarzeń
    //FU12 - Dodawanie dodatkowych biletów do wydarzeń
    @RequestMapping(method = RequestMethod.POST,
            value = "/user/{userid}",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                    MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseErrorHandler showEventsCreatedByUser(@PathVariable String userid, HttpServletRequest httpServletRequest) {
        //TODO tylko on sam moze zobaczyc swoje eventy?
        List<Event> userEvents = eventService.showUserEvents(userid);
        if (userEvents == null) {
            return new ResponseErrorHandler<>(404, "", false, 108, "No events",
                    "This user doesn't have eny events", Constants.URL);
        }
        return new ResponseErrorHandler<>(200, userEvents, true, 100, "User events",
                "User " + userid + " has created these events", Constants.URL);
    }


    @RequestMapping(method = RequestMethod.DELETE,
            value = "/{eventid}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseErrorHandler cancelEvent(@PathVariable long eventid, HttpServletRequest httpServletRequest) {
        Event event = eventService.getEvent(eventid);
        if (event == null) {
            return new ResponseErrorHandler<>(HttpStatus.NOT_FOUND.value(), "", false,  101,
                    "No such event", "Event with id " + eventid + " doesn't exist", Constants.URL);
        }
        //id z tokena nie rowna sie organizatorowi
        if (!httpServletRequest.getAttribute(Constants.TOKEN_PAYLOAD_USER).equals(event.getOrganizer())) {
//            return new ResponseErrorHandler<>()
        }
        if (eventService.sendCancelEventReq(eventid)) {
            eventService.updateEventStatus(eventid, Constants.EVENT_CANCELED);
            return new ResponseErrorHandler<>(200, "", true, 100, "Event cancelled",
                    "Event " + eventid + " + has been cancelled", Constants.URL);
        }
        return new ResponseErrorHandler<>(400, "", false, 100, "", "", Constants.URL);//TODO
    }

    //FU12
    @RequestMapping(method = RequestMethod.GET,
            value = "/seats/{eventid}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseErrorHandler showAvailableSeatsNumber(@PathVariable long eventid) {
        Event event = eventService.getEvent(eventid);
        if (event == null) {
            return new ResponseErrorHandler<>(HttpStatus.NOT_FOUND.value(), "", false,  101,
                    "No such event", "Event with id " + eventid + " doesn't exist", Constants.URL);
        }
        int occupiedSeats = event.getPremiumTicketsNumber() + event.getRegularTicketsNumber();
        int availableSeats = Constants.MAX_SEATS - occupiedSeats;
        return new ResponseErrorHandler<>(200, availableSeats, true, 100, "Available seats",
                "available seats in event: " + eventid, Constants.URL);
    }

    @RequestMapping(method = RequestMethod.GET,
    value = "/token-expired")
    public ResponseErrorHandler tokenExpired() {
        return new ResponseErrorHandler<>(401, "", false, 102, "Authorization failed",
                "Token has expired", Constants.URL);
    }

//    @GetMapping(path = "/getTicket",
//            produces=MediaType.APPLICATION_JSON_VALUE)
//    public String getTicket(@RequestParam long id) {
//        RestTemplate restTemplate = new RestTemplate();
//        Ticket ticket = restTemplate.getForObject("http://localhost:8080/tickets/getTicket", Ticket.class);
//        return "got " + ticket.getEventID();
//    }
//
//    @GetMapping(path = "/test")
//    public String getHello() {
//        RestTemplate rt = new RestTemplate();
//        String hello = rt.getForObject("http://localhost:8080/tickets/test", String.class);
//        return hello;
//    }
}
