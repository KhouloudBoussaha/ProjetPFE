package tn.sopra.continuix.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.sopra.continuix.entities.TicketPCA;
import tn.sopra.continuix.repositories.TicketPCARepository;
@Transactional
@Service
public class TicketPCAServiceIMP implements TicketPcaService{
    @Autowired
    public TicketPCARepository ticketPCARepository;


    @Override
    public TicketPCA createTicketPCA(TicketPCA ticketPCA) {
        return ticketPCARepository.save(ticketPCA);
    }
}
