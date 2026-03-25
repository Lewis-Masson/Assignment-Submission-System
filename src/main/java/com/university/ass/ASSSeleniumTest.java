package com.university.ass;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ASSSeleniumTest {

    public static void main(String[] args) {

        // Set up ChromeDriver
        ChromeOptions options = new ChromeOptions();

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {

            // STEP 1: Navigate to the login page
            driver.get("https://ass-system-63986c5db6f4.herokuapp.com/login");
            System.out.println("Navigated to login page");

            // STEP 2: Log in with valid credentials
            WebElement emailField = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.name("email"))
            );
            emailField.sendKeys("student@test.com");

            WebElement passwordField = driver.findElement(By.name("password"));
            passwordField.sendKeys("Password1");

            WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
            loginButton.click();
            System.out.println("Login submitted");

            // STEP 3: Verify login was successful by checking dashboard
            wait.until(ExpectedConditions.urlContains("/student/dashboard"));
            System.out.println("Login successful - on student dashboard");

            // STEP 4: Navigate to submit assignment page
            WebElement submitButton = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.cssSelector("a[href='/student/submit']")
                    )
            );
            submitButton.click();
            System.out.println("Navigated to submit assignment page");

            // STEP 5: Fill in the assignment submission form
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("courseId")))
                    .sendKeys("CS101");

            driver.findElement(By.name("creditUnits")).sendKeys("15");
            driver.findElement(By.name("session")).sendKeys("2024/2025");
            driver.findElement(By.name("term")).sendKeys("Semester 1");
            driver.findElement(By.name("additionalInfo"))
                    .sendKeys("Selenium automated test submission");

            System.out.println("Assignment form filled");

            // STEP 6: Submit the form
            driver.findElement(By.cssSelector("button[type='submit']")).click();
            System.out.println("Assignment submitted");

            // STEP 7: Verify submission was successful
            wait.until(ExpectedConditions.urlContains("submitted"));
            System.out.println("Submission confirmed - redirected back to dashboard");

            // Verify the success message is visible
            WebElement successMsg = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector(".success-msg")
                    )
            );
            System.out.println("Success message displayed: " + successMsg.getText());

        } catch (Exception e) {
            System.out.println("Test failed: " + e.getMessage());

        } finally {
            // Close the browser
            driver.quit();
            System.out.println("Browser closed");
        }
    }
}
