package renato.araujo.account.manager.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import renato.araujo.account.manager.model.Conta;
import renato.araujo.account.manager.util.builder.ContaBuilder;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ContaRepositoryIntegrationTest {

    @Autowired
    private ContaRepository contaRepository;

    @Test
    void shouldSaveAndFind() {
        Conta novaConta = ContaBuilder.newConta();

        contaRepository.save(novaConta);

        List<Conta> savedContas = contaRepository.findAll();
        assertSavedConta(savedContas.get(0), novaConta);
    }

    @Test
    void shouldNotSaveNullPerson() {
        Conta novaConta = ContaBuilder.newConta();
        novaConta.setIdPessoa(null);

        assertThatThrownBy(() -> contaRepository.save(novaConta)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldNotSaveNullSaldo() {
        Conta novaConta = ContaBuilder.newConta();
        novaConta.setSaldo(null);

        assertThatThrownBy(() -> contaRepository.save(novaConta)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldNotSaveNullLimiteSaqueDiario() {
        Conta novaConta = ContaBuilder.newConta();
        novaConta.setLimiteSaqueDiario(null);

        assertThatThrownBy(() -> contaRepository.save(novaConta)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldNotSaveNegativeLimiteSaqueDiario() {
        Conta novaConta = ContaBuilder.newConta();
        novaConta.setLimiteSaqueDiario(new BigDecimal("-1000.00"));

        assertThatThrownBy(() -> contaRepository.save(novaConta)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldNotSaveNullTipoConta() {
        Conta novaConta = ContaBuilder.newConta();
        novaConta.setTipoConta(null);

        assertThatThrownBy(() -> contaRepository.save(novaConta)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldNotSaveNullDataCriacao() {
        Conta novaConta = ContaBuilder.newConta();
        novaConta.setDataCriacao(null);

        assertThatThrownBy(() -> contaRepository.save(novaConta)).isInstanceOf(ConstraintViolationException.class);
    }

    private void assertSavedConta(Conta savedConta, Conta novaConta) {
        assertThat(savedConta.getIdPessoa()).isEqualTo(novaConta.getIdPessoa());
        assertThat(savedConta.getSaldo()).isEqualByComparingTo(novaConta.getSaldo());
        assertThat(savedConta.getLimiteSaqueDiario()).isEqualTo(novaConta.getLimiteSaqueDiario());
        assertThat(savedConta.isFlagAtivo()).isEqualTo(novaConta.isFlagAtivo());
        assertThat(savedConta.getTipoConta()).isEqualTo(novaConta.getTipoConta());
        assertThat(savedConta.getDataCriacao()).isEqualTo(novaConta.getDataCriacao());
    }

}
