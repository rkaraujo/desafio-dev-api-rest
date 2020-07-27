package renato.araujo.account.manager.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import renato.araujo.account.manager.dto.operations.CreateContaDTO;
import renato.araujo.account.manager.exception.ContaInactiveException;
import renato.araujo.account.manager.exception.ContaNotFoundException;
import renato.araujo.account.manager.exception.InvalidPessoaException;
import renato.araujo.account.manager.model.Conta;
import renato.araujo.account.manager.repository.ContaRepository;
import renato.araujo.account.manager.repository.PessoaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final PessoaRepository pessoaRepository;

    public ContaService(ContaRepository contaRepository, PessoaRepository pessoaRepository) {
        this.contaRepository = contaRepository;
        this.pessoaRepository = pessoaRepository;
    }

    @Transactional
    public Conta create(CreateContaDTO createContaDTO) {
        if (!pessoaRepository.existsById(createContaDTO.getIdPessoa())) {
            throw new InvalidPessoaException();
        }

        Conta conta = buildNewConta(createContaDTO);
        return contaRepository.save(conta);
    }

    private Conta buildNewConta(CreateContaDTO createContaDTO) {
        Conta conta = new Conta();
        conta.setIdPessoa(createContaDTO.getIdPessoa());
        conta.setLimiteSaqueDiario(createContaDTO.getLimiteSaqueDiario());
        conta.setFlagAtivo(createContaDTO.isFlagAtivo());
        conta.setTipoConta(createContaDTO.getTipoConta());
        conta.setSaldo(BigDecimal.ZERO);
        conta.setDataCriacao(LocalDateTime.now());
        return conta;
    }

    @Transactional
    public BigDecimal getAccountBalance(Integer idConta) {
        Conta conta = contaRepository.findById(idConta).orElseThrow(ContaNotFoundException::new);
        if (!conta.isFlagAtivo()) {
            throw new ContaInactiveException();
        }
        return conta.getSaldo();
    }

    @Transactional
    public void saveFlagAtivo(Integer idConta, boolean ativo) {
        Conta conta = contaRepository.findById(idConta).orElseThrow(ContaNotFoundException::new);
        conta.setFlagAtivo(ativo);
        contaRepository.save(conta);
    }
}
