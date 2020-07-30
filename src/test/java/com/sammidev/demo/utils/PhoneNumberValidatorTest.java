package com.sammidev.demo.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class PhoneNumberValidatorTest {

    private PhoneNumberValidator underTest;

    @BeforeEach
    void setUp() {
        underTest = new PhoneNumberValidator();
    }

    @ParameterizedTest
    @CsvSource({
            "+62238794, false",
            "+239234802, false",
            "+6294, true"
    })
    void itShouldValidatePhoneNumber(String phoneNumber, boolean expected) {
        // when
        boolean isValid = underTest.test(phoneNumber);
        // then
        assertThat(isValid).isEqualTo(expected);
    }
}