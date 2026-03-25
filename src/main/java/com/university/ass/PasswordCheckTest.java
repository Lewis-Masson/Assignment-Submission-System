package com.university.ass;

import com.university.ass.model.User;

public class PasswordCheckTest {

    public static void main(String[] args) {

        User user = new User();

        System.out.println("=== Password Check Test Results ===");
        System.out.println();

        // TC1 - Valid password
        String tc1 = "abcPass1";
        boolean result1 = user.checkPassword(tc1);
        System.out.println("TC1 - Valid password '" + tc1 + "': " + result1);
        System.out.println("Expected: true | Result: " + (result1 == true ? "PASS" : "FAIL"));
        System.out.println();

        // TC2 - Too short
        String tc2 = "abc12";
        boolean result2 = user.checkPassword(tc2);
        System.out.println("TC2 - Too short '" + tc2 + "': " + result2);
        System.out.println("Expected: false | Result: " + (result2 == false ? "PASS" : "FAIL"));
        System.out.println();

        // TC3 - Too long
        String tc3 = "abcPassword1";
        boolean result3 = user.checkPassword(tc3);
        System.out.println("TC3 - Too long '" + tc3 + "': " + result3);
        System.out.println("Expected: false | Result: " + (result3 == false ? "PASS" : "FAIL"));
        System.out.println();

        // TC4 - First three not letters
        String tc4 = "12cPass1";
        boolean result4 = user.checkPassword(tc4);
        System.out.println("TC4 - First three not letters '" + tc4 + "': " + result4);
        System.out.println("Expected: false | Result: " + (result4 == false ? "PASS" : "FAIL"));
        System.out.println();

        System.out.println("=== Tests Complete ===");
    }
}