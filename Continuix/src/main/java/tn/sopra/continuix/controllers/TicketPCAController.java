package tn.sopra.continuix.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.sopra.continuix.entities.TicketPCA;
import tn.sopra.continuix.services.TicketPCAServiceIMP;

@RestController
@RequestMapping
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TicketPCAController {
    @Autowired
    private TicketPCAServiceIMP ticketPCAServiceIMP;
    @PostMapping("/ticket")
    public TicketPCA createTicket(@RequestBody TicketPCA ticketPCA) {
        return ticketPCAServiceIMP.createTicketPCA(ticketPCA);
 }
}

