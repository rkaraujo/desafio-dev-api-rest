package renato.araujo.account.manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "conta")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conta")
    private Integer idConta;

    @Column(name = "id_pessoa")
    @NotNull
    private Integer idPessoa;

    @Column(name = "saldo")
    @NotNull
    private BigDecimal saldo;

    @Column(name = "limite_saque_diario")
    @NotNull
    @PositiveOrZero
    private BigDecimal limiteSaqueDiario;

    @Column(name = "flag_ativo")
    private boolean flagAtivo;

    @Column(name = "tipo_conta")
    @NotNull
    private Integer tipoConta;

    @Column(name = "data_criacao")
    @NotNull
    private LocalDateTime dataCriacao;

    public void deposit(BigDecimal valor) {
        this.saldo = this.saldo.add(valor);
    }

    public void withdraw(BigDecimal valor) {
        this.saldo = this.saldo.subtract(valor);
    }
}
