/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.tests.metamer.ftest.richHotKey;

import static org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.ValuesFrom.FROM_ENUM;
import static org.testng.Assert.assertEquals;

import java.util.Set;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.WebDriver;
import org.richfaces.tests.metamer.ftest.extension.attributes.coverage.annotations.CoversAttributes;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.richfaces.tests.metamer.ftest.extension.configurator.use.annotation.UseWithField;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestHotKeyAttributes extends AbstractHotKeyTest {

    private KeysEnum key;

    public enum KeysEnum {

        CONTROL_Z("ctrl+z"),
        T("t"),
        CTRL_X("ctrl+x"),
        ALT_CONTROL_X("alt+ctrl+x");

        private final String keysString;

        private KeysEnum(String keysString) {
            this.keysString = keysString;
        }

        @Override
        public String toString() {
            return keysString;
        }
    }

    /**
     * Will try to create a new window with hotkey. If preventDefault is true, then no window will be created. Should work at
     * least in IE, Chrome and Firefox.
     */
    private void checkPreventDefault(boolean preventDefault) {
        String originalWindow = driver.getWindowHandle();
        firstHotkeyAttributes.set(HotKeyAttributes.preventDefault, preventDefault);
        firstHotkeyAttributes.set(HotKeyAttributes.key, "ctrl+n");
        getHotkey1().setHotkey("ctrl+n");
        getHotkey1().invoke();
        try {
            if (!preventDefault) {
                Graphene.waitModel().until(new Predicate<WebDriver>() {
                    @Override
                    public boolean apply(WebDriver input) {
                        return driver.getWindowHandles().size() == 2;
                    }

                    @Override
                    public String toString() {
                        return "2 windows should be opened now, but have: " + driver.getWindowHandles().size();
                    }
                });
            } else {
                waiting(2000L);
                assertEquals(driver.getWindowHandles().size(), 1, "Only one window should be opened.");
            }
        } finally {
            closeAllWindowsExceptOriginalAndSwitchToIt(originalWindow);
        }
        checkEvent("onkeydown", 1);
        checkEvent("onkeyup", 1);
    }

    private void closeAllWindowsExceptOriginalAndSwitchToIt(String originalWindowHandle) {
        Set<String> windowHandles = driver.getWindowHandles();
        windowHandles.remove(originalWindowHandle);

        for (String window : windowHandles) {
            driver.switchTo().window(window).close();
        }

        Graphene.waitModel().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver input) {
                return driver.getWindowHandles().size() == 1;
            }

            @Override
            public String toString() {
                return "Only one window should be opened now, but have: " + driver.getWindowHandles().size();
            }
        });

        driver.switchTo().window(originalWindowHandle);
    }

    @Test
    @CoversAttributes("enabledInInput")
    public void enabledInInput() {
        // true
        getHotkey1().invoke(getFirstInput().advanced().getInputElement());
        checkEvents(1, 0);
        clearHotKeyEvents();
        // false
        firstHotkeyAttributes.set(HotKeyAttributes.enabledInInput, false);
        getHotkey1().invoke(getFirstInput().advanced().getInputElement());
        checkEvents(0, 0);
    }

    @Test
    @CoversAttributes("key")
    @UseWithField(field = "key", valuesFrom = FROM_ENUM, value = "")
    public void testKey() {
        firstHotkeyAttributes.set(HotKeyAttributes.key, key.keysString);
        getHotkey1().setHotkey(key.toString());
        getHotkey1().invoke();
        checkEvents(1, 0);
        clearHotKeyEvents();
    }

    @Test
    @CoversAttributes({ "onkeydown", "onkeyup" })
    public void testOnkeydownOnkeyup() {
        // these events are already binded, they add message to a4j:log on the page
        getHotkey1().invoke();
        checkEvents(1, 0);
        getHotkey2().invoke();
        checkEvents(1, 1);
    }

    @Test
    @CoversAttributes("preventDefault")
    public void testPreventDefaultFalse() {
        checkPreventDefault(false);
    }

    @Test
    @CoversAttributes("preventDefault")
    public void testPreventDefaultTrue() {
        checkPreventDefault(true);
    }

    @Test
    @CoversAttributes("rendered")
    @Templates(value = "plain")
    public void testRendered() {
        firstHotkeyAttributes.set(HotKeyAttributes.rendered, false);
        assertNotPresent(getHotkey1().advanced().getRootElement(), "Hotkey 1 should not be present on page.");
        assertPresent(getHotkey2().advanced().getRootElement(), "Hotkey 2 should be present on page.");
    }

    @Test
    @CoversAttributes("selector")
    public void testSelector() {
        firstHotkeyAttributes.set(HotKeyAttributes.selector, "input.first-input");
        getHotkey1().setSelector("input.first-input");
        getHotkey1().invoke();// invoke on element found by selector
        checkEvents(1, 0);
        getHotkey1().invoke(getSecondInput().advanced().getInputElement());
        checkEvents(1, 0);// no change
        getHotkey1().invoke();// invoke on element found by selector
        checkEvents(2, 0);
    }
}
