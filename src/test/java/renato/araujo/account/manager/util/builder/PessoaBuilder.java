package renato.araujo.account.manager.util.builder;

import renato.araujo.account.manager.model.Pessoa;

import java.time.LocalDate;

public final class PessoaBuilder {

    public static Pessoa newPessoa() {
        Integer idPessoa = null;
        String nome = "Maria Souza da Silva";
        String cpf = "751.618.310-57";
        LocalDate dataNascimento = LocalDate.of(1960, 8, 15);

        return new Pessoa(idPessoa, nome, cpf, dataNascimento);
    }

}
