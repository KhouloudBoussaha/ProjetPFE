package tn.sopra.continuix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.sopra.continuix.entities.TicketPCA;

@Repository
public interface TicketPCARepository extends JpaRepository<TicketPCA,Long> {
}
