package eventstickets.service;

import eventstickets.dao.TicketRepository;
import eventstickets.domain.Constants;
import eventstickets.domain.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    EventService eventService;

    public Ticket getTicket(long id) {
        return ticketRepository.findTicketById(id);
    }

    public void addTickets(List<Ticket> tickets) {
        for (Ticket t : tickets) {
            ticketRepository.save(t);
        }
    }

    public void addTicketsToEvent(long eventID, int regular, int premium) {
        for (int i = 0; i < regular; i++) {
            Ticket t = new Ticket();
            t.setEventID(eventID);
            t.setType(Constants.TICKET_REGULAR);
            ticketRepository.save(t);
        }
        for (int i = 0; i < premium; i++) {
            Ticket t = new Ticket();
            t.setEventID(eventID);
            t.setType(Constants.TICKET_PREMIUM);
            ticketRepository.save(t);
        }

    }

    public void addTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    //TODO zmienic String
    public void addTicketsOfType(long eventID, int number, String type) {
        Ticket t;
        for (int i = 0; i < number; i++) {
            t = new Ticket();
            t.setEventID(eventID);
            t.setType(type);
            ticketRepository.save(t);
        }
    }

    public void updateTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    public void cancelTicketsForEvent(long eventID) {
        List<Ticket> tickets = ticketRepository.findAllByEventID(eventID);
        for (Ticket t : tickets) {
            t.setStatus(Constants.TICKET_CANCELED);
            ticketRepository.save(t);
        }
    }

    public List<Ticket> showTicketsForEvent(long eventID) {
        return ticketRepository.findAllByEventID(eventID);
    }

    public List<Ticket> showAvailableTickets(long eventID) {
        return ticketRepository.findAllByEventIDAndStatus(eventID, Constants.TICKET_AVAILABLE);
    }

    public boolean doesEventExist(long eventID) {
        return (eventService.getEvent(eventID) != null);
    }

}
