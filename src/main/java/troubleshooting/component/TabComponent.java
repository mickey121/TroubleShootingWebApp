package troubleshooting.component;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

public class TabComponent extends HorizontalLayout {

    public TabComponent() {
        Tab tab1 = new Tab("Workflow");
        Tab tab2 = new Tab("Step");
        Tab tab3 = new Tab("View");
        Tabs tabs = new Tabs(tab1, tab2, tab3);
        tabs.setFlexGrowForEnclosedTabs(1);

        add(tabs);
    }
}
