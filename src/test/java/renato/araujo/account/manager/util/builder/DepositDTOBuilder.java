package renato.araujo.account.manager.util.builder;

import renato.araujo.account.manager.dto.operations.DepositDTO;

import java.math.BigDecimal;

public final class DepositDTOBuilder {

    public static DepositDTO instance() {
        Integer idConta = 1;
        BigDecimal valor = new BigDecimal("20.00");
        return new DepositDTO(idConta, valor);
    }
}
