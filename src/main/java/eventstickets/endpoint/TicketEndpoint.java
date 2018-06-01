package eventstickets.endpoint;

import eventstickets.domain.Ticket;
import eventstickets.domain.Constants;
import eventstickets.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/tickets")
public class TicketEndpoint {


    @Autowired
    TicketService ticketService;

    //FU6 - Sprawdzanie dostępności biletów na dane wydarzenie
    @RequestMapping(method = RequestMethod.GET,
            value = "/event/{eventid}/available",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonErrorResponses showAvailableTickets(@PathVariable int eventid) {
        if (ticketService.doesEventExist(eventid)) {
            List<Ticket> availableTickets = ticketService.showAvailableTickets(eventid);
            return new JsonErrorResponses<>(200, availableTickets, true, 100, "Available tickets",
                    "Available tickets for event " + eventid, Constants.URL);
        }
        return new JsonErrorResponses<>(HttpStatus.NOT_FOUND.value(), new ArrayList<Ticket>(), false, 101,
                "No such event", "Event with id " + eventid + " doesn't exist", Constants.URL);
    }

    //FU7 - Rezerwacja biletów
    //FU9 - Anulowanie rezerwacji zamówień
    @RequestMapping(method = RequestMethod.POST,
            value = "/status/",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                    MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonErrorResponses updateTicketStatus(@RequestParam long ticketid, @RequestParam String status) {
        Ticket ticket = ticketService.getTicket(ticketid);
        if (ticket == null) {
            return new JsonErrorResponses<>(404, "", false, 105, "Ticket not found",
                    "Ticket with id " + ticketid + " does not exist", Constants.URL);
        } else if (ticket.getStatus().equals(status)) {
            switch (status) {
                case Constants.TICKET_AVAILABLE:
                    return new JsonErrorResponses<>(400, "", false, 110, "Ticket already free",
                            "This ticket was available before", Constants.URL);
                case Constants.TICKET_OCCUPIED:
                    return new JsonErrorResponses<>(400, "", false, 109, "Ticket already occupied",
                            "This ticket was occupied before", Constants.URL);
            }
        } else if (ticket.getStatus().equals(Constants.TICKET_CANCELED)) {
            return new JsonErrorResponses<>(400, "", false, 111, "Ticket cancelled",
                    "This ticket was cancelled", Constants.URL);

        }

        ticket.setStatus(status);
        ticketService.updateTicket(ticket);
        return new JsonErrorResponses<>(200, "", true, 100, "Ticket's status changed",
                "Ticket's status changed to " + status, Constants.URL);
    }

    @RequestMapping(method = RequestMethod.GET,
            value = "/event/{eventid}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonErrorResponses showTicketsForEvent(@PathVariable long eventid) {
        if (ticketService.doesEventExist(eventid)) {
            List<Ticket> tickets = ticketService.showTicketsForEvent(eventid);
            return new JsonErrorResponses<>(200, tickets, true, 100, "Tickets",
                    "Tickets for event " + eventid, Constants.URL);
        }
        return new JsonErrorResponses<>(HttpStatus.NOT_FOUND.value(), new ArrayList<Ticket>(), false, 101,
                "No such event", "Event with id " + eventid + " doesn't exist", Constants.URL);
    }

    //FU11 - Anulowanie wydarzeń
    @RequestMapping(method = RequestMethod.DELETE,
            value = "/event/{eventid}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonErrorResponses cancelTicketForEvent(@PathVariable int eventid) {
        if (!ticketService.doesEventExist(eventid)) {
            return new JsonErrorResponses<>(HttpStatus.NOT_FOUND.value(), "", false, 101,
                    "No such event", "Event with id " + eventid + " doesn't exist", Constants.URL);
        }
        ticketService.cancelTicketsForEvent(eventid);
        return new JsonErrorResponses<>(HttpStatus.OK.value(), "", true, 100,
                "Tickets cancelled", "Tickets for event " + eventid + " have been cancelled", Constants.URL);
    }

    //FU12 - Dodawanie dodatkowych biletów do wydarzeń
    @RequestMapping(method = RequestMethod.POST,
            value = "add/event/{eventid}",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                    MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE) //TODO co tutaj bedzie w req?
    public JsonErrorResponses addMoreTickets(@PathVariable long eventid, @RequestParam int regular, @RequestParam int premium) {
        if (!ticketService.doesEventExist(eventid)) {
            return new JsonErrorResponses<>(HttpStatus.NOT_FOUND.value(), "", false, 101,
                    "No such event", "Event with id " + eventid + " doesn't exist", Constants.URL);
        }
        ticketService.addTicketsToEvent(eventid, regular, premium);
//        ticketService.addTickets(tickets);
        return new JsonErrorResponses<>(HttpStatus.OK.value(), "", true, 100,
                "Tickets added", "Tickets for event " + eventid + " have been added", Constants.URL);
    }

    @RequestMapping(method = RequestMethod.POST,
            value = "/",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                    MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonErrorResponses addTicket(Ticket ticket) {
        long eventid = ticket.getEventID();
        if (!ticketService.doesEventExist(eventid)) {
            return new JsonErrorResponses<>(HttpStatus.NOT_FOUND.value(), "", false, 101,
                    "No such event", "Event with id " + eventid + " doesn't exist", Constants.URL);
        }
        ticketService.addTicket(ticket);
        return new JsonErrorResponses<>(HttpStatus.OK.value(), "", true, 100,
                "Ticket added", "Ticket  with id " + ticket.getId() + " has been added ", Constants.URL);
    }

//    @PostMapping(path = "/addTicketsOfType",
//            produces=MediaType.APPLICATION_JSON_VALUE)
//    public Response addTicketsOfType (@RequestParam long eventID, @RequestParam int number, @RequestParam String type) {
//        ticketService.addTicketsOfType(eventID, number, type);
//        return Response.trueStatus();
//    }

    @RequestMapping(method = RequestMethod.GET,
            value = "/{ticketid}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonErrorResponses showTicket(@PathVariable long ticketid) {
        Ticket ticket = ticketService.getTicket(ticketid);
        if (ticket == null) {
            return new JsonErrorResponses<>(HttpStatus.NOT_FOUND.value(), "", false, 105,
                    "No such ticket", "Ticket with id " + ticketid + " doesn't exist", Constants.URL);
        }
        return new JsonErrorResponses<>(HttpStatus.OK.value(), ticket, true, 100,
                "Ticket", "Ticket with id " + ticketid, Constants.URL);
    }

//    @GetMapping(path = "/test",
//            produces=MediaType.APPLICATION_JSON_VALUE)
//    public String getGreetings() {
//        return "hello";
//    }
}
