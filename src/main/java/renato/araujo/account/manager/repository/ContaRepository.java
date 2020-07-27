package renato.araujo.account.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import renato.araujo.account.manager.model.Conta;

public interface ContaRepository extends JpaRepository<Conta, Integer> {
}
