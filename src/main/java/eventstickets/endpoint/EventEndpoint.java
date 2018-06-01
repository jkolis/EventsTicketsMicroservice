package eventstickets.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import eventstickets.domain.Constants;
import eventstickets.domain.Event;
import eventstickets.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
    public JsonErrorResponses createEventAndTickets(Event event, HttpServletRequest httpServletRequest) {
        if (checkPermissions(httpServletRequest)) {
            return new JsonErrorResponses<>(HttpStatus.NOT_FOUND.value(), "", false, 104,
                    "Permission denied", "Only admin can do this action", Constants.URL);
        }
        eventService.createEvent(event);
        eventService.addTickets(event);
        return new JsonErrorResponses<>(HttpStatus.OK.value(), "", true, 100,
                "Event created", "Event " + event.getId() + " has been created", Constants.URL);
    }

    //FU11 - Anulowanie wydarzeń
    @RequestMapping(method = RequestMethod.POST,
            value = "/status/{eventid}",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                    MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonErrorResponses updateEventStatus(@PathVariable long eventid, @RequestParam String status, HttpServletRequest httpServletRequest) {
        Event event = eventService.getEvent(eventid);
        if (event != null) {
            event.setStatus(status);
            eventService.updateEvent(event);
            return new JsonErrorResponses<>(HttpStatus.OK.value(), "", true, 100,
                    "Event status updated", "Event's " + eventid + " status updated to " + status, Constants.URL);
        }
        return new JsonErrorResponses<>(HttpStatus.NOT_FOUND.value(), "", false,  101,
                "No such event", "Event with id " + eventid + " doesn't exist", Constants.URL);
    }

    //FU5 - Wyszukiwanie wydarzeń
    @RequestMapping(method = RequestMethod.GET,
            value = "/show",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonErrorResponses showEvents() throws JsonProcessingException {
        List<Event> events = eventService.showEvents();
        return new JsonErrorResponses<>(HttpStatus.OK.value(), events, true, 100,
                "Events", "All events", Constants.URL);
    }

    //FU12 - Dodawanie dodatkowych biletów do wydarzeń
    @RequestMapping(method = RequestMethod.GET,
            value = "/{eventid}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonErrorResponses showEventInfo(@PathVariable long eventid) throws JsonProcessingException {
        Event event = eventService.getEvent(eventid);
        if (event == null) {
            return new JsonErrorResponses<>(HttpStatus.NOT_FOUND.value(), new Event(), false,  101,
                    "No such event", "Event with id " + eventid + " doesn't exist", Constants.URL);
        }
        return new JsonErrorResponses<>(HttpStatus.OK.value(), event, true, 0,
                "Events", "All events", Constants.URL);
    }

    //FU9 - Anulowanie rezerwacji zamówień
    @RequestMapping(method = RequestMethod.GET,
            value = "/resignation/{eventid}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonErrorResponses checkResignationTime(@PathVariable long eventid) {
        Event event = eventService.getEvent(eventid);
        if (event == null) {
            return new JsonErrorResponses<>(HttpStatus.NOT_FOUND.value(), "", false,  101,
                    "No such event", "Event with id " + eventid + " doesn't exist", Constants.URL);
        }
        boolean result = eventService.checkResignation(eventid);
        if (result)
        return new JsonErrorResponses<>(200, "", true, 100, "Can resign",
                "You still can resign from this event", Constants.URL);
        return new JsonErrorResponses<>(200, "", false, 107, "You can't resign",
                "Resignation time for this event has expired", Constants.URL);
    }

    //FU11 - Anulowanie wydarzeń
    //FU12 - Dodawanie dodatkowych biletów do wydarzeń
    @RequestMapping(method = RequestMethod.POST,
            value = "/user/{userid}",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                    MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonErrorResponses showEventsCreatedByUser(@PathVariable String userid, HttpServletRequest httpServletRequest) {
        //TODO tylko on sam moze zobaczyc swoje eventy?
        List<Event> userEvents = eventService.showUserEvents(userid);
        if (userEvents == null) {
            return new JsonErrorResponses<>(404, new ArrayList<Event>(), false, 108, "No events",
                    "This user doesn't have eny events", Constants.URL);
        }
        return new JsonErrorResponses<>(200, userEvents, true, 100, "User events",
                "User " + userid + " has created these events", Constants.URL);
    }


    @RequestMapping(method = RequestMethod.DELETE,
            value = "/{eventid}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonErrorResponses cancelEvent(@PathVariable long eventid, HttpServletRequest httpServletRequest) {

        if (checkPermissions(httpServletRequest)) {
            return new JsonErrorResponses<>(HttpStatus.NOT_FOUND.value(), "", false, 104,
                    "Permission denied", "Only admin can do this action", Constants.URL);
        }

        Event event = eventService.getEvent(eventid);
        if (event == null) {
            return new JsonErrorResponses<>(HttpStatus.NOT_FOUND.value(), "", false,  101,
                    "No such event", "Event with id " + eventid + " doesn't exist", Constants.URL);
        }
        //id z tokena nie rowna sie organizatorowi
//        if (!httpServletRequest.getAttribute(Constants.TOKEN_PAYLOAD_USER).equals(event.getOrganizer())) {
////            return new JsonErrorResponses<>()
//        }
        if (eventService.sendCancelEventReq(eventid)) {
            eventService.updateEventStatus(eventid, Constants.EVENT_CANCELED);
            return new JsonErrorResponses<>(200, "", true, 100, "Event cancelled",
                    "Event " + eventid + " + has been cancelled", Constants.URL);
        }
        return new JsonErrorResponses<>(400, "", false, 113, "Connection problem",
                "Problem with connecting to other microservice", Constants.URL);
    }

    private boolean checkPermissions(HttpServletRequest httpServletRequest) {
        return ((int) httpServletRequest.getAttribute(Constants.TOKEN_PAYLOAD_PERMISSION)) != Constants.ADMIN_PERM;
    }

    //FU12
    @RequestMapping(method = RequestMethod.GET,
            value = "/seats/{eventid}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonErrorResponses showAvailableSeatsNumber(@PathVariable long eventid) {
        Event event = eventService.getEvent(eventid);
        if (event == null) {
            return new JsonErrorResponses<>(HttpStatus.NOT_FOUND.value(), 0, false,  101,
                    "No such event", "Event with id " + eventid + " doesn't exist", Constants.URL);
        }
        int occupiedSeats = event.getPremiumTicketsNumber() + event.getRegularTicketsNumber();
        int availableSeats = Constants.MAX_SEATS - occupiedSeats;
        return new JsonErrorResponses<>(200, availableSeats, true, 100, "Available seats",
                "available seats in event: " + eventid, Constants.URL);
    }

    @RequestMapping(method = RequestMethod.GET,
    value = "/token-expired")
    public JsonErrorResponses tokenExpired() {
        return new JsonErrorResponses<>(401, "", false, 102, "Authorization failed",
                "Token has expired", Constants.URL);
    }

    @RequestMapping(method = RequestMethod.GET,
    value = "/token-not-valid")
    public JsonErrorResponses tokenNotValid() {
        return new JsonErrorResponses<>(401, "", false, 112, "Authorization failed",
                "Token is not valid", Constants.URL);
    }

    @RequestMapping(method = RequestMethod.GET,
    value = "token-needed")
    public JsonErrorResponses tokenNeeded() {
        return new JsonErrorResponses<>(401, "", false, 114, "Authorization failed",
                "There is no token in your request", Constants.URL);
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
