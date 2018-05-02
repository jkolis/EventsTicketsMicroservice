package eventstickets.service;

import eventstickets.dao.EventRepository;
import eventstickets.domain.Event;
import eventstickets.endpoint.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    TicketService ticketService;

    public void createEvent(Event event) {
        eventRepository.save(event);
        addTickets(event);
    }

    public void addTickets(Event event) {
        long eventId = event.getId();
        ticketService.addTicketsOfType(eventId,
                event.getPremiumTicketsNumber(), "premium");
        ticketService.addTicketsOfType(eventId,
                event.getRegularTicketsNumber(), "regular");
    }

    public Event getEvent(long id) {
        return eventRepository.findEventById(id);
    }

    public void updateEvent(Event event) {
        eventRepository.save(event);
    }

    public List<Event> showUserEvents(String user) {
        return eventRepository.findAllByOrOrganizer(user);
    }

    public boolean checkResignation(long id) {
        Calendar cal = Calendar.getInstance();
        Date currentDate = new Date();
        cal.setTime(currentDate);
        int resignationDays = getEvent(id).getResignationPeriod();
        cal.add(Calendar.DATE, resignationDays);
        Date date = cal.getTime();
        return !date.after(currentDate);
    }

    public boolean sendCancelEventReq() {
        RestTemplate rt = new RestTemplate();
        //TODO
        Response res = rt.getForObject("http://localhost:8181/tickets/test", Response.class);
        return res.getStatus();
    }
}
