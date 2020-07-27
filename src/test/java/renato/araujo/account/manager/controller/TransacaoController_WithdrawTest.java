package renato.araujo.account.manager.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import renato.araujo.account.manager.dto.operations.WithdrawDTO;
import renato.araujo.account.manager.exception.AccountBalanceInsufficientException;
import renato.araujo.account.manager.exception.ContaNotFoundException;
import renato.araujo.account.manager.exception.ValorExceedsLimiteSaqueDiarioException;
import renato.araujo.account.manager.service.TransacaoService;
import renato.araujo.account.manager.util.builder.TestUtil;
import renato.araujo.account.manager.util.builder.WithdrawDTOBuilder;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransacaoController.class)
public class TransacaoController_WithdrawTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransacaoService transacaoService;

    @Test
    void testWithdraw() throws Exception {
        WithdrawDTO withdrawDTO = WithdrawDTOBuilder.instance();
        String jsonWithdraw = TestUtil.toJson(withdrawDTO);

        mockMvc.perform(post("/api/v1/transacoes/sacar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithdraw))
                .andExpect(status().isOk());

        verify(transacaoService, times(1)).withdraw(withdrawDTO);
    }

    @Test
    void shouldNotWithdraw_IdContaNull() throws Exception {
        WithdrawDTO withdrawDTO = WithdrawDTOBuilder.instance();
        withdrawDTO.setIdConta(null);

        String jsonWithdraw = TestUtil.toJson(withdrawDTO);

        mockMvc.perform(post("/api/v1/transacoes/sacar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithdraw))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").value(containsString("idConta")));
    }

    @Test
    void shouldNotWithdraw_ValorNull() throws Exception {
        WithdrawDTO withdrawDTO = WithdrawDTOBuilder.instance();
        withdrawDTO.setValor(null);

        String jsonWithdraw = TestUtil.toJson(withdrawDTO);

        mockMvc.perform(post("/api/v1/transacoes/sacar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithdraw))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").value(containsString("valor")));
    }

    @Test
    void shouldNotWithdraw_ValorNegative() throws Exception {
        WithdrawDTO withdrawDTO = WithdrawDTOBuilder.instance();
        withdrawDTO.setValor(new BigDecimal("-5.00"));

        String jsonWithdraw = TestUtil.toJson(withdrawDTO);

        mockMvc.perform(post("/api/v1/transacoes/sacar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithdraw))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").value("valor deve ser positivo"));
    }

    @Test
    void shouldNotWithdraw_ValorZero() throws Exception {
        WithdrawDTO withdrawDTO = WithdrawDTOBuilder.instance();
        withdrawDTO.setValor(BigDecimal.ZERO);

        String jsonWithdraw = TestUtil.toJson(withdrawDTO);

        mockMvc.perform(post("/api/v1/transacoes/sacar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithdraw))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").value("valor deve ser positivo"));
    }

    @Test
    void shouldNotWithdraw_ContaNotFound() throws Exception {
        WithdrawDTO withdrawDTO = WithdrawDTOBuilder.instance();
        String jsonWithdraw = TestUtil.toJson(withdrawDTO);

        doThrow(new ContaNotFoundException()).when(transacaoService).withdraw(withdrawDTO);

        mockMvc.perform(post("/api/v1/transacoes/sacar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithdraw))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messages[0]").value("Conta nao encontrada"));
    }

    @Test
    void shouldNotWithdraw_AccountBalanceInsufficient() throws Exception {
        WithdrawDTO withdrawDTO = WithdrawDTOBuilder.instance();
        String jsonWithdraw = TestUtil.toJson(withdrawDTO);

        doThrow(new AccountBalanceInsufficientException()).when(transacaoService).withdraw(withdrawDTO);

        mockMvc.perform(post("/api/v1/transacoes/sacar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithdraw))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").value("Saldo insuficiente"));
    }

    @Test
    void shouldNotWithdraw_ValorExceedsLimiteSaqueDiario() throws Exception {
        WithdrawDTO withdrawDTO = WithdrawDTOBuilder.instance();
        String jsonWithdraw = TestUtil.toJson(withdrawDTO);

        doThrow(new ValorExceedsLimiteSaqueDiarioException()).when(transacaoService).withdraw(withdrawDTO);

        mockMvc.perform(post("/api/v1/transacoes/sacar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithdraw))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").value("Valor do saque excede o limite de saque diario"));
    }
}
