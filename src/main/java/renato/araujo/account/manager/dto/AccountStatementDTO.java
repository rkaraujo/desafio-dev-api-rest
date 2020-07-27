package renato.araujo.account.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountStatementDTO {

    private Integer idConta;
    private LocalDate start;
    private LocalDate end;
    private Integer page;
    private List<TransacaoDTO> transactions;

}
