package renato.araujo.account.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransacaoDTO {

    private Integer idTransacao;
    private Integer idConta;
    private BigDecimal valor;
    private LocalDateTime dataTransacao;

}
