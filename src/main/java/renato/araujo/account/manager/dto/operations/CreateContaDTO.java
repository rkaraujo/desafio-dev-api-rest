package renato.araujo.account.manager.dto.operations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateContaDTO {

    @NotNull(message = "idPessoa é obrigatório")
    private Integer idPessoa;

    @NotNull(message = "limiteSaqueDiario é obrigatório")
    @PositiveOrZero(message = "limiteSaqueDiario deve ser zero ou positivo")
    private BigDecimal limiteSaqueDiario;

    private boolean flagAtivo;

    @NotNull(message = "tipoConta é obrigatório")
    private Integer tipoConta;

}
