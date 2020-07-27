package renato.araujo.account.manager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;

    private final ContaRepository contaRepository;

    @Value("${app.default.page.size:30}")
    int defaultPageSize;

    public TransacaoService(TransacaoRepository transacaoRepository, ContaRepository contaRepository) {
        this.transacaoRepository = transacaoRepository;
        this.contaRepository = contaRepository;
    }

    @Transactional
    public void deposit(DepositDTO depositDTO) {
        Conta conta = contaRepository.findById(depositDTO.getIdConta())
                .orElseThrow(ContaNotFoundException::new);

        checkAccountActive(conta);

        Transacao transacao = new Transacao();
        transacao.setIdConta(conta.getIdConta());
        transacao.setValor(depositDTO.getValor());
        transacao.setDataTransacao(LocalDateTime.now());
        transacaoRepository.save(transacao);

        conta.deposit(depositDTO.getValor());
        contaRepository.save(conta);
    }

    public void withdraw(WithdrawDTO withdrawDTO) {
        Conta conta = contaRepository.findById(withdrawDTO.getIdConta())
                .orElseThrow(ContaNotFoundException::new);

        checkAccountActive(conta);
        checkAccountBalanceSufficient(conta, withdrawDTO.getValor());
        checkDailyWithdrawLimit(conta, withdrawDTO.getValor());

        Transacao transacao = new Transacao();
        transacao.setIdConta(conta.getIdConta());
        transacao.setValor(withdrawDTO.getValor().negate());
        transacao.setDataTransacao(LocalDateTime.now());
        transacaoRepository.save(transacao);

        conta.withdraw(withdrawDTO.getValor());
        contaRepository.save(conta);
    }

    private void checkAccountActive(Conta conta) {
        if (!conta.isFlagAtivo()) {
            throw new ContaInactiveException();
        }
    }

    private void checkAccountBalanceSufficient(Conta conta, BigDecimal valor) {
        boolean isSaldoLessThanValor = conta.getSaldo().compareTo(valor) < 0;
        if (isSaldoLessThanValor) {
            throw new AccountBalanceInsufficientException();
        }
    }

    private void checkDailyWithdrawLimit(Conta conta, BigDecimal valor) {
        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime todayEnd = today.atTime(LocalTime.MAX);

        // get only withdraw transactions, valor < 0
        List<Transacao> todayWithdrawTransactions = transacaoRepository.findByValorLessThanAndDataTransacaoBetween(BigDecimal.ZERO, todayStart, todayEnd);

        BigDecimal todayPreviousWithdrawValor = todayWithdrawTransactions.stream()
                .map(Transacao::getValor)
                .map(BigDecimal::abs)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        boolean isTotalWithdrawBiggerThanLimiteSaqueDiario = todayPreviousWithdrawValor.add(valor).compareTo(conta.getLimiteSaqueDiario()) > 0;
        if (isTotalWithdrawBiggerThanLimiteSaqueDiario) {
            throw new ValorExceedsLimiteSaqueDiarioException();
        }
    }

    @Transactional
    public List<Transacao> accountStatement(Integer idConta, LocalDate start, LocalDate end, Integer page) {
        PageRequest pageRequest = PageRequest.of(page, defaultPageSize, Sort.Direction.ASC, "dataTransacao");
        LocalDateTime dtStart = start.atStartOfDay();
        LocalDateTime dtEnd = end.atTime(LocalTime.MAX);

        return transacaoRepository.findByIdContaAndDataTransacaoBetween(idConta, dtStart, dtEnd, pageRequest);
    }
}
