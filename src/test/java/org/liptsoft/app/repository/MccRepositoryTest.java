package org.liptsoft.app.repository;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.liptsoft.app.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MccRepositoryTest extends IntegrationTest {
    @Autowired
    private MccRepository mccRepository;

    static Stream<Integer> provideDataForCorrectlyTest() {
        return Stream.of(5811, 5812, 5813, 5814, 5297);
    }

    static Stream<Integer> provideDataForIncorrectlyTest() {
        return Stream.of(3213, 2222, 8989);
    }

    @ParameterizedTest
    @MethodSource("provideDataForCorrectlyTest")
    public void findByMcc_shouldCorrectlyReturnMcc(Integer mcc) {
        assertThat(mccRepository.findByMcc(mcc)).isPresent();
    }

    @ParameterizedTest
    @MethodSource("provideDataForIncorrectlyTest")
    public void findByMcc_shouldThrowExceptionIfMccNotFound(Integer mcc) {
        assertThat(mccRepository.findByMcc(mcc)).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("provideDataForCorrectlyTest")
    public void existsByMcc_shouldReturnTrueIfMccFound(Integer mcc) {
        assert mccRepository.existsByMcc(mcc);
    }

    @ParameterizedTest
    @MethodSource("provideDataForIncorrectlyTest")
    public void existsByMcc_shouldReturnFalseIfMccNoFound(Integer mcc) {
        assert !mccRepository.existsByMcc(mcc);
    }

}
