package tn.sopra.continuix.services;

import org.springframework.stereotype.Repository;
import tn.sopra.continuix.entities.TicketPCA;
@Repository
public interface TicketPcaService {
    TicketPCA createTicketPCA(TicketPCA ticketPCA);
}
