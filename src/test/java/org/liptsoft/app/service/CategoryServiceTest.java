package org.liptsoft.app.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import model.CategoryGroup;
import model.CategoryWithMcc;
import org.junit.jupiter.api.Test;
import org.liptsoft.app.IntegrationTest;
import org.liptsoft.app.domain.CategoryEntity;
import org.liptsoft.app.domain.MccEntity;
import org.liptsoft.app.exception.ExistCategoryNameException;
import org.liptsoft.app.exception.ExistMccCategoryException;
import org.liptsoft.app.exception.NoExistCategoryNameException;
import org.liptsoft.app.repository.CategoryRepository;
import org.liptsoft.app.repository.MccRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CategoryServiceTest extends IntegrationTest {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MccRepository mccRepository;

    @Test
    @Rollback
    @Transactional
    public void createCategory_shouldCorrectlyCreateCategory()
        throws ExistMccCategoryException, ExistCategoryNameException {
        final CategoryWithMcc categoryWithMcc = new CategoryWithMcc("Путешествия", List.of(3333, 4444));
        categoryService.createCategory(categoryWithMcc);
        Optional<CategoryEntity> category = categoryRepository.findByName("Путешествия");
        assert category.isPresent();
        List<Integer> mccs = category.get().getMccs().stream().map(MccEntity::getMcc).toList();
        assertThat(mccs).containsAll(categoryWithMcc.getMccs());
    }

    @Test
    @Rollback
    @Transactional
    public void createCategory_shouldThrowExceptionIfCategoryNameExist() {
        ExistCategoryNameException exception = assertThrows(
            ExistCategoryNameException.class,
            () -> categoryService.createCategory(new CategoryWithMcc("Рестораны", List.of(1111)))
        );
        assertThat(exception.getMessage()).isEqualTo("Категория с именем \"Рестораны\" уже существует");
    }

    @Test
    @Rollback
    @Transactional
    public void createCategory_shouldThrowExceptionIfMccExist() {
        final CategoryWithMcc categoryWithMcc = new CategoryWithMcc("Путешествия", List.of(3333, 5813));
        ExistMccCategoryException exception =
            assertThrows(ExistMccCategoryException.class, () -> categoryService.createCategory(categoryWithMcc));
        assertThat(exception.getMessage()).isEqualTo("MCC \"5813\" уже зарезервирован для категории \"Рестораны\"");
    }

    @Test
    @Rollback
    @Transactional
    public void removeCategory_shouldCorrectlyRemoveCategoryAndMcc() throws NoExistCategoryNameException {
        final String categoryName = "Рестораны";
        final List<Integer> categoryMccs = List.of(1, 2, 3);
        categoryService.removeCategory(categoryName);
        assert !categoryRepository.existsByName(categoryName);
        categoryMccs.forEach(mcc -> {
            assert !mccRepository.existsByMcc(mcc);
        });
    }

    @Test
    @Rollback
    @Transactional
    public void removeCategory_shouldCorrectlyRemoveCategoryAndNotRemoveSubcategory()
        throws NoExistCategoryNameException {
        final List<String> subcategoryNames = List.of("Рестораны", "Супермаркеты", "Фастфуд");
        final String categoryName = "Еда";
        categoryService.removeCategory(categoryName);
        subcategoryNames.forEach(subcategoryName -> {
            assert categoryRepository.existsByName(subcategoryName);
        });
    }

    @Test
    @Rollback
    @Transactional
    public void removeCategory_shouldThrowExceptionIfCategoryNameNoExist() {
        final String categoryName = "Путешествия";
        NoExistCategoryNameException exception = assertThrows(NoExistCategoryNameException.class, () -> {
            categoryService.removeCategory(categoryName);
        });
        assertThat(exception.getMessage()).isEqualTo("Категория с именем \"Путешествия\" не найдена");
    }

    @Test
    @Rollback
    @Transactional
    public void addMcc_shouldCorrectlyAddMccIfMccNoExistAndCategoryExist()
        throws ExistMccCategoryException, NoExistCategoryNameException {
        final String categoryName = "Развлечения";
        final List<Integer> mccs = List.of(1010, 2020, 3030);
        final List<Integer> exceptedMccs = List.of(4299, 1010, 2020, 3030);
        assertThat(categoryService.addMcc(categoryName, mccs).getMccs()).containsAll(exceptedMccs);
        assertThat(categoryRepository.findByName(categoryName)
            .get()
            .getMccs()
            .stream()
            .map(MccEntity::getMcc)
            .toList()).containsAll(exceptedMccs);
    }

    @Test
    @Rollback
    @Transactional
    public void addMcc_shouldThrowExceptionIfCategoryNoExist() {
        final String categoryName = "Путешествия";
        NoExistCategoryNameException exception = assertThrows(NoExistCategoryNameException.class, () -> {
            categoryService.addMcc(categoryName, List.of(3245));
        });
        assertThat(exception.getMessage()).isEqualTo("Категория с именем \"Путешествия\" не найдена");
    }

    @Test
    @Rollback
    @Transactional
    public void addMcc_shouldThrowExceptionIfMccExist() {
        final String categoryName = "Развлечения";
        final List<Integer> mccs = List.of(1010, 2020, 3030, 5814);
        ExistMccCategoryException exception = assertThrows(ExistMccCategoryException.class, () ->
            categoryService.addMcc(categoryName, mccs));
        assertThat(exception.getMessage()).isEqualTo("MCC \"5814\" уже зарезервирован для категории \"Фастфуд\"");
    }

    @Test
    @Rollback
    @Transactional
    public void addSubcategory_shouldCorrectlyAddSubcategories() throws NoExistCategoryNameException {
        final String categoryName = "Развлечения";
        final CategoryGroup categoryGroup = new CategoryGroup().categories(List.of("Фастфуд", "Еда"));
        final List<String> exceptedSubcategories = List.of("Фастфуд", "Еда", "Рестораны");
        categoryService.addSubcategory(categoryName, categoryGroup);
        assertThat(categoryRepository.findByName(categoryName).get()
            .getSubCategories()
            .stream()
            .map(CategoryEntity::getName)).containsAll(exceptedSubcategories);
    }

    @Test
    @Rollback
    @Transactional
    public void addSubcategory_shouldThrowExceptionIfCategoryNameNoExist() {
        final String categoryName = "Путешествия";
        final CategoryGroup categoryGroup = new CategoryGroup().categories(List.of("Фастфуд", "Еда"));
        NoExistCategoryNameException exception = assertThrows(NoExistCategoryNameException.class, () -> {
            categoryService.addSubcategory(categoryName, categoryGroup);
        });
        assertThat(exception.getMessage()).isEqualTo("Категория с именем \"Путешествия\" не найдена");
    }

    @Test
    @Rollback
    @Transactional
    public void addSubcategory_shouldThrowExceptionIfSubcategoryNameNoExist() {
        final String categoryName = "Еда";
        final CategoryGroup categoryGroup = new CategoryGroup().categories(List.of("Путешествия", "Фастфуд"));
        NoExistCategoryNameException exception = assertThrows(NoExistCategoryNameException.class, () -> {
            categoryService.addSubcategory(categoryName, categoryGroup);
        });
        assertThat(exception.getMessage()).isEqualTo("Категория с именем \"Путешествия\" не найдена");
    }

    @Test
    @Rollback
    @Transactional
    public void getCategories_shouldCorrectlyReturnCategories() {
        List<String> exceptedCategories = List.of("Рестораны", "Еда", "Супермаркеты", "Фастфуд", "Развлечения");
        List<String> categories = categoryService.getCategories();
        assert categories.size() == 5;
        assertThat(categories).containsAll(exceptedCategories);
    }

    @Test
    @Rollback
    @Transactional
    public void getCategories_shouldReturnEmptyListIfNoCategories() {
        categoryRepository.deleteAll();
        assert categoryService.getCategories().isEmpty();
    }

}

