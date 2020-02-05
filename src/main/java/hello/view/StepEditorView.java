package hello.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import hello.component.StepComponent;
import hello.component.TabComponent;
import hello.repo.StepRepository;
import ma.glasnost.orika.MapperFactory;


@Route(value="step")
@Theme(value = Lumo.class)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/menu-buttons.css", themeFor = "vaadin-button")
public class StepEditorView extends Div {
    private StepRepository stepRepository;
    private MapperFactory mapperFactory;

    private TextField filter;

    private Button addNewBtn;

    public void StepEditorView(StepRepository stepRepository, MapperFactory mapperFactory) {
        this.stepRepository = stepRepository;
        this.mapperFactory = mapperFactory;
        this.filter = new TextField();
        this.addNewBtn = new Button("New customer", VaadinIcon.PLUS.create());
        addForm();
    }

    private void addForm() {
        setVisible(true);
        StepComponent question = new StepComponent(stepRepository, mapperFactory);
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
