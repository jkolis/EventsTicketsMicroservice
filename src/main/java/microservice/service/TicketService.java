package microservice.service;

import microservice.dao.TicketRepository;
import microservice.domain.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    public static final String AVAILABLE = "available"; //TODO przeniesc do osobnej klasy wszystkie stałe

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


    public void updateTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    public List<Ticket> showTicketsForEvent(int eventID) {
        return ticketRepository.findAllByEventID(eventID);
    }

    public List<Ticket> showAvailableTickets(int eventID) {
        return ticketRepository.findAllByEventIDAndStatus(eventID, AVAILABLE);
    }

}
