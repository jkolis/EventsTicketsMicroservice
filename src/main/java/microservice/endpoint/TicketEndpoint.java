package microservice.endpoint;

import microservice.domain.Ticket;
import microservice.domain.Constants;
import microservice.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/tickets")
public class TicketEndpoint {


    @Autowired
    TicketService ticketService;

    //FU5 - Wyszukiwanie wydarzeń
    @PostMapping(path = "/showEvents",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public void showEvents() {
        //TODO
    }

    //FU6 - Sprawdzanie dostępności biletów na dane wydarzenie
    @GetMapping(path = "/showAvailableTickets",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Ticket> showAvailableTickets(@RequestParam int eventID) {
        return ticketService.showAvailableTickets(eventID);
    }

    //FU7 - Rezerwacja biletów
    //FU9 - Anulowanie rezerwacji zamówień
    @PostMapping(path = "/updateTicketStatus",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public Response updateTicketStatus(@RequestParam long id, @RequestParam String status) {
        Ticket ticket = ticketService.getTicket(id);
        if (ticket != null) {
            ticket.setStatus(status);
            ticketService.updateTicket(ticket);
            return Response.trueStatus();
        }
        return Response.falseStatus();
    }

    //FU11 - Anulowanie wydarzeń
    @PostMapping(path = "/updateTicketStatusForEvent",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public Response updateTicketStatusForEvent(@RequestParam int id) {
        List<Ticket> ticketsToUpdate = ticketService.showTicketsForEvent(id);
        for (Ticket t : ticketsToUpdate) {
            t.setStatus(Constants.TICKET_CANCELED);
            ticketService.updateTicket(t);
        }
        return Response.trueStatus();
    }

    //FU12 - Dodawanie dodatkowych biletów do wydarzeń
    @PostMapping(path = "/addMoreTickets",
            produces=MediaType.APPLICATION_JSON_VALUE) //TODO co tutaj bedzie w req?
    public Response addMoreTickets(@RequestBody List<Ticket> tickets) {
        ticketService.addTickets(tickets);
        return Response.trueStatus();
    }

    @PostMapping(path = "/addTicket",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public Response addTicket(@RequestBody Ticket ticket) {
        ticketService.addTicket(ticket);
        return Response.trueStatus();
    }

    @PostMapping(path = "/addTicketsOfType",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public Response addTicketsOfType (@RequestParam long eventID, @RequestParam int number, @RequestParam String type) {
        ticketService.addTicketsOfType(eventID, number, type);
        return Response.trueStatus();
    }

    @GetMapping(path = "/getTicket",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public Ticket showTicket(@RequestParam long id) {
        return ticketService.getTicket(id);
    }

//    @GetMapping(path = "/test",
//            produces=MediaType.APPLICATION_JSON_VALUE)
//    public String getGreetings() {
//        return "hello";
//    }
}
