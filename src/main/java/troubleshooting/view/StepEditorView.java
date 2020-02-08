package troubleshooting.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import troubleshooting.component.StepComponent;
import troubleshooting.component.TabComponent;
import troubleshooting.repo.StepRepository;
import ma.glasnost.orika.MapperFactory;


@Route(value="step")
@Theme(value = Lumo.class)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/menu-buttons.css", themeFor = "vaadin-button")
public class StepEditorView extends VerticalLayout implements RouterLayout {
    private StepRepository stepRepository;
    private MapperFactory mapperFactory;

    private TextField filter;

    private Button addNewBtn;

    public StepEditorView(StepRepository stepRepository, MapperFactory mapperFactory) {
        this.stepRepository = stepRepository;
        this.mapperFactory = mapperFactory;
        this.filter = new TextField();
        this.addNewBtn = new Button("New customer", VaadinIcon.PLUS.create());
        addForm();
    }

    private void addForm() {
        StepComponent question = new StepComponent(stepRepository, mapperFactory, null);
        TabComponent tabComponent = new TabComponent();
        add(tabComponent);
        // build layout
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, question);
    }

    private void addMenuElement(
            Class<? extends Component> navigationTarget,
            String name) {
        // implementation omitted
    }
}
