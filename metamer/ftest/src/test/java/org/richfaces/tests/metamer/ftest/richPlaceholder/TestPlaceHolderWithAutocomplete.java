/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richPlaceholder;

import org.jboss.arquillian.ajocado.dom.Event;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.testng.annotations.Test;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestPlaceHolderWithAutocomplete extends AbstractPlaceholderJSFTest {

    @FindBy(css = INPUT1_ID + " > span > input")
    WebElement input1;
    @FindBy(css = INPUT2_ID + " > span > input")
    WebElement input2;

    public TestPlaceHolderWithAutocomplete() {
        super("autocomplete");
    }

    @Override
    WebElement getInput1() {
        return input1;
    }

    @Override
    WebElement getInput2() {
        return input2;
    }

    @Override
    protected void clickOnInput1() {
        fireEvent(getInput1(), Event.FOCUS);
    }

    @Override
    protected void clickOnInput2() {
        fireEvent(getInput2(), Event.FOCUS);
    }

    @Test
    @RegressionTest({ "https://issues.jboss.org/browse/RF-12623", "https://issues.jboss.org/browse/RF-12625" })
    @Override
    public void testAjaxSubmit() {
        super.testAjaxSubmit();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12623")
    @Override
    public void testClickOnInputWithPlaceholder() {
        super.testClickOnInputWithPlaceholder();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12623")
    @Override
    public void testDeleteTextFromInputWithPlaceholder() {
        super.testDeleteTextFromInputWithPlaceholder();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12623")
    @Override
    public void testHTTPSubmit() {
        super.testHTTPSubmit();
    }

    @Test(groups = "4.Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-12650")
    @RegressionTest("https://issues.jboss.org/browse/RF-12623")
    @Override
    public void testRendered() {
        super.testRendered();
    }

    @Test
    @Override
    public void testSelector() {
        super.testSelector();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12621")
    @Override
    public void testSelectorEmpty() {
        super.testSelectorEmpty();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12623")
    @Override
    public void testStyleClass() {
        super.testStyleClass();
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-12623")
    @Override
    public void testTypeToInputWithPlaceholder() {
        super.testTypeToInputWithPlaceholder();
    }
}
