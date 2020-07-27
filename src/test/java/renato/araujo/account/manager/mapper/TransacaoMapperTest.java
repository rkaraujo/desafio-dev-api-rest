package renato.araujo.account.manager.mapper;

import org.junit.jupiter.api.Test;
import renato.araujo.account.manager.dto.TransacaoDTO;
import renato.araujo.account.manager.model.Transacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TransacaoMapperTest {

    @Test
    void testFromTransacaoList() {
        List<Transacao> transacoes = List.of(
                new Transacao(1, 123, new BigDecimal("1300.00"), LocalDateTime.of(2020, 7, 10, 15, 0)),
                new Transacao(2, 123, new BigDecimal("-50.00"), LocalDateTime.of(2020, 7, 11, 12, 0)),
                new Transacao(3, 123, new BigDecimal("-30.00"), LocalDateTime.of(2020, 7, 15, 11, 0))
        );

        List<TransacaoDTO> dtos = TransacaoMapper.INSTANCE.fromTransacaoList(transacoes);

        assertThat(dtos).hasSize(3);
        for (int i = 0; i < dtos.size(); i++) {
            assertThat(dtos.get(i))
                    .as("DTO " + i)
                    .usingRecursiveComparison()
                    .isEqualTo(transacoes.get(i));
        }
    }
}
