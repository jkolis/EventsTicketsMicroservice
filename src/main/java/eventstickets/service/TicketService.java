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

    public Ticket getTicket(long id) {
        return ticketRepository.findTicketById(id);
    }

    public void addTickets(List<Ticket> tickets) {
        for (Ticket t : tickets) {
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

    public List<Ticket> showTicketsForEvent(int eventID) {
        return ticketRepository.findAllByEventID(eventID);
    }

    public List<Ticket> showAvailableTickets(int eventID) {
        return ticketRepository.findAllByEventIDAndStatus(eventID, Constants.TICKET_AVAILABLE);
    }

}
