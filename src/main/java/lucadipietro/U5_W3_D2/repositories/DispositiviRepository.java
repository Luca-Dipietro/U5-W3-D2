package lucadipietro.U5_W3_D2.repositories;

import lucadipietro.U5_W3_D2.entities.Dispositivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DispositiviRepository extends JpaRepository<Dispositivo, UUID> {
}
