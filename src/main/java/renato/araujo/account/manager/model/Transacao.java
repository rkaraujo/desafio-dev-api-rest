package renato.araujo.account.manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacao")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transacao")
    private Integer idTransacao;

    @Column(name = "id_conta")
    @NotNull
    private Integer idConta;

    @Column(name = "valor")
    @NotNull
    private BigDecimal valor;

    @Column(name = "data_transacao")
    @NotNull
    private LocalDateTime dataTransacao;

}
