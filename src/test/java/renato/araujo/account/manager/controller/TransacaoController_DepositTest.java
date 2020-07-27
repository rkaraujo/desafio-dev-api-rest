package renato.araujo.account.manager.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import renato.araujo.account.manager.dto.operations.DepositDTO;
import renato.araujo.account.manager.exception.ContaNotFoundException;
import renato.araujo.account.manager.service.TransacaoService;
import renato.araujo.account.manager.util.builder.DepositDTOBuilder;
import renato.araujo.account.manager.util.builder.TestUtil;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransacaoController.class)
public class TransacaoController_DepositTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransacaoService transacaoService;

    @Test
    void testDeposit() throws Exception {
        DepositDTO depositDTO = DepositDTOBuilder.instance();
        String jsonDeposit = TestUtil.toJson(depositDTO);

        mockMvc.perform(post("/api/v1/transacoes/depositar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDeposit))
                .andExpect(status().isOk());

        verify(transacaoService, times(1)).deposit(depositDTO);
    }

    @Test
    void shouldNotDeposit_IdContaNull() throws Exception {
        DepositDTO depositDTO = DepositDTOBuilder.instance();
        depositDTO.setIdConta(null);

        String jsonDeposit = TestUtil.toJson(depositDTO);

        mockMvc.perform(post("/api/v1/transacoes/depositar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDeposit))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").value(containsString("idConta")));
    }

    @Test
    void shouldNotDeposit_ValorNull() throws Exception {
        DepositDTO depositDTO = DepositDTOBuilder.instance();
        depositDTO.setValor(null);

        String jsonDeposit = TestUtil.toJson(depositDTO);

        mockMvc.perform(post("/api/v1/transacoes/depositar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDeposit))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").value(containsString("valor")));
    }

    @Test
    void shouldNotDeposit_ValorNegative() throws Exception {
        DepositDTO depositDTO = DepositDTOBuilder.instance();
        depositDTO.setValor(new BigDecimal("-100.00"));

        String jsonDeposit = TestUtil.toJson(depositDTO);

        mockMvc.perform(post("/api/v1/transacoes/depositar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDeposit))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").value("valor deve ser positivo"));
    }

    @Test
    void shouldNotDeposit_ValorZero() throws Exception {
        DepositDTO depositDTO = DepositDTOBuilder.instance();
        depositDTO.setValor(BigDecimal.ZERO);

        String jsonDeposit = TestUtil.toJson(depositDTO);

        mockMvc.perform(post("/api/v1/transacoes/depositar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDeposit))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").value("valor deve ser positivo"));
    }

    @Test
    void shouldNotDeposit_ContaNotFound() throws Exception {
        DepositDTO depositDTO = DepositDTOBuilder.instance();
        String jsonDeposit = TestUtil.toJson(depositDTO);

        doThrow(new ContaNotFoundException()).when(transacaoService).deposit(any());

        mockMvc.perform(post("/api/v1/transacoes/depositar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDeposit))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messages[0]").value("Conta nao encontrada"));
    }
}
