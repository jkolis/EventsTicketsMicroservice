package microservice.endpoint;

import microservice.domain.Event;
import microservice.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/events")
public class EventEndpoint {

    @Autowired
    EventService eventService;

    //FU10 - Dodawanie wydarzeń
    //TODO DODAWANIE BILETÓW!!!
    @PostMapping(path = "/createEventAndTickets")
    public String createEventAndTickets(@RequestBody Event event) {
        eventService.createEvent(event);
        return "saved " + event.getId() + "\n";
    }

    //FU11 - Anulowanie wydarzeń
    @PostMapping(path = "/updateEventStatus")
    public String updateEventStatus(@RequestParam long id, @RequestParam String status) {
        Event event = eventService.getEvent(id);
        if (event != null) {//TODO
            event.setStatus(status);
            eventService.updateEvent(event);
            return "updated"; //TODO
        }
        return "problem"; //TODO
    }

    //FU9 - Anulowanie rezerwacji zamówień
    //TODO weryfikacja czy mozna zrezygnowac
    //FU12 - Dodawanie dodatkowych biletów do wydarzeń
    //TODO weryfikacja czy mozna dodac miejsca i ile
    @GetMapping(path = "/showEventInfo")
    public Event showEventInfo(@RequestParam long id) {
        return eventService.getEvent(id);
    }

    //FU11 - Anulowanie wydarzeń
    //FU12 - Dodawanie dodatkowych biletów do wydarzeń
    @GetMapping(path = "/showEventsCreatedByUser")
    public List<Event> showEventsCreatedByUser(@RequestParam String user) {
        return eventService.showUserEvents(user);
    }

}
