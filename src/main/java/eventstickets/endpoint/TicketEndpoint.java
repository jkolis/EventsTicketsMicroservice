package eventstickets.endpoint;

import eventstickets.domain.Ticket;
import eventstickets.domain.Constants;
import eventstickets.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/tickets")
public class TicketEndpoint {


    @Autowired
    TicketService ticketService;

    //FU6 - Sprawdzanie dostępności biletów na dane wydarzenie
    @GetMapping(path = "/event/{eventid}/available",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Ticket> showAvailableTickets(@PathVariable int eventid) {
        return ticketService.showAvailableTickets(eventid);
    }

    //FU7 - Rezerwacja biletów
    //FU9 - Anulowanie rezerwacji zamówień
    @RequestMapping(method = RequestMethod.POST, value = "/status/",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public Response updateTicketStatus(@RequestParam long ticketid, @RequestParam String status) {
        Ticket ticket = ticketService.getTicket(ticketid);
        if (ticket != null) {
            ticket.setStatus(status);
            ticketService.updateTicket(ticket);
            return Response.trueStatus();
        }
        return Response.falseStatus();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/event/{eventid}",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Ticket> showTicketsForEvent(@PathVariable long eventid) {
        return ticketService.showTicketsForEvent(eventid);
    }

    //FU11 - Anulowanie wydarzeń
    @DeleteMapping(path = "/event/{eventid}",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public Response cancelTicketForEvent(@PathVariable int eventid) {
        List<Ticket> ticketsToUpdate = ticketService.showTicketsForEvent(eventid);
        for (Ticket t : ticketsToUpdate) {
            t.setStatus(Constants.TICKET_CANCELED);
            ticketService.updateTicket(t);
        }
        return Response.trueStatus();
    }

    //FU12 - Dodawanie dodatkowych biletów do wydarzeń
    @PostMapping(path = "add/event/{eventid}",
            produces=MediaType.APPLICATION_JSON_VALUE) //TODO co tutaj bedzie w req?
    public Response addMoreTickets(@PathVariable long eventid, @RequestParam int regular, @RequestParam int premium) {
//        ticketService.addTickets(tickets);
        return Response.trueStatus();
    }

    @PostMapping(path = "/",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public Response addTicket(@RequestBody Ticket ticket) {
        ticketService.addTicket(ticket);
        return Response.trueStatus();
    }

//    @PostMapping(path = "/addTicketsOfType",
//            produces=MediaType.APPLICATION_JSON_VALUE)
//    public Response addTicketsOfType (@RequestParam long eventID, @RequestParam int number, @RequestParam String type) {
//        ticketService.addTicketsOfType(eventID, number, type);
//        return Response.trueStatus();
//    }

    @GetMapping(path = "/{ticketid}",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public Ticket showTicket(@PathVariable long ticketid) {
        return ticketService.getTicket(ticketid);
    }

//    @GetMapping(path = "/test",
//            produces=MediaType.APPLICATION_JSON_VALUE)
//    public String getGreetings() {
//        return "hello";
//    }
}
