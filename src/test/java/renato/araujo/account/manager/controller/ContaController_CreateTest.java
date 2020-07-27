package renato.araujo.account.manager.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import renato.araujo.account.manager.dto.operations.CreateContaDTO;
import renato.araujo.account.manager.exception.InvalidPessoaException;
import renato.araujo.account.manager.model.Conta;
import renato.araujo.account.manager.service.ContaService;
import renato.araujo.account.manager.service.TransacaoService;
import renato.araujo.account.manager.util.builder.CreateContaDTOBuilder;
import renato.araujo.account.manager.util.builder.TestUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContaController.class)
public class ContaController_CreateTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContaService contaService;

    @MockBean
    private TransacaoService transacaoService;

    @Test
    void testCreate() throws Exception {
        CreateContaDTO createContaDTO = CreateContaDTOBuilder.instance();
        String jsonCreateConta = TestUtil.toJson(createContaDTO);

        int idConta = 1;
        Conta mockSavedConta = new Conta(idConta,
                createContaDTO.getIdPessoa(),
                BigDecimal.ZERO,
                createContaDTO.getLimiteSaqueDiario(),
                createContaDTO.isFlagAtivo(),
                createContaDTO.getTipoConta(),
                LocalDateTime.of(2020, 7, 27, 11, 0));
        when(contaService.create(createContaDTO)).thenReturn(mockSavedConta);

        mockMvc.perform(post("/api/v1/contas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCreateConta))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idConta").value(idConta));
    }

    @Test
    void shouldNotCreate_IdPessoaNull() throws Exception {
        CreateContaDTO createContaDTO = CreateContaDTOBuilder.instance();
        createContaDTO.setIdPessoa(null);

        String jsonCreateConta = TestUtil.toJson(createContaDTO);

        mockMvc.perform(post("/api/v1/contas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCreateConta))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").value(containsString("idPessoa")));
    }

    @Test
    void shouldNotCreate_LimiteSaqueDiarioNull() throws Exception {
        CreateContaDTO createContaDTO = CreateContaDTOBuilder.instance();
        createContaDTO.setLimiteSaqueDiario(null);

        String jsonCreateConta = TestUtil.toJson(createContaDTO);

        mockMvc.perform(post("/api/v1/contas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCreateConta))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").value(containsString("limiteSaqueDiario")));
    }

    @Test
    void shouldNotCreate_LimiteSaqueDiarioNegative() throws Exception {
        CreateContaDTO createContaDTO = CreateContaDTOBuilder.instance();
        createContaDTO.setLimiteSaqueDiario(new BigDecimal("-300.00"));

        String jsonCreateConta = TestUtil.toJson(createContaDTO);

        mockMvc.perform(post("/api/v1/contas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCreateConta))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").value("limiteSaqueDiario deve ser zero ou positivo"));
    }

    @Test
    void shouldNotCreate_TipoContaNull() throws Exception {
        CreateContaDTO createContaDTO = CreateContaDTOBuilder.instance();
        createContaDTO.setTipoConta(null);

        String jsonCreateConta = TestUtil.toJson(createContaDTO);

        mockMvc.perform(post("/api/v1/contas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCreateConta))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").value(containsString("tipoConta")));
    }

    @Test
    void shouldNotCreate_InvalidPessoa() throws Exception {
        CreateContaDTO createContaDTO = CreateContaDTOBuilder.instance();
        String jsonCreateConta = TestUtil.toJson(createContaDTO);

        when(contaService.create(any())).thenThrow(new InvalidPessoaException());

        mockMvc.perform(post("/api/v1/contas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCreateConta))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").value(containsString("Pessoa invalida")));
    }
}
