package renato.araujo.account.manager.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import renato.araujo.account.manager.model.Conta;
import renato.araujo.account.manager.model.Transacao;
import renato.araujo.account.manager.util.builder.ContaBuilder;
import renato.araujo.account.manager.util.builder.TransacaoBuilder;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class TransacaoRepositoryIntegrationTest {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Test
    void shouldSaveAndFind() {
        Conta conta = insertConta();

        Transacao transacao = TransacaoBuilder.newTransacao();
        transacao.setIdConta(conta.getIdConta());

        transacaoRepository.save(transacao);

        Transacao savedTransacao = transacaoRepository.findAll().get(0);
        assertThat(savedTransacao.getIdTransacao()).isNotNull();
        assertThat(savedTransacao.getIdConta()).isEqualTo(transacao.getIdConta());
        assertThat(savedTransacao.getValor()).isEqualTo(transacao.getValor());
        assertThat(savedTransacao.getDataTransacao()).isEqualTo(transacao.getDataTransacao());
    }

    @Test
    void shouldNotSaveNullConta() {
        Transacao transacao = TransacaoBuilder.newTransacao();
        transacao.setIdConta(null);

        assertThatThrownBy(() -> transacaoRepository.save(transacao)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldNotSaveNullValor() {
        Conta conta = insertConta();

        Transacao transacao = TransacaoBuilder.newTransacao();
        transacao.setIdConta(conta.getIdConta());
        transacao.setValor(null);

        assertThatThrownBy(() -> transacaoRepository.save(transacao)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldNotSaveNullDataTransacao() {
        Conta conta = insertConta();

        Transacao transacao = TransacaoBuilder.newTransacao();
        transacao.setIdConta(conta.getIdConta());
        transacao.setDataTransacao(null);

        assertThatThrownBy(() -> transacaoRepository.save(transacao)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void testFindByValorLessThanAndDataCriacaoBetween() {
        Conta conta = insertConta();

        BigDecimal valorSaque1 = new BigDecimal("-1.00");
        Transacao transacaoSaque1 = TransacaoBuilder.newTransacao();
        transacaoSaque1.setIdConta(conta.getIdConta());
        transacaoSaque1.setValor(valorSaque1);
        transacaoRepository.save(transacaoSaque1);

        BigDecimal valorSaque2 = new BigDecimal("-2.00");
        Transacao transacaoSaque2 = TransacaoBuilder.newTransacao();
        transacaoSaque2.setIdConta(conta.getIdConta());
        transacaoSaque2.setValor(valorSaque2);
        transacaoSaque2.setDataTransacao(transacaoSaque1.getDataTransacao().plusMinutes(1));
        transacaoRepository.save(transacaoSaque2);

        BigDecimal valorDeposito = new BigDecimal("3.00");
        Transacao transacaoDeposito = TransacaoBuilder.newTransacao();
        transacaoDeposito.setIdConta(conta.getIdConta());
        transacaoDeposito.setValor(valorDeposito);
        transacaoDeposito.setDataTransacao(transacaoSaque2.getDataTransacao().plusMinutes(1));
        transacaoRepository.save(transacaoDeposito);

        LocalDate day = transacaoSaque1.getDataTransacao().toLocalDate();
        LocalDateTime dayStart = day.atStartOfDay();
        LocalDateTime dayEnd = day.atTime(LocalTime.MAX);
        List<Transacao> transacoes = transacaoRepository.findByValorLessThanAndDataTransacaoBetween(BigDecimal.ZERO, dayStart, dayEnd);

        assertThat(transacoes).hasSize(2);

        assertThat(transacoes).extracting("valor")
                .containsExactly(valorSaque1, valorSaque2)
                .doesNotContain(valorDeposito);
    }

    @Test
    void testFindByIdContaAndDataTransacaoBetween() {
        Conta conta = insertConta();
        Transacao transacao1 = insertTransacao(conta.getIdConta(),
                new BigDecimal("1300.00"),
                LocalDateTime.of(2020, 7, 10, 12, 13, 14));
        Transacao transacao2 = insertTransacao(conta.getIdConta(),
                new BigDecimal("-300.00"),
                LocalDateTime.of(2020, 7, 12, 8, 9, 10));

        List<Transacao> transacoes = transacaoRepository.findByIdContaAndDataTransacaoBetween(conta.getIdConta(),
                LocalDateTime.of(2020, 7, 1, 0, 0, 0),
                LocalDateTime.of(2020, 7, 31, 23, 59, 59),
                PageRequest.of(0, 30, Sort.Direction.ASC, "dataTransacao"));

        assertThat(transacoes).hasSize(2);
        assertThat(transacoes.get(0)).usingRecursiveComparison().isEqualTo(transacao1);
        assertThat(transacoes.get(1)).usingRecursiveComparison().isEqualTo(transacao2);
    }

    private Conta insertConta() {
        Conta conta = ContaBuilder.newConta();
        return contaRepository.save(conta);
    }

    private Transacao insertTransacao(Integer idConta, BigDecimal valor, LocalDateTime dataTransacao) {
        Transacao transacao = new Transacao(null, idConta, valor, dataTransacao);
        return transacaoRepository.save(transacao);
    }
}