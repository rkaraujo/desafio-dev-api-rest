package renato.araujo.account.manager.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class TransacaoTest {

    @Test
    void testTransacao() {
        Integer idTransacao = 1;
        Integer idConta = 2;
        BigDecimal valor = new BigDecimal("1.99");
        LocalDateTime dataTransacao = LocalDateTime.of(2020, 8, 1, 19, 0);
        
        Transacao transacao = new Transacao(idTransacao, idConta, valor, dataTransacao);
        
        assertThat(transacao.getIdTransacao()).isEqualTo(idTransacao);
        assertThat(transacao.getIdConta()).isEqualTo(idConta);
        assertThat(transacao.getValor()).isEqualTo(valor);
        assertThat(transacao.getDataTransacao()).isEqualTo(dataTransacao);
    }
}