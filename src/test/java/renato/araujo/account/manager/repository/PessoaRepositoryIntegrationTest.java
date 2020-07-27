package renato.araujo.account.manager.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import renato.araujo.account.manager.model.Pessoa;
import renato.araujo.account.manager.util.builder.PessoaBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class PessoaRepositoryIntegrationTest {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Test
    void shouldSaveAndFind() {
        Pessoa novaPessoa = PessoaBuilder.newPessoa();

        Pessoa savedPessoa = pessoaRepository.save(novaPessoa);

        Pessoa dbPessoa = pessoaRepository.findById(savedPessoa.getIdPessoa()).get();
        assertThat(dbPessoa.getIdPessoa()).isNotNull();
        assertThat(dbPessoa.getNome()).isEqualTo(novaPessoa.getNome());
        assertThat(dbPessoa.getCpf()).isEqualTo(novaPessoa.getCpf());
        assertThat(dbPessoa.getDataNascimento()).isEqualTo(novaPessoa.getDataNascimento());
    }

}
