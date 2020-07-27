package renato.araujo.account.manager.util.builder;

import renato.araujo.account.manager.model.Transacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public final class TransacaoBuilder {

    public static Transacao newTransacao() {
        Integer idTransacao = null;
        Integer idConta = 2;
        BigDecimal valor = new BigDecimal("1.99");
        LocalDateTime dataTransacao = LocalDateTime.of(2020, 8, 1, 19, 0);

        return new Transacao(idTransacao, idConta, valor, dataTransacao);
    }

    public static List<Transacao> transacaoList() {
        return List.of(
                new Transacao(1, 123, new BigDecimal("1300.00"), LocalDateTime.of(2020, 7, 10, 15, 0, 1)),
                new Transacao(2, 123, new BigDecimal("-50.00"), LocalDateTime.of(2020, 7, 11, 12, 0, 2))
        );
    }

}
