package troubleshooting.component;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import troubleshooting.view.StepEditorView;

public class TabComponent extends HorizontalLayout {

    public TabComponent() {
        Tab tab1 = new Tab("Workflow");
        Tab tab2 = new Tab("Step");
        Tab tab3 = new Tab("View");
        Tabs tabs = new Tabs(tab1, tab2, tab3);
        tabs.setFlexGrowForEnclosedTabs(1);
        tab1.add(new RouterLink("Home", StepEditorView.class));
        tab2.add(new RouterLink("Home", StepEditorView.class));
        tab3.add(new RouterLink("Home", StepEditorView.class));
        add(tabs);
    }
}
