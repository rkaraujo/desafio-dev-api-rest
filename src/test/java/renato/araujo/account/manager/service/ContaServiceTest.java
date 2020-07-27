package renato.araujo.account.manager.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import renato.araujo.account.manager.dto.operations.CreateContaDTO;
import renato.araujo.account.manager.exception.ContaInactiveException;
import renato.araujo.account.manager.exception.ContaNotFoundException;
import renato.araujo.account.manager.exception.InvalidPessoaException;
import renato.araujo.account.manager.model.Conta;
import renato.araujo.account.manager.repository.ContaRepository;
import renato.araujo.account.manager.repository.PessoaRepository;
import renato.araujo.account.manager.util.builder.ContaBuilder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContaServiceTest {

    @InjectMocks
    private ContaService contaService;

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private PessoaRepository pessoaRepository;

    @Captor
    private ArgumentCaptor<Conta> contaCaptor;

    @Test
    void testCreateConta() {
        Integer idPessoa = 1;
        BigDecimal limiteSaqueDiario = new BigDecimal("500.00");
        boolean flagAtivo = true;
        Integer tipoConta = 2;
        CreateContaDTO createContaDTO = new CreateContaDTO(idPessoa, limiteSaqueDiario, flagAtivo, tipoConta);

        when(pessoaRepository.existsById(createContaDTO.getIdPessoa())).thenReturn(true);
        when(contaRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Conta savedConta = contaService.create(createContaDTO);

        verify(contaRepository, times(1)).save(savedConta);
        assertThat(savedConta.getIdPessoa()).isEqualTo(idPessoa);
        assertThat(savedConta.getLimiteSaqueDiario()).isEqualTo(limiteSaqueDiario);
        assertThat(savedConta.isFlagAtivo()).isEqualTo(flagAtivo);
        assertThat(savedConta.getTipoConta()).isEqualTo(tipoConta);
        assertThat(savedConta.getSaldo()).isZero();
        assertThat(savedConta.getDataCriacao()).isNotNull();
    }

    @Test
    void shouldNotCreateConta_IdPessoaNotExists() {
        Integer idPessoaNotExists = 78178178;
        BigDecimal limiteSaqueDiario = new BigDecimal("500.00");
        boolean flagAtivo = true;
        Integer tipoConta = 2;
        CreateContaDTO createContaDTO = new CreateContaDTO(idPessoaNotExists, limiteSaqueDiario, flagAtivo, tipoConta);

        when(pessoaRepository.existsById(idPessoaNotExists)).thenReturn(false);

        assertThatThrownBy(() -> contaService.create(createContaDTO)).isInstanceOf(InvalidPessoaException.class);
    }

    @Test
    void testGetAccountBalance() {
        Conta conta = ContaBuilder.savedConta();

        when(contaRepository.findById(conta.getIdConta())).thenReturn(Optional.of(conta));

        BigDecimal saldo = contaService.getAccountBalance(conta.getIdConta());

        assertThat(saldo).isEqualTo(conta.getSaldo());
    }

    @Test
    void testGetAccountBalance_ContaInactive() {
        Conta conta = ContaBuilder.savedConta();
        conta.setFlagAtivo(false);

        when(contaRepository.findById(conta.getIdConta())).thenReturn(Optional.of(conta));

        assertThatThrownBy(() ->contaService.getAccountBalance(conta.getIdConta())).isInstanceOf(ContaInactiveException.class);
    }

    @Test
    void testGetAccountBalance_ContaNotFound() {
        when(contaRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contaService.getAccountBalance(1111)).isInstanceOf(ContaNotFoundException.class);
    }

    @Test
    void testSaveFlagActive() {
        Conta conta = ContaBuilder.savedConta();
        conta.setFlagAtivo(true);

        when(contaRepository.findById(conta.getIdConta())).thenReturn(Optional.of(conta));

        contaService.saveFlagAtivo(conta.getIdConta(), false);

        verify(contaRepository).save(contaCaptor.capture());
        assertThat(contaCaptor.getValue().isFlagAtivo()).isFalse();
    }

    @Test
    void testSaveFlagActive_ContaNotFound() {
        when(contaRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contaService.saveFlagAtivo(111, true)).isInstanceOf(ContaNotFoundException.class);
    }
}
