package com.sammidev.demo.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class KegantenganTest {
    private Kegantengan underTest;
    @BeforeEach
    void setUp() {
        underTest = new Kegantengan();
    }

    @ParameterizedTest
    @CsvSource({
            "Ayatullah, true",
            "Sammi, true",
            "Adit, true",
            "Dandi, true"
    })
    void itShouldValidateGanteng(String name, boolean expected) {
        // WHEN
        boolean isGanteng = underTest.test(name);
        // THEN
        assertThat(isGanteng).isEqualTo(expected);
    }
}