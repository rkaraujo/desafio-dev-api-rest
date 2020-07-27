package renato.araujo.account.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContaDTO {

    private Integer idConta;
    private Integer idPessoa;
    private BigDecimal saldo;
    private BigDecimal limiteSaqueDiario;
    private boolean flagAtivo;
    private Integer tipoConta;
    private LocalDateTime dataCriacao;

}
