package com.university.ass;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ASSSeleniumTest {

    public static void main(String[] args) {

        // Change this to "chrome", "firefox" or "edge"
        String browser = "firefox";

        WebDriver driver = getBrowser(browser);
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

            // STEP 3: Verify login was successful
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
            WebElement submitAssignment = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.cssSelector("button[type='submit']")
                    )
            );
            submitAssignment.click();
            System.out.println("Assignment submitted");

            // STEP 7: Verify submission was successful
            wait.until(ExpectedConditions.urlContains("submitted"));
            System.out.println("Submission confirmed - redirected back to dashboard");

            WebElement successMsg = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector(".success-msg")
                    )
            );
            System.out.println("Success message displayed: " + successMsg.getText());

        } catch (Exception e) {
            System.out.println("Test failed: " + e.getMessage());
            e.printStackTrace();

        } finally {
            driver.quit();
            System.out.println("Browser closed");
        }
    }

    private static WebDriver getBrowser(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver(new ChromeOptions());
            case "edge":
                WebDriverManager.edgedriver().setup();
                return new EdgeDriver(new EdgeOptions());
            case "firefox":
            default:
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver(new FirefoxOptions());
        }
    }
}
