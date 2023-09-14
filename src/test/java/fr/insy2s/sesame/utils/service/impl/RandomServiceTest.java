package fr.insy2s.sesame.utils.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RandomServiceTest {
    private RandomServiceImpl randomService;

    @BeforeEach
    void setUp() {
        randomService = new RandomServiceImpl();
    }

    @Test
    void generateRandomUUIDString() {
       String randomString = randomService.generateRandomUUIDString(10);
        assertEquals(10, randomString.length());
    }

    @Test
    void generateRandomUUIDStringWithNegativeLength() {
        String randomString = randomService.generateRandomUUIDString(-10);
        assertEquals(5, randomString.length());
    }

    @Test
    void generateRandomUUIDStringWithTooLongLength() {
        String randomString = randomService.generateRandomUUIDString(100);
        assertEquals(36, randomString.length());
    }
}