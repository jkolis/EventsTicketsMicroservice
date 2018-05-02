package eventstickets.dao;

import eventstickets.domain.Ticket;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TicketRepository extends CrudRepository<Ticket, Long> {

    List<Ticket> findAll();
    Ticket findTicketById(long id);
    List<Ticket> findAllByEventID(int eventID);
    List<Ticket> findAllByEventIDAndStatus(int eventID, String status);
}
