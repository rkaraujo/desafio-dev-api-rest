package renato.araujo.account.manager.util.builder;

import renato.araujo.account.manager.dto.operations.CreateContaDTO;

import java.math.BigDecimal;

public final class CreateContaDTOBuilder {

    public static CreateContaDTO instance() {
        Integer idPessoa = 1;
        BigDecimal limiteSaqueDiario = new BigDecimal("500.00");
        boolean flagAtivo = true;
        Integer tipoConta = 2;

        return new CreateContaDTO(idPessoa, limiteSaqueDiario, flagAtivo, tipoConta);
    }

}
