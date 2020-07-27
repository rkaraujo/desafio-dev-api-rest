package renato.araujo.account.manager.util.builder;

import renato.araujo.account.manager.dto.operations.WithdrawDTO;

import java.math.BigDecimal;

public final class WithdrawDTOBuilder {

    public static WithdrawDTO instance() {
        Integer idConta = 1;
        BigDecimal valor = new BigDecimal("40.00");
        return new WithdrawDTO(idConta, valor);
    }

}
