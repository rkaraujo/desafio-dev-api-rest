package renato.araujo.account.manager.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import renato.araujo.account.manager.model.Transacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByValorLessThanAndDataTransacaoBetween(BigDecimal valor, LocalDateTime start, LocalDateTime end);

    List<Transacao> findByIdContaAndDataTransacaoBetween(Integer idConta, LocalDateTime start, LocalDateTime end, Pageable pageable);
}
