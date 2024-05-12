package org.liptsoft.app.controller;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import model.AddTransactionRequest;
import model.MonthEnum;
import model.Transaction;
import org.junit.jupiter.api.Test;
import org.liptsoft.app.exception.NoExistCategoryNameException;
import org.liptsoft.app.exception.NoExistMccCategoryException;
import org.liptsoft.app.exception.NotFoundTransactionException;
import org.liptsoft.app.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = TransactionController.class)
public class TransactionControllerTest {
    private static final String CATEGORY_NAME = "Фастфуд";
    private static final AddTransactionRequest TRANSACTION_REQUEST =
        new AddTransactionRequest().transaction(new Transaction(1700.0, MonthEnum.AUGUST))
            .mcc(List.of(4299, 5298));
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Transaction TRANSACTION = new Transaction(500.0, MonthEnum.JANUARY);
    @Autowired
    MockMvc mockMvc;
    @MockBean
    TransactionService transactionService;

    @Test
    void addTransaction_shouldReturnOkForCorrectRequest() throws Exception {
        when(transactionService.addTransaction(CATEGORY_NAME, TRANSACTION_REQUEST)).thenReturn(List.of(CATEGORY_NAME));
        getPostPerform().andExpect(status().isOk());
    }

    @Test
    void addTransaction_shouldReturnBadRequestForNoExistCategoryName() throws Exception {
        when(transactionService.addTransaction(CATEGORY_NAME, TRANSACTION_REQUEST)).thenThrow(
            NoExistCategoryNameException.class);
        getPostPerform().andExpect(status().isBadRequest());
    }

    @Test
    void addTransaction_shouldReturnBadRequestForNoExistMcc() throws Exception {
        when(transactionService.addTransaction(CATEGORY_NAME, TRANSACTION_REQUEST)).thenThrow(
            NoExistMccCategoryException.class);
        getPostPerform().andExpect(status().isBadRequest());
    }

    @Test
    void deleteTransaction_shouldReturnOkForCorrectRequest() throws Exception {
        when(transactionService.deleteTransaction(CATEGORY_NAME, TRANSACTION)).thenReturn(TRANSACTION);
        getDeletePerform().andExpect(status().isOk());
    }

    @Test
    void deleteTransaction_shouldReturnBadRequestForNoExistCategoryName() throws Exception {
        when(transactionService.deleteTransaction(
            CATEGORY_NAME,
            TRANSACTION
        )).thenThrow(NoExistCategoryNameException.class);
        getDeletePerform().andExpect(status().isBadRequest());
    }

    @Test
    void deleteTransaction_shouldReturnBadRequestForNoExistTransaction() throws Exception {
        when(transactionService.deleteTransaction(
            CATEGORY_NAME,
            TRANSACTION
        )).thenThrow(NotFoundTransactionException.class);
        getDeletePerform().andExpect(status().isBadRequest());
    }

    @Test
    void getTransactionByCategory_shouldReturnOkForCorrectRequest() throws Exception {
        when(transactionService.getTransactionByCategory(CATEGORY_NAME)).thenReturn(List.of());
        getGetPerform().andExpect(status().isOk());
    }

    @Test
    void getTransactionByCategory_shouldReturnBadRequestForNoExistCategoryName() throws Exception {
        when(transactionService.getTransactionByCategory(CATEGORY_NAME)).thenThrow(NoExistCategoryNameException.class);
        getGetPerform().andExpect(status().isBadRequest());
    }

    @Test
    void getTransactionByMonth_shouldReturnOkForCorrectRequest() throws Exception {
        when(transactionService.getTransactionByMonth(MonthEnum.JANUARY)).thenReturn(List.of());
        getGetMonthPerform().andExpect(status().isOk());
    }

    @NotNull
    private ResultActions getPostPerform() throws Exception {
        return mockMvc.perform(post(
            "/transaction/{categoryName}",
            CATEGORY_NAME
        ).content(OBJECT_MAPPER.writeValueAsString(TRANSACTION_REQUEST))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));
    }

    @NotNull
    private ResultActions getDeletePerform() throws Exception {
        return mockMvc.perform(delete(
            "/transaction/{categoryName}",
            CATEGORY_NAME
        ).content(OBJECT_MAPPER.writeValueAsString(TRANSACTION))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));
    }

    @NotNull
    private ResultActions getGetPerform() throws Exception {
        return mockMvc.perform(get(
            "/transaction/category/{categoryName}",
            CATEGORY_NAME
        ).accept(MediaType.APPLICATION_JSON));
    }

    @NotNull
    private ResultActions getGetMonthPerform() throws Exception {
        return mockMvc.perform(get("/transaction/{month}", MonthEnum.JANUARY).accept(MediaType.APPLICATION_JSON));
    }

}
