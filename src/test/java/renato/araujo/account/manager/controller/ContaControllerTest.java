package renato.araujo.account.manager.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import renato.araujo.account.manager.exception.ContaInactiveException;
import renato.araujo.account.manager.exception.ContaNotFoundException;
import renato.araujo.account.manager.model.Conta;
import renato.araujo.account.manager.model.Transacao;
import renato.araujo.account.manager.service.ContaService;
import renato.araujo.account.manager.service.TransacaoService;
import renato.araujo.account.manager.util.builder.ContaBuilder;
import renato.araujo.account.manager.util.builder.TransacaoBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContaController.class)
public class ContaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContaService contaService;

    @MockBean
    private TransacaoService transacaoService;

    @Test
    void testAccountBalance() throws Exception {
        Conta conta = ContaBuilder.savedConta();

        when(contaService.getAccountBalance(conta.getIdConta())).thenReturn(conta.getSaldo());

        mockMvc.perform(get("/api/v1/contas/123/saldo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idConta").value(conta.getIdConta()))
                .andExpect(jsonPath("$.saldo").value(conta.getSaldo()));
    }

    @Test
    void testAccountBalance_ContaNotFound() throws Exception {
        when(contaService.getAccountBalance(any())).thenThrow(new ContaNotFoundException());

        mockMvc.perform(get("/api/v1/contas/111/saldo"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messages[0]").value("Conta nao encontrada"));
    }

    @Test
    void testAccountBalance_ContaInactive() throws Exception {
        when(contaService.getAccountBalance(any())).thenThrow(new ContaInactiveException());

        mockMvc.perform(get("/api/v1/contas/111/saldo"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").value("Conta inativa"));
    }

    @Test
    void testLockAccount() throws Exception {
        mockMvc.perform(patch("/api/v1/contas/123/bloquear"))
                .andExpect(status().isOk());

        verify(contaService).saveFlagAtivo(any(), eq(false));
    }

    @Test
    void testLockAccount_ContaNotFound() throws Exception {
        doThrow(new ContaNotFoundException()).when(contaService).saveFlagAtivo(any(), anyBoolean());

        mockMvc.perform(patch("/api/v1/contas/123/bloquear"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messages[0]").value("Conta nao encontrada"));
    }

    @Test
    void testUnlockAccount() throws Exception {
        mockMvc.perform(patch("/api/v1/contas/123/desbloquear"))
                .andExpect(status().isOk());

        verify(contaService).saveFlagAtivo(any(), eq(true));
    }

    @Test
    void testUnlockAccount_ContaNotFound() throws Exception {
        doThrow(new ContaNotFoundException()).when(contaService).saveFlagAtivo(any(), anyBoolean());

        mockMvc.perform(patch("/api/v1/contas/123/desbloquear"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messages[0]").value("Conta nao encontrada"));
    }

    @Test
    void testAccountStatement() throws Exception {
        List<Transacao> transacoes = TransacaoBuilder.transacaoList();

        when(transacaoService.accountStatement(eq(Integer.valueOf(123)),
                eq(LocalDate.of(2020, 7, 1)),
                eq(LocalDate.of(2020, 7, 31)),
                any(Integer.class))).thenReturn(transacoes);

        mockMvc.perform(get("/api/v1/contas/123/extrato")
                .param("inicio", "01072020")
                .param("fim", "31072020")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.start").value("2020-07-01"))
                .andExpect(jsonPath("$.end").value("2020-07-31"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.transactions").value(hasSize(transacoes.size())))
                .andExpect(jsonPath("$.transactions[0].idTransacao").value(transacoes.get(0).getIdTransacao()))
                .andExpect(jsonPath("$.transactions[0].idConta").value(transacoes.get(0).getIdConta()))
                .andExpect(jsonPath("$.transactions[0].valor").value(transacoes.get(0).getValor().doubleValue()))
                .andExpect(jsonPath("$.transactions[0].dataTransacao").value(transacoes.get(0).getDataTransacao().toString()))
                .andExpect(jsonPath("$.transactions[1].idTransacao").value(transacoes.get(1).getIdTransacao()))
                .andExpect(jsonPath("$.transactions[1].idConta").value(transacoes.get(1).getIdConta()))
                .andExpect(jsonPath("$.transactions[1].valor").value(transacoes.get(1).getValor().doubleValue()))
                .andExpect(jsonPath("$.transactions[1].dataTransacao").value(transacoes.get(1).getDataTransacao().toString()));
    }
}
