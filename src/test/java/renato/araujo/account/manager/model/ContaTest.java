package renato.araujo.account.manager.model;

import org.junit.jupiter.api.Test;
import renato.araujo.account.manager.util.builder.ContaBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ContaTest {

    @Test
    void testConta() {
        Integer idConta = 123;
        Integer idPessoa = 456;
        BigDecimal saldo = new BigDecimal("1099.99");
        BigDecimal limiteSaqueDiario = new BigDecimal("1000.00");
        boolean flagAtivo = true;
        Integer tipoConta = 1;
        LocalDateTime dataCriacao = LocalDateTime.now();

        Conta conta = new Conta(idConta, idPessoa, saldo, limiteSaqueDiario, flagAtivo, tipoConta, dataCriacao);

        assertThat(conta.getIdConta()).isEqualTo(idConta);
        assertThat(conta.getIdPessoa()).isEqualTo(idPessoa);
        assertThat(conta.getSaldo()).isEqualTo(saldo);
        assertThat(conta.getLimiteSaqueDiario()).isEqualTo(limiteSaqueDiario);
        assertThat(conta.isFlagAtivo()).isEqualTo(flagAtivo);
        assertThat(conta.getTipoConta()).isEqualTo(tipoConta);
        assertThat(conta.getDataCriacao()).isEqualTo(dataCriacao);
    }

    @Test
    void testDeposit() {
        Conta conta = ContaBuilder.newConta();
        conta.setSaldo(new BigDecimal("11.00"));

        conta.deposit(new BigDecimal("4.00"));

        assertThat(conta.getSaldo()).isEqualTo(new BigDecimal("15.00"));
    }

    @Test
    void testWithdraw() {
        Conta conta = ContaBuilder.newConta();
        conta.setSaldo(new BigDecimal("11.00"));

        conta.withdraw(new BigDecimal("4.00"));

        assertThat(conta.getSaldo()).isEqualTo(new BigDecimal("7.00"));
    }
}
