package ru.netology;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeleniumTest {
    private ChromeOptions options = new ChromeOptions();
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
    }

    @BeforeEach
    void setUp() {
        options.setHeadless(true);
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Nested
    public class NameValidPhoneValidCheckIs {
        private String expected;

        @BeforeEach
        void init() {
            driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
            driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
            expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        }

        @Test
        void shouldSentFormAllValid() {
            driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Василий");
            driver.findElement(By.cssSelector("[role='button']")).click();
            String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
            assertEquals(expected, text.trim());
        }

        @Test
        void shouldSentFormDableName() {
            driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Петр Иван");
            driver.findElement(By.cssSelector("[role='button']")).click();
            String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
            assertEquals(expected, text.trim());
        }

        @Test
        void shouldSentFormDableNameSeparator() {
            driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Петр-Иван");
            driver.findElement(By.cssSelector("[role='button']")).click();
            String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
            assertEquals(expected, text.trim());
        }

        @Test
        void shouldSentFormDableNameLong() {
            driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванова-Перевозкина-Пограничникова Александра Виктория Валентина");
            driver.findElement(By.cssSelector("[role='button']")).click();
            String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
            assertEquals(expected, text.trim());
        }
    }

    @Nested
    public class NameNotValidPhoneValidCheckIs {
        private String expected;

        @BeforeEach
        void init() {
            driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
            driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
            expected="Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        }

        @Test
        void shouldNotSentFormNameEnglish() {
            driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ivanov Ivan");
            driver.findElement(By.cssSelector("[role='button']")).click();
            WebElement name = driver.findElement(By.cssSelector("[data-test-id=name]"));
            String text = name.findElement(By.className("input__sub")).getText();
            assertEquals(expected, text.trim());
        }

        @Test
        void shouldNotSentFormNameNumber() {
            driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("8888");
            driver.findElement(By.cssSelector("[role='button']")).click();
            WebElement name = driver.findElement(By.cssSelector("[data-test-id=name]"));
            String text = name.findElement(By.className("input__sub")).getText();
            assertEquals(expected, text.trim());
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
            assertEquals(expected, text.trim());
        }

//        @Test
//        void shouldNotSentFormNameOnly() {
//            driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов");
//            driver.findElement(By.cssSelector("[role='button']")).click();
//            WebElement name = driver.findElement(By.cssSelector("[data-test-id=name]"));
//            String text = name.findElement(By.className("input__sub")).getText();
//            assertEquals(expected, text.trim());
//        }
    }

    @Nested
    public class PhoneNotValidNameValidCheckIs {
        private String expected;

        @BeforeEach
        void init() {
            driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Василий");
            driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
            expected="Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        }

        @Test
        void shouldNotSentFormPhoneSpecialSymbols() {
            driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("$^%%^^&^");
            driver.findElement(By.cssSelector("[role='button']")).click();
            WebElement phone = driver.findElement(By.cssSelector("[data-test-id=phone]"));
            String text = phone.findElement(By.className("input__sub")).getText();
            assertEquals(expected, text.trim());
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
            assertEquals(expected, text.trim());
        }

        @Test
        void shouldNotSentFormPhoneNot11element() {
            driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("8989");
            driver.findElement(By.cssSelector("[role='button']")).click();
            WebElement phone = driver.findElement(By.cssSelector("[data-test-id=phone]"));
            String text = phone.findElement(By.className("input__sub")).getText();
            assertEquals(expected, text.trim());
        }

//        @Test
//        void shouldNotSentFormPhoneFist0(){
//            driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+09999999999");
//            driver.findElement(By.cssSelector("[role='button']")).click();
//            WebElement phone = driver.findElement(By.cssSelector("[data-test-id=phone]"));
//            String text = phone.findElement(By.className("input__sub")).getText();
//            assertEquals(expected, text.trim());
//        }
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


