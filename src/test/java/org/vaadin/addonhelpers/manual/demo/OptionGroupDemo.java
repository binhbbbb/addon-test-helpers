package org.vaadin.addonhelpers.manual.demo;

import com.vaadin.ui.Component;
import org.vaadin.addonhelpers.AbstractTest;

import com.vaadin.v7.ui.*;

public class OptionGroupDemo extends AbstractTest {

    @Override
    public Component getTestComponent() {
        // A single-select radio button group
        OptionGroup single = new OptionGroup("Single Selection");
        single.addItems("Single", "Sola", "Yksi");
        single.setItemEnabled("Sola", false);
        single.setId("single-optiongroup");

        // A multi-select check box group
        OptionGroup multi = new OptionGroup("Multiple Selection");
        multi.setMultiSelect(true);
        multi.addItems("Many", "Muchos", "Monta");
        multi.setItemEnabled("Monta", false);
        multi.setId("multi-optiongroup");

        return new VerticalLayout(single, multi);
    }
}
