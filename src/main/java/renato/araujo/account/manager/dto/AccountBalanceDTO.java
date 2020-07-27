package renato.araujo.account.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalanceDTO {

    private Integer idConta;
    private BigDecimal saldo;

}
