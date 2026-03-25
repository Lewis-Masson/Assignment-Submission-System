package com.university.ass;

import com.university.ass.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordCheckJUnitTest {

    User user = new User();

    @Test
    @DisplayName("TC1 - Valid password abcPass1 should return true")
    public void testValidPassword() {
        assertEquals(true, user.checkPassword("abcPass1"),
                "Valid password should return true");
    }

    @Test
    @DisplayName("TC2 - Password too short abc12 should return false")
    public void testPasswordTooShort() {
        assertEquals(false, user.checkPassword("abc12"),
                "Password shorter than 6 characters should return false");
    }

    @Test
    @DisplayName("TC3 - Password too long abcPassword1 should return false")
    public void testPasswordTooLong() {
        assertEquals(false, user.checkPassword("abcPassword1"),
                "Password longer than 10 characters should return false");
    }

    @Test
    @DisplayName("TC4 - First three characters of abcPass1 must all be letters")
    public void testFirstThreeCharactersAreLetters() {
        String password = "abcPass1";
        assertTrue(Character.isLetter(password.charAt(0)) &&
                   Character.isLetter(password.charAt(1)) &&
                   Character.isLetter(password.charAt(2)),
                "First three characters should all be letters");
    }
}