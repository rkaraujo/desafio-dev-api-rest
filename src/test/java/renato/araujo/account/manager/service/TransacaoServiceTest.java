package renato.araujo.account.manager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import renato.araujo.account.manager.dto.operations.DepositDTO;
import renato.araujo.account.manager.dto.operations.WithdrawDTO;
import renato.araujo.account.manager.exception.AccountBalanceInsufficientException;
import renato.araujo.account.manager.exception.ContaInactiveException;
import renato.araujo.account.manager.exception.ContaNotFoundException;
import renato.araujo.account.manager.exception.ValorExceedsLimiteSaqueDiarioException;
import renato.araujo.account.manager.model.Conta;
import renato.araujo.account.manager.model.Transacao;
import renato.araujo.account.manager.repository.ContaRepository;
import renato.araujo.account.manager.repository.TransacaoRepository;
import renato.araujo.account.manager.util.builder.ContaBuilder;
import renato.araujo.account.manager.util.builder.DepositDTOBuilder;
import renato.araujo.account.manager.util.builder.TransacaoBuilder;
import renato.araujo.account.manager.util.builder.WithdrawDTOBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransacaoServiceTest {

    @InjectMocks
    private TransacaoService transacaoService;

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private ContaRepository contaRepository;

    @Captor
    private ArgumentCaptor<Conta> contaCaptor;

    @Captor
    private ArgumentCaptor<Transacao> transacaoCaptor;

    @BeforeEach
    void setUp() {
        transacaoService.defaultPageSize = 30;
    }

    @Test
    void testDeposit() {
        Conta conta = ContaBuilder.savedConta();
        DepositDTO depositDTO = DepositDTOBuilder.instance();
        depositDTO.setIdConta(conta.getIdConta());
        BigDecimal previousSaldo = conta.getSaldo();

        when(contaRepository.findById(depositDTO.getIdConta())).thenReturn(Optional.of(conta));

        transacaoService.deposit(depositDTO);

        verify(transacaoRepository, times(1)).save(transacaoCaptor.capture());
        Transacao savedTransaction = transacaoCaptor.getValue();
        assertThat(savedTransaction.getIdConta()).isEqualTo(depositDTO.getIdConta());
        assertThat(savedTransaction.getValor()).isEqualTo(depositDTO.getValor());
        assertThat(savedTransaction.getDataTransacao()).isNotNull();

        verify(contaRepository, times(1)).save(contaCaptor.capture());
        Conta updatedConta = contaCaptor.getValue();
        assertThat(updatedConta.getSaldo()).isEqualTo(previousSaldo.add(depositDTO.getValor()));
    }

    @Test
    void shouldNotDeposit_ContaNotFound() {
        DepositDTO depositDTO = DepositDTOBuilder.instance();

        when(contaRepository.findById(depositDTO.getIdConta())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transacaoService.deposit(depositDTO)).isInstanceOf(ContaNotFoundException.class);

        verify(transacaoRepository, never()).save(any());
        verify(contaRepository, never()).save(any());
    }

    @Test
    void shouldNotDeposit_ContaInactive() {
        Conta conta = ContaBuilder.savedConta();
        conta.setFlagAtivo(false);
        DepositDTO depositDTO = DepositDTOBuilder.instance();
        depositDTO.setIdConta(conta.getIdConta());

        when(contaRepository.findById(depositDTO.getIdConta())).thenReturn(Optional.of(conta));

        assertThatThrownBy(() -> transacaoService.deposit(depositDTO)).isInstanceOf(ContaInactiveException.class);

        verify(transacaoRepository, never()).save(any());
        verify(contaRepository, never()).save(any());
    }

    @Test
    void testWithdraw() {
        Conta conta = ContaBuilder.savedConta();
        WithdrawDTO withDrawDTO = WithdrawDTOBuilder.instance();
        withDrawDTO.setIdConta(conta.getIdConta());
        BigDecimal previousSaldo = conta.getSaldo();

        when(contaRepository.findById(withDrawDTO.getIdConta())).thenReturn(Optional.of(conta));

        transacaoService.withdraw(withDrawDTO);

        verify(transacaoRepository, times(1)).save(transacaoCaptor.capture());
        Transacao savedTransaction = transacaoCaptor.getValue();
        assertThat(savedTransaction.getIdConta()).isEqualTo(withDrawDTO.getIdConta());
        assertThat(savedTransaction.getValor()).isEqualTo(withDrawDTO.getValor().negate());
        assertThat(savedTransaction.getDataTransacao()).isNotNull();

        verify(contaRepository, times(1)).save(contaCaptor.capture());
        Conta updatedConta = contaCaptor.getValue();
        assertThat(updatedConta.getSaldo()).isEqualTo(previousSaldo.subtract(withDrawDTO.getValor()));
    }

    @Test
    void testWithdraw_SaldoZero() {
        Conta conta = ContaBuilder.savedConta();

        WithdrawDTO withDrawDTO = WithdrawDTOBuilder.instance();
        withDrawDTO.setIdConta(conta.getIdConta());
        withDrawDTO.setValor(conta.getSaldo());

        when(contaRepository.findById(withDrawDTO.getIdConta())).thenReturn(Optional.of(conta));

        transacaoService.withdraw(withDrawDTO);

        verify(transacaoRepository, times(1)).save(transacaoCaptor.capture());
        Transacao savedTransaction = transacaoCaptor.getValue();
        assertThat(savedTransaction.getIdConta()).isEqualTo(withDrawDTO.getIdConta());
        assertThat(savedTransaction.getValor()).isEqualTo(withDrawDTO.getValor().negate());
        assertThat(savedTransaction.getDataTransacao()).isNotNull();

        verify(contaRepository, times(1)).save(contaCaptor.capture());
        Conta updatedConta = contaCaptor.getValue();
        assertThat(updatedConta.getSaldo()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldNotWithdraw_ContaNotFound() {
        WithdrawDTO withDrawDTO = WithdrawDTOBuilder.instance();

        when(contaRepository.findById(withDrawDTO.getIdConta())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transacaoService.withdraw(withDrawDTO)).isInstanceOf(ContaNotFoundException.class);

        verify(transacaoRepository, never()).save(any());
        verify(contaRepository, never()).save(any());
    }

    @Test
    void shouldNotWithdraw_ContaInactive() {
        Conta conta = ContaBuilder.savedConta();
        conta.setFlagAtivo(false);
        WithdrawDTO withDrawDTO = WithdrawDTOBuilder.instance();
        withDrawDTO.setIdConta(conta.getIdConta());

        when(contaRepository.findById(withDrawDTO.getIdConta())).thenReturn(Optional.of(conta));

        assertThatThrownBy(() -> transacaoService.withdraw(withDrawDTO)).isInstanceOf(ContaInactiveException.class);

        verify(transacaoRepository, never()).save(any());
        verify(contaRepository, never()).save(any());
    }

    @Test
    void shouldNotWithdraw_valorBiggerThanSaldo() {
        Conta conta = ContaBuilder.savedConta();

        WithdrawDTO withDrawDTO = WithdrawDTOBuilder.instance();
        withDrawDTO.setValor(conta.getSaldo().add(new BigDecimal("1000.00")));

        when(contaRepository.findById(withDrawDTO.getIdConta())).thenReturn(Optional.of(conta));

        assertThatThrownBy(() -> transacaoService.withdraw(withDrawDTO)).isInstanceOf(AccountBalanceInsufficientException.class);

        verify(transacaoRepository, never()).save(any());
        verify(contaRepository, never()).save(any());
    }

    @Test
    void shouldNotWithdraw_valorExceedsLimiteSaqueDiario() {
        Conta conta = ContaBuilder.savedConta();
        conta.setLimiteSaqueDiario(new BigDecimal("1000.00"));
        conta.setSaldo(new BigDecimal("1000.00"));

        WithdrawDTO withDrawDTO = WithdrawDTOBuilder.instance();
        withDrawDTO.setValor(new BigDecimal("1000.00"));

        Transacao other1 = TransacaoBuilder.newTransacao();
        other1.setValor(new BigDecimal("-100.00"));
        Transacao other2 = TransacaoBuilder.newTransacao();
        other2.setValor(new BigDecimal("-200.00"));
        List<Transacao> todayOtherWithdrawTransactions = List.of(other1, other2);

        when(contaRepository.findById(withDrawDTO.getIdConta())).thenReturn(Optional.of(conta));
        when(transacaoRepository.findByValorLessThanAndDataTransacaoBetween(any(), any(), any())).thenReturn(todayOtherWithdrawTransactions);

        assertThatThrownBy(() -> transacaoService.withdraw(withDrawDTO)).isInstanceOf(ValorExceedsLimiteSaqueDiarioException.class);

        verify(transacaoRepository, never()).save(any());
        verify(contaRepository, never()).save(any());
    }

    @Test
    void testAccountStatement() {
        Integer idConta = 1;
        LocalDate start = LocalDate.of(2020, 7, 1);
        LocalDate end = LocalDate.of(2020, 7, 31);
        Integer page = 0;

        List<Transacao> transacoes = TransacaoBuilder.transacaoList();
        when(transacaoRepository.findByIdContaAndDataTransacaoBetween(eq(idConta), any(), any(), any())).thenReturn(transacoes);

        List<Transacao> result = transacaoService.accountStatement(idConta, start, end, page);

        assertThat(result).isEqualTo(transacoes);
    }
}