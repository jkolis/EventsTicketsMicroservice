package eventstickets.dao;

import eventstickets.domain.Ticket;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TicketRepository extends CrudRepository<Ticket, Long> {

    List<Ticket> findAll();
    Ticket findTicketById(long id);
    List<Ticket> findAllByEventID(long eventID);
    List<Ticket> findAllByEventIDAndStatus(long eventID, String status);
}
