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
package org.richfaces.tests.metamer.bean.rich;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.richfaces.component.UISelect;
import org.richfaces.tests.metamer.Attributes;
import org.richfaces.tests.metamer.bean.abstractions.SelectValidationBean;
import org.richfaces.tests.metamer.bean.abstractions.StringInputValidationBean;
import org.richfaces.tests.metamer.model.Capital;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * Managed bean for rich:select.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@ManagedBean(name = "richSelectBean")
@SessionScoped
public class RichSelectBean extends SelectValidationBean implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RichSelectBean.class);
    private static final long serialVersionUID = -1L;

    private final String ajaxAttributes = "onbeforedomupdate,onbegin,oncomplete,status";// https://issues.jboss.org/browse/RF-10966
    private Capital capital;
    @ManagedProperty("#{model.capitals}")
    private List<Capital> capitals;
    private List<SelectItem> capitalsOptions = null;
    private String validatorMessage;

    /**
     * https://issues.jboss.org/browse/RF-10966
     */
    public String getAjaxAttributes() {
        return ajaxAttributes;
    }

    public Capital getCapital() {
        return capital;
    }

    public List<Capital> getCapitals() {
        return capitals;
    }

    public List<SelectItem> getCapitalsOptions() {
        return capitalsOptions;
    }

    public String getValidatorMessage() {
        return validatorMessage;
    }

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        LOGGER.debug("initializing bean " + getClass().getName());

        capitalsOptions = new ArrayList<SelectItem>();
        for (Capital capital : capitals) {
            capitalsOptions.add(new SelectItem(capital.getState(), capital.getState()));
        }
        attributes = Attributes.getComponentAttributesFromFacesConfig(UISelect.class, getClass());

        attributes.setAttribute("defaultLabel", "Click here to edit");
        attributes.setAttribute("enableManualInput", true);
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("required", true);
        attributes.setAttribute("requiredMessage", StringInputValidationBean.REQUIRED_VALIDATION_MSG);
        attributes.setAttribute("showButton", true);
        attributes.setAttribute("valueChangeListener", "valueChangeListener");

        // TODO has to be tested in another way
        attributes.remove("converter");
        attributes.remove("converterMessage");
        attributes.remove("validator");
        attributes.remove("validatorMessage");

        attributes.remove("var");
        attributes.remove("itemLabel");
        attributes.remove("itemValue");
        attributes.remove("autocompleteMethod");
        attributes.remove("autocompleteList");
    }

    public void setCapital(Capital capital) {
        this.capital = capital;
    }

    public void setCapitals(List<Capital> capitals) {
        this.capitals = capitals;
    }

    public void setCapitalsOptions(List<SelectItem> capitalsOptions) {
        this.capitalsOptions = capitalsOptions;
    }

    public void setValidatorMessage(String validatorMessage) {
        this.validatorMessage = validatorMessage;
    }

    public Collection<Capital> suggest(FacesContext facesContext, UIComponent component, final String prefix) {
        Collection<Capital> suggestions = Collections2.filter(capitals, new Predicate<Capital>() {
            @Override
            public boolean apply(Capital input) {
                if (prefix == null) {
                    return true;
                }
                return input.getState().toLowerCase().startsWith(prefix.toLowerCase());
            }
        });
        return suggestions;
    }
}
