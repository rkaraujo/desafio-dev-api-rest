package renato.araujo.account.manager.dto.operations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositDTO {

    @NotNull(message = "idConta é obrigatório")
    private Integer idConta;

    @NotNull(message = "valor é obrigatório")
    @Positive(message = "valor deve ser positivo")
    private BigDecimal valor;

}
