package eventstickets.service;

import eventstickets.dao.EventRepository;
import eventstickets.domain.Constants;
import eventstickets.domain.Event;
import eventstickets.endpoint.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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

    public boolean sendCancelEventReq(long eventid) {
        RestTemplate rt = new RestTemplate();
        final String uri = Constants.ORDERS_URI + "/orders/event/delete/" + eventid;
        Response res = rt.getForObject(uri, Response.class);
        return res.getStatus();
    }

    public void updateEventStatus(long eventid, String status) {
        Event e = getEvent(eventid);
        e.setStatus(status);
        eventRepository.save(e);
    }

    public List<Event> showEvents() {
        return eventRepository.findAll();
    }
}
