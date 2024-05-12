package org.liptsoft.app.repository;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.liptsoft.app.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CategoryRepositoryTest extends IntegrationTest {
    @Autowired
    private CategoryRepository categoryRepository;

    static Stream<String> provideDataForCorrectlyTest() {
        return Stream.of("Рестораны", "Еда", "Супермаркеты", "Фастфуд", "Развлечения");
    }

    static Stream<String> provideDataForIncorrectlyTest() {
        return Stream.of("Покупки", "Электроника", "Обучение");
    }

    @ParameterizedTest
    @MethodSource("provideDataForCorrectlyTest")
    public void findByName_shouldCorrectlyReturnCategory(String categoryName) {
        assertThat(categoryRepository.findByName(categoryName)).isPresent();
    }

    @ParameterizedTest
    @MethodSource("provideDataForIncorrectlyTest")
    public void findByName_shouldThrowExceptionIfCategoryNotFound(String categoryName) {
        assertThat(categoryRepository.findByName(categoryName)).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("provideDataForCorrectlyTest")
    public void existsByName_shouldReturnTrueIfCategoryFound(String categoryName) {
        assert categoryRepository.existsByName(categoryName);
    }

    @ParameterizedTest
    @MethodSource("provideDataForIncorrectlyTest")
    public void existsByName_shouldReturnFalseIfCategoryNoFound(String categoryName) {
        assert !categoryRepository.existsByName(categoryName);
    }

}
