package org.liptsoft.app.controller;

import api.CategoryApi;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.RequiredArgsConstructor;
import model.CategoryGroup;
import model.CategoryWithMcc;
import model.CategoryWithSubCategory;
import model.RemoveCategoryResponse;
import org.liptsoft.app.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryApi {
    private final CategoryService categoryService;

    @Override
    public ResponseEntity<CategoryWithMcc> addMccByCategory(
        String categoryName,
        @Valid @Size(min = 1) List<@Min(1000) @Max(9999) Integer> mccs
    ) {
        return ResponseEntity.ok(categoryService.addMcc(categoryName, mccs));
    }

    @Override
    public ResponseEntity<CategoryWithSubCategory> addSubcategory(String categoryName, CategoryGroup categoryGroup) {
        return ResponseEntity.ok(categoryService.addSubcategory(categoryName, categoryGroup));
    }

    @Override
    public ResponseEntity<CategoryWithMcc> createCategory(CategoryWithMcc categoryWithMcc) {
        return ResponseEntity.ok(categoryService.createCategory(categoryWithMcc));
    }

    @Override
    public ResponseEntity<RemoveCategoryResponse> deleteCategory(String categoryName) {
        return ResponseEntity.ok(categoryService.removeCategory(categoryName));
    }

    @Override
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }
}
