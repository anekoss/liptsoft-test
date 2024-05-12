package org.liptsoft.app.controller;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import model.CategoryGroup;
import model.CategoryWithMcc;
import model.CategoryWithSubCategory;
import model.RemoveCategoryResponse;
import org.junit.jupiter.api.Test;
import org.liptsoft.app.exception.ExistCategoryNameException;
import org.liptsoft.app.exception.ExistMccCategoryException;
import org.liptsoft.app.exception.NoExistCategoryNameException;
import org.liptsoft.app.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = CategoryController.class)
public class CategoryControllerTest {
    private static final String CATEGORY_NAME = "Фастфуд";
    private static final List<Integer> MCCS = List.of(3333, 4444);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final CategoryWithMcc CATEGORY_WITH_MCC = new CategoryWithMcc(CATEGORY_NAME, MCCS);
    private static final CategoryGroup CATEGORY_GROUP = new CategoryGroup();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    CategoryService categoryService;

    @Test
    void addMcc_shouldReturnOkForCorrectRequest() throws Exception {
        when(categoryService.addMcc(CATEGORY_NAME, MCCS)).thenReturn(CATEGORY_WITH_MCC);
        getPostCategoryPerform().andExpect(status().isOk());
    }

    @Test
    void addMcc_shouldReturnBadRequestIfNoExistCategoryName() throws Exception {
        when(categoryService.addMcc(CATEGORY_NAME, MCCS)).thenThrow(NoExistCategoryNameException.class);
        getPostCategoryPerform().andExpect(status().isBadRequest());
    }

    @Test
    void addMcc_shouldReturnBadRequestIfExistMcc() throws Exception {
        when(categoryService.addMcc(CATEGORY_NAME, MCCS)).thenThrow(ExistMccCategoryException.class);
        getPostCategoryPerform().andExpect(status().isBadRequest());
    }

    @Test
    void addSubcategory_shouldReturnOkForCorrectRequest() throws Exception {
        when(categoryService.addSubcategory(
            CATEGORY_NAME,
            CATEGORY_GROUP
        )).thenReturn(mock(CategoryWithSubCategory.class));
        getPutCategoryPerform().andExpect(status().isOk());
    }

    @Test
    void addSubcategory_shouldReturnBadRequestIfNoExistCategoryName() throws Exception {
        when(categoryService.addSubcategory(
            CATEGORY_NAME,
            CATEGORY_GROUP
        )).thenThrow(NoExistCategoryNameException.class);
        getPutCategoryPerform().andExpect(status().isBadRequest());
    }

    @Test
    void createCategory_shouldReturnOkForCorrectRequest() throws Exception {
        when(categoryService.createCategory(CATEGORY_WITH_MCC)).thenReturn(mock(CategoryWithMcc.class));
        getPostPerform().andExpect(status().isOk());
    }

    @Test
    void createCategory_shouldReturnBadRequestIfNoExistCategoryName() throws Exception {
        when(categoryService.createCategory(CATEGORY_WITH_MCC)).thenThrow(ExistCategoryNameException.class);
        getPostPerform().andExpect(status().isBadRequest());
    }

    @Test
    void createCategory_shouldReturnBadRequestIfExistMcc() throws Exception {
        when(categoryService.createCategory(CATEGORY_WITH_MCC)).thenThrow(ExistMccCategoryException.class);
        getPostPerform().andExpect(status().isBadRequest());
    }

    @Test
    void deleteCategory_shouldReturnOkForCorrectRequest() throws Exception {
        when(categoryService.removeCategory(CATEGORY_NAME)).thenReturn(mock(RemoveCategoryResponse.class));
        getDeleteCategoryPerform().andExpect(status().isOk());
    }

    @Test
    void deleteCategory_shouldReturnBadRequestIfNoExistCategoryName() throws Exception {
        when(categoryService.removeCategory(CATEGORY_NAME)).thenThrow(NoExistCategoryNameException.class);
        getDeleteCategoryPerform().andExpect(status().isBadRequest());
    }

    @Test
    void getCategories_shouldReturnOkForCorrectRequest() throws Exception {
        when(categoryService.getCategories()).thenReturn(List.of());
        getGetPerform().andExpect(status().isOk());
    }

    @NotNull
    private ResultActions getPostPerform() throws Exception {
        return mockMvc.perform(post("/category").content(OBJECT_MAPPER.writeValueAsString(CATEGORY_WITH_MCC))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));
    }

    @NotNull
    private ResultActions getGetPerform() throws Exception {
        return mockMvc.perform(get("/category")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));
    }

    @NotNull
    private ResultActions getDeleteCategoryPerform() throws Exception {
        return mockMvc.perform(delete("/category/{categoryName}", CATEGORY_NAME)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));
    }

    @NotNull
    private ResultActions getPostCategoryPerform() throws Exception {
        return mockMvc.perform(post("/category/{categoryName}", CATEGORY_NAME).content(OBJECT_MAPPER.writeValueAsString(
                MCCS))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));
    }

    @NotNull
    private ResultActions getPutCategoryPerform() throws Exception {
        return mockMvc.perform(put("/category/{categoryName}", CATEGORY_NAME).content(OBJECT_MAPPER.writeValueAsString(
                CATEGORY_GROUP))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));
    }

}
