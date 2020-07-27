package renato.araujo.account.manager.util.builder;

import renato.araujo.account.manager.model.Conta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class ContaBuilder {

    public static Conta newConta() {
        Integer idConta = null;
        Integer idPessoa = 1;
        BigDecimal saldo = BigDecimal.ZERO;
        BigDecimal limiteSaqueDiario = new BigDecimal("1000.00");
        boolean flagAtivo = true;
        Integer tipoConta = 1;
        LocalDateTime dataCriacao = LocalDateTime.of(2020, 7, 27, 11, 0, 0);

        return new Conta(idConta, idPessoa, saldo, limiteSaqueDiario, flagAtivo, tipoConta, dataCriacao);
    }

    public static Conta savedConta() {
        Conta conta = newConta();
        conta.setIdConta(123);
        conta.setSaldo(new BigDecimal("543.21"));
        return conta;
    }

}
