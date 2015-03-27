package org.vaadin.addonhelpers.automated;

import java.io.*;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.server.Server;
import org.junit.*;
import org.junit.rules.MethodRule;
import org.junit.runners.model.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.*;

/**
 * Base class for webDriver test. If you need a local test server use
 * {@link AbstractWebDriverCase}.
 * 
 */
public class AbstractBaseWebDriverCase {

    protected WebDriver driver;
    protected static Server server;
    @Rule
    public ScreenshotTestRule screenshotTestRule = new ScreenshotTestRule();

    public AbstractBaseWebDriverCase() {
        super();
    }

    @After
    public void tearDown() {

    }

    protected void startBrowser() {
        if (driver != null) {
            driver.quit();
        }
        driver = new FirefoxDriver();
    }

    protected void clickNotification() {
        try {
            WebElement notification = driver.findElement(By
                    .className("v-Notification"));
            notification.click();
        } catch (NoSuchElementException e) {
            throw e;
        }

    }

    protected void waitForLoading() {
        sleep(1000);
        // driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        (new WebDriverWait(driver, 20)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                boolean stopWait = false;
                try {
                    WebElement findElement = d.findElement(By
                            .className("v-loading-indicator"));
                    String cssValueDisplay = findElement.getCssValue("display");
                    if (StringUtils.equals("none", cssValueDisplay)) {
                        stopWait = true;
                    }
                } catch (NoSuchElementException e) {
                    stopWait = true;
                }
                return stopWait;
            }
        });
    }

    protected void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    class ScreenshotTestRule implements MethodRule {
        public Statement apply(final Statement statement,
                final FrameworkMethod frameworkMethod, final Object o) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    try {
                        statement.evaluate();
                    } catch (Throwable t) {
                        captureScreenshot(frameworkMethod.getName());
                        // rethrow to allow the failure to be reported to JUnit
                        throw t;
                    } finally {
                        // Closing the webdriver
                        after();
                    }
                }

                public void after() {
                    // We can't close the webdriver in @After annotated method,
                    // because the method is called before this rule
                    if (driver != null) {
                        driver.quit();
                    }
                }

                public void captureScreenshot(String fileName) {
                    try {// Insure directory is there
                        new File("target/surefire-reports/screenshots/")
                                .mkdirs();
                        FileOutputStream out = new FileOutputStream(
                                "target/surefire-reports/screenshots/"
                                        + fileName + ".png");
                        out.write(((TakesScreenshot) driver)
                                .getScreenshotAs(OutputType.BYTES));
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            };
        }
    }
}