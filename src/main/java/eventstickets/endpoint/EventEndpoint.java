package eventstickets.endpoint;

import eventstickets.domain.Constants;
import eventstickets.domain.Event;
import eventstickets.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Event> showEvents() {
        return eventService.showEvents();
    }

    //FU12 - Dodawanie dodatkowych biletów do wydarzeń
    @RequestMapping(method = RequestMethod.GET,
            value = "/{eventid}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Event showEventInfo(@PathVariable long eventid) {
        return eventService.getEvent(eventid);
    }

    //FU9 - Anulowanie rezerwacji zamówień
    @RequestMapping(method = RequestMethod.GET,
            value = "/resigantion/{eventid}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean checkResignationTime(@PathVariable long eventid) {
        return eventService.checkResignation(eventid);
    }

    //FU11 - Anulowanie wydarzeń
    //FU12 - Dodawanie dodatkowych biletów do wydarzeń
    @RequestMapping(method = RequestMethod.POST,
            value = "/user/{userid}",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                    MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Event> showEventsCreatedByUser(@PathVariable String userid) {
        return eventService.showUserEvents(userid);
    }


    @RequestMapping(method = RequestMethod.DELETE,
            value = "/{eventid}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Response cancelEvent(@PathVariable long eventid) {
        if (eventService.sendCancelEventReq(eventid)) {
            eventService.updateEventStatus(eventid, Constants.EVENT_CANCELED);
            return Response.trueStatus();
        }
        return Response.falseStatus();
    }

    //FU12
    @RequestMapping(method = RequestMethod.GET,
            value = "/seats/{eventid}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public int showAvailableSeatsNumber(@PathVariable long eventid) {
        Event event = eventService.getEvent(eventid);
        int occupiedSeats = event.getPremiumTicketsNumber() + event.getRegularTicketsNumber();
        return Constants.MAX_SEATS - occupiedSeats;
    }

    @RequestMapping(method = RequestMethod.GET,
    value = "/token-expired")
    public void tokenExpired() {
        //TODO
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
