package renato.araujo.account.manager.mapper;

import org.junit.jupiter.api.Test;
import renato.araujo.account.manager.dto.ContaDTO;
import renato.araujo.account.manager.model.Conta;
import renato.araujo.account.manager.util.builder.ContaBuilder;

import static org.assertj.core.api.Assertions.assertThat;

class ContaMapperTest {

    @Test
    void testFromConta() {
        Conta conta = ContaBuilder.savedConta();

        ContaDTO contaDTO = ContaMapper.INSTANCE.dtoFromConta(conta);

        assertThat(contaDTO).usingRecursiveComparison().isEqualTo(conta);
    }
}