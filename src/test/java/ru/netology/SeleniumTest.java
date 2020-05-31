package ru.netology;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeleniumTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
    }

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSentFormAllValid() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Василий");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("[role='button']")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Nested
    public class NameNotValidPhoneValidCheckIs {

        @BeforeEach
        void init() {
            driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
            driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        }

        @Test
        void shouldNotSentFormNameEnglish() {
            driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ivanov Ivan");
            driver.findElement(By.cssSelector("[role='button']")).click();
            WebElement name = driver.findElement(By.cssSelector("[data-test-id=name]"));
            String text = name.findElement(By.className("input__sub")).getText();
            assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
        }

        @Test
        void shouldNotSentFormNameNumber() {
            driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("8888");
            driver.findElement(By.cssSelector("[role='button']")).click();
            WebElement name = driver.findElement(By.cssSelector("[data-test-id=name]"));
            String text = name.findElement(By.className("input__sub")).getText();
            assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
        }

        @Test
        void shouldNotSentFormNameEmpty() {
            driver.findElement(By.cssSelector("[role='button']")).click();
            String text = driver.findElement(By.className("input__sub")).getText();
            assertEquals("Поле обязательно для заполнения", text.trim());
        }

        @Test
        void shouldNotSentFormNameSpecialSymbols() {
            driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("$$^@#@%^^^$#");
            driver.findElement(By.cssSelector("[role='button']")).click();
            WebElement name = driver.findElement(By.cssSelector("[data-test-id=name]"));
            String text = name.findElement(By.className("input__sub")).getText();
            assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
        }
    }

    @Nested
    public class PhoneNotValidNameValidCheckIs {

        @BeforeEach
        void init() {
            driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Василий");
            driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        }

        @Test
        void shouldNotSentFormPhoneSpecialSymbols() {
            driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("$^%%^^&^");
            driver.findElement(By.cssSelector("[role='button']")).click();
            WebElement phone = driver.findElement(By.cssSelector("[data-test-id=phone]"));
            String text = phone.findElement(By.className("input__sub")).getText();
            assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
        }

        @Test
        void shouldNotSentFormPhoneEmpty() {
            driver.findElement(By.cssSelector("[role='button']")).click();
            WebElement phone = driver.findElement(By.cssSelector("[data-test-id=phone]"));
            String text = phone.findElement(By.className("input__sub")).getText();
            assertEquals("Поле обязательно для заполнения", text.trim());
        }

        @Test
        void shouldNotSentFormPhoneFistSymbolNotPlus() {
            driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("89898989890");
            driver.findElement(By.cssSelector("[role='button']")).click();
            WebElement phone = driver.findElement(By.cssSelector("[data-test-id=phone]"));
            String text = phone.findElement(By.className("input__sub")).getText();
            assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
        }

        @Test
        void shouldNotSentFormPhoneNot11element() {
            driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("8989");
            driver.findElement(By.cssSelector("[role='button']")).click();
            WebElement phone = driver.findElement(By.cssSelector("[data-test-id=phone]"));
            String text = phone.findElement(By.className("input__sub")).getText();
            assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
        }
    }

    @Nested
    public class CheckIsNotNamePhoneValid {

        @Test
        void shouldNotSentFormPhoneEmpty() {
            driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Василий");
            driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
            driver.findElement(By.cssSelector("[role='button']")).click();
            assertEquals("rgba(255, 92, 92, 1)", driver.findElement(By.cssSelector("[data-test-id=agreement]")).getCssValue("color"));
        }
    }
}


