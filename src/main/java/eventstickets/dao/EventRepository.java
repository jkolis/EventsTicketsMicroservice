package eventstickets.dao;

import eventstickets.domain.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {

    List<Event> findAll();
    Event findEventById(long id);
    List<Event> findAllByOrOrganizer(String organizer);
}
