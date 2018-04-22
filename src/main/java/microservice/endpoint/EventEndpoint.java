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

    @PostMapping(path = "/createEventAndTickets")
    public String addNewEvent(@RequestBody Event event) {
        eventService.addEvent(event);
        return "saved " + event.getId() + "\n";
    }

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

    @GetMapping(path = "/showEventInfo")
    public Event showEventInfo(@RequestParam long id) {
        return eventService.getEvent(id);
    }

    @GetMapping(path = "/showEventsCreatedByUser")
    public List<Event> showEventsCreatedByUser(@RequestParam String user) {
        return eventService.showUserEvents(user);
    }

}
