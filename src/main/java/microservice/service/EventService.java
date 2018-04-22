package microservice.service;

import microservice.dao.EventRepository;
import microservice.domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public void addEvent(Event event) {
        eventRepository.save(event);
        addTickets(event);
    }

    private void addTickets(Event event) {
        //TODO
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
}
