package microservice.endpoint;

import microservice.domain.Constants;
import microservice.domain.Event;
import microservice.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/events")
public class EventEndpoint {

    @Autowired
    EventService eventService;

    //FU10 - Dodawanie wydarzeń
    @PostMapping(path = "/createEventAndTickets",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public Response createEventAndTickets(@RequestBody Event event) {
        eventService.createEvent(event);
        eventService.addTickets(event);
        return Response.trueStatus();
    }

    //FU11 - Anulowanie wydarzeń
    @PostMapping(path = "/updateEventStatus",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public Response updateEventStatus(@RequestParam long id, @RequestParam String status) {
        Event event = eventService.getEvent(id);
        if (event != null) {
            event.setStatus(status);
            eventService.updateEvent(event);
            return Response.falseStatus();
        }
        return Response.falseStatus();
    }

    //FU12 - Dodawanie dodatkowych biletów do wydarzeń
    @GetMapping(path = "/showEventInfo",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public Event showEventInfo(@RequestParam long id) {
        return eventService.getEvent(id);
    }

    //FU9 - Anulowanie rezerwacji zamówień
    @GetMapping(path = "/checkResignationTime")
    public boolean checkResignationTime(@RequestParam long id) {
        return eventService.checkResignation(id);
    }

    //FU11 - Anulowanie wydarzeń
    //FU12 - Dodawanie dodatkowych biletów do wydarzeń
    @GetMapping(path = "/showEventsCreatedByUser")
    public List<Event> showEventsCreatedByUser(@RequestParam String user) {
        return eventService.showUserEvents(user);
    }

    @GetMapping(path = "/cancelEvent",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public Response cancelEvent(@RequestParam long id) {
        if(eventService.sendCancelEventReq()) {
            return Response.trueStatus();
        }
        return Response.falseStatus();
    }

    //FU12
    @GetMapping(path = "/showAvailableSeatsNumber",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public int showAvailableSeatsNumber(@RequestParam long eventID) {
        Event event = eventService.getEvent(eventID);
        int occupiedSeats = event.getPremiumTicketsNumber() + event.getRegularTicketsNumber();
        return Constants.MAX_SEATS - occupiedSeats;
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
