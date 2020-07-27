package renato.araujo.account.manager.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import renato.araujo.account.manager.dto.AccountBalanceDTO;
import renato.araujo.account.manager.dto.AccountStatementDTO;
import renato.araujo.account.manager.dto.ContaDTO;
import renato.araujo.account.manager.dto.operations.CreateContaDTO;
import renato.araujo.account.manager.mapper.ContaMapper;
import renato.araujo.account.manager.mapper.TransacaoMapper;
import renato.araujo.account.manager.model.Conta;
import renato.araujo.account.manager.model.Transacao;
import renato.araujo.account.manager.service.ContaService;
import renato.araujo.account.manager.service.TransacaoService;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ContaController {

    private final ContaService contaService;
    private final TransacaoService transacaoService;

    public ContaController(ContaService contaService, TransacaoService transacaoService) {
        this.contaService = contaService;
        this.transacaoService = transacaoService;
    }

    @PostMapping("/v1/contas")
    @ResponseStatus(HttpStatus.CREATED)
    public ContaDTO create(@Valid @RequestBody CreateContaDTO createContaDTO) {
        Conta conta = contaService.create(createContaDTO);
        return ContaMapper.INSTANCE.dtoFromConta(conta);
    }

    @GetMapping("/v1/contas/{idConta}/saldo")
    public AccountBalanceDTO accountBalance(@PathVariable Integer idConta) {
        BigDecimal saldo = contaService.getAccountBalance(idConta);
        return new AccountBalanceDTO(idConta, saldo);
    }

    @PatchMapping("/v1/contas/{idConta}/bloquear")
    public void lock(@PathVariable Integer idConta) {
        contaService.saveFlagAtivo(idConta, false);
    }

    @PatchMapping("/v1/contas/{idConta}/desbloquear")
    public void unlock(@PathVariable Integer idConta) {
        contaService.saveFlagAtivo(idConta, true);
    }

    @GetMapping("/v1/contas/{idConta}/extrato")
    public AccountStatementDTO accountStatement(@PathVariable Integer idConta,
                                                @RequestParam(value = "inicio", required = false)
                                               @DateTimeFormat(pattern = "ddMMyyyy") LocalDate start,
                                                @RequestParam(value = "fim", required = false)
                                               @DateTimeFormat(pattern = "ddMMyyyy") LocalDate end,
                                                @RequestParam(value = "pagina", defaultValue = "0") Integer page) {
        if (start == null) {
            start = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        }
        if (end == null) {
            end = start.with(TemporalAdjusters.lastDayOfMonth());
        }

        List<Transacao> transacoes = transacaoService.accountStatement(idConta, start, end, page);
        return new AccountStatementDTO(idConta,
                start,
                end,
                page,
                TransacaoMapper.INSTANCE.fromTransacaoList(transacoes));
    }
}
