package troubleshooting;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import troubleshooting.component.StepComponent;
import troubleshooting.component.TabComponent;
import troubleshooting.repo.StepRepository;
import ma.glasnost.orika.MapperFactory;

@Route
@Theme(value = Lumo.class)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/menu-buttons.css", themeFor = "vaadin-button")
public class MainView extends VerticalLayout {
	private StepRepository stepRepository;
	private MapperFactory mapperFactory;

	public MainView(StepRepository stepRepository, MapperFactory mapperFactory) {
		this.stepRepository = stepRepository;
		StepComponent question = new StepComponent(stepRepository, mapperFactory);
		TabComponent tabComponent = new TabComponent();
		add(tabComponent);
		// build layout
		add(question);
	}

}
