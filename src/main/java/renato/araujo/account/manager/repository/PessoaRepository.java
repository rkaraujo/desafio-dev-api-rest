package renato.araujo.account.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import renato.araujo.account.manager.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Integer>  {
}
