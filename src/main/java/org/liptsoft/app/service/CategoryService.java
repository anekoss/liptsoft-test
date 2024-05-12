package org.liptsoft.app.service;

import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import model.CategoryGroup;
import model.CategoryWithMcc;
import model.CategoryWithSubCategory;
import model.RemoveCategoryResponse;
import org.liptsoft.app.domain.CategoryEntity;
import org.liptsoft.app.domain.MccEntity;
import org.liptsoft.app.exception.ExistCategoryNameException;
import org.liptsoft.app.exception.ExistMccCategoryException;
import org.liptsoft.app.exception.NoExistCategoryNameException;
import org.liptsoft.app.repository.CategoryRepository;
import org.liptsoft.app.repository.MccRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    private final MccRepository mccRepository;

    @Transactional
    public CategoryWithMcc createCategory(CategoryWithMcc categoryWithMcc) {
        final String categoryName = categoryWithMcc.getCategoryName();
        Optional<CategoryEntity> optionalCategory = categoryRepository.findByName(categoryName);
        if (optionalCategory.isPresent()) {
            throw new ExistCategoryNameException(optionalCategory.get().getName());
        }
        CategoryEntity category = new CategoryEntity().setName(categoryName);
        categoryRepository.saveAndFlush(category.setMccs(checkMccs(category, categoryWithMcc.getMccs())));
        return categoryWithMcc;
    }

    @Transactional
    public RemoveCategoryResponse removeCategory(String categoryName) {
        final CategoryEntity category = findCategoryByName(categoryName);
        categoryRepository.delete(category);
        return new RemoveCategoryResponse(categoryName);
    }

    @Transactional
    public CategoryWithMcc addMcc(String categoryName, List<Integer> mccs) {
        CategoryEntity category = findCategoryByName(categoryName);
        category.getMccs().addAll(checkMccs(category, mccs));
        categoryRepository.saveAndFlush(category);
        return new CategoryWithMcc(categoryName, category.getMccs().stream().map(MccEntity::getMcc).toList());
    }

    @Transactional
    public CategoryWithSubCategory addSubcategory(String categoryName, CategoryGroup categoryGroup) {
        CategoryEntity category = findCategoryByName(categoryName);
        for (var subcategoryName : categoryGroup.getCategories()) {
            CategoryEntity subcategory = findCategoryByName(subcategoryName);
            category.addSubcategory(subcategory);
        }
        categoryRepository.saveAndFlush(category);
        return new CategoryWithSubCategory(categoryName, category.getMccs()
            .stream()
            .map(MccEntity::getMcc)
            .toList()).categories(category.getSubCategories().stream()
            .map(CategoryEntity::getName)
            .toList());
    }

    @Transactional
    public List<String> getCategories() {
        return categoryRepository.findAll().stream().map(CategoryEntity::getName).toList();
    }

    private CategoryEntity findCategoryByName(String categoryName) {
        Optional<CategoryEntity> optionalCategory = categoryRepository.findByName(categoryName);
        if (optionalCategory.isEmpty()) {
            throw new NoExistCategoryNameException(categoryName);
        }
        return optionalCategory.get();
    }

    private Set<MccEntity> checkMccs(CategoryEntity category, List<Integer> mccs) {
        Set<MccEntity> categoryMccs = new HashSet<>();
        for (var mcc : mccs) {
            Optional<MccEntity> optionalMcc = mccRepository.findByMcc(mcc);
            if (optionalMcc.isPresent()) {
                throw new ExistMccCategoryException(optionalMcc.get().getCategory().getName(), mcc);
            }
            categoryMccs.add(new MccEntity(mcc, category));
        }
        return categoryMccs;
    }

}
