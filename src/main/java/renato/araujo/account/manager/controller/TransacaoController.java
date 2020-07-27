package renato.araujo.account.manager.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import renato.araujo.account.manager.dto.operations.DepositDTO;
import renato.araujo.account.manager.dto.operations.WithdrawDTO;
import renato.araujo.account.manager.service.TransacaoService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class TransacaoController {

    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping("/v1/transacoes/depositar")
    public void deposit(@Valid @RequestBody DepositDTO depositDTO) {
        transacaoService.deposit(depositDTO);
    }

    @PostMapping("/v1/transacoes/sacar")
    public void withdraw(@Valid @RequestBody WithdrawDTO withdrawDTO) {
        transacaoService.withdraw(withdrawDTO);
    }

}
