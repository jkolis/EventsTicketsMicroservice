package microservice.endpoint;

import microservice.domain.Ticket;
import microservice.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/tickets")
public class TicketEndpoint {

    @Autowired
    TicketService ticketService;

    //FU5 - Wyszukiwanie wydarzeń
    @PostMapping(path = "/showEvents")
    public void showEvents() {
        //TODO
    }

    //FU6 - Sprawdzanie dostępności biletów na dane wydarzenie
    @GetMapping(path = "/showAvailableTickets")
    public void showAvailableTickets(@RequestParam int eventID) {
        ticketService.showAvailableTickets(eventID);
    }

    //FU7 - Rezerwacja biletów
    //FU9 - Anulowanie rezerwacji zamówień
    @PostMapping(path = "/updateTicketStatus")
    public String updateTicketStatus(@RequestParam long id, @RequestParam String status) {
        Ticket ticket = ticketService.getTicket(id);
        if (ticket != null) { //TODO wyjatek
            ticket.setStatus(status);
            ticketService.updateTicket(ticket);
            return "updated"; //TODO
        }
        return "problem"; //TODO
    }

    //FU11 - Anulowanie wydarzeń
    @PostMapping(path = "/updateTicketStatusForEvent")
    public String updateTicketStatusForEvent(@RequestParam int id) {
        List<Ticket> ticketsToUpdate = ticketService.showTicketsForEvent(id);
        for (Ticket t : ticketsToUpdate) {
            t.setStatus("BLOCKED"); //TODO
            ticketService.updateTicket(t);
        }
        return "ok"; //TODO
    }

    //FU12 - Dodawanie dodatkowych biletów do wydarzeń
    @PostMapping(path = "/addMoreTickets") //co tutaj bedzie w req?
    public String addMoreTickets(@RequestBody List<Ticket> tickets) {
        ticketService.addTickets(tickets);
        return "added"; //TODO
    }

    @PostMapping(path = "/addTicket")
    public String addTicket(@RequestBody Ticket ticket) {
        ticketService.addTicket(ticket);
        return "added"; //TODO
    }

    @GetMapping(path = "/getTicket")
    public Ticket showTicket(@RequestParam long id) {
        return ticketService.getTicket(id);
    }

}
