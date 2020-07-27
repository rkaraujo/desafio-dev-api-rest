package renato.araujo.account.manager.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class PessoaTest {

    @Test
    void testPessoa() {
        Integer idPessoa = 45;
        String nome = "Maria Souza da Silva";
        String cpf = "751.618.310-57";
        LocalDate dataNascimento = LocalDate.of(1960, 8, 15);

        Pessoa pessoa = new Pessoa(idPessoa, nome, cpf, dataNascimento);

        assertThat(pessoa.getIdPessoa()).isEqualTo(idPessoa);
        assertThat(pessoa.getNome()).isEqualTo(nome);
        assertThat(pessoa.getCpf()).isEqualTo(cpf);
        assertThat(pessoa.getDataNascimento()).isEqualTo(dataNascimento);
    }
}
