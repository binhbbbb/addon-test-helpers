package org.vaadin.addonhelpers.automated;

import org.openqa.selenium.*;

import com.google.common.base.Predicate;

public class VaadinConditions {
    private static final long BROWSER_RENDERING_MS = 10;

    /**
     * Waiting condition for selenium webdriver. Uses the vaadin js-api to see
     * if rendering is done.
     * <p>
     * Taken, and fixed, from the (german) book "Vaadin".
     *
     * @see <a href="https://www.dpunkt.de/buecher/4526/vaadin.html">(german)
     *      Book: Vaadin</a>
     *
     */
    public static Predicate<WebDriver> ajaxCallsCompleted() {
        return new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver driver) {
                if (isVaadinActive(driver)) {
                    return true;
                } else {
                    ensureBrowserRenderingTookPlace();
                    return false;
                }
            }

        };
    }

    protected static boolean isVaadinActive(WebDriver driver) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        return (Boolean) executor.executeScript(//
                        "if (window.vaadin) { " + //
                        " for (var client in window.vaadin.clients) { " + //
                        "  if (window.vaadin.clients[client].isActive()) { " + //
                        "   return false;" + //
                        "  }" + //
                        " }" + //
                        " return true;" + //
                        "} else {" + //
                        " return false;" + //
                        "}");
    }

    protected static void ensureBrowserRenderingTookPlace() {
        try {
            Thread.sleep(BROWSER_RENDERING_MS);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}
