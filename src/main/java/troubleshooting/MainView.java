package troubleshooting;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import troubleshooting.component.StepComponent;
import troubleshooting.component.TabComponent;
import troubleshooting.component.WorkflowComponent;
import troubleshooting.layout.AccordionLayout;
import troubleshooting.layout.CrudLayout;
import troubleshooting.repo.StepRepository;
import ma.glasnost.orika.MapperFactory;
import troubleshooting.repo.WorkflowRepository;


//each tab click event should change the subview below
//build a grid for workflow
//show a grid (vaadin object) under the workflow tab

@Route
@Theme(value = Lumo.class)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/menu-buttons.css", themeFor = "vaadin-button")
public class MainView extends VerticalLayout implements RouterLayout {
	private StepRepository stepRepository;
	private MapperFactory mapperFactory;
	private Accordion accordion;

	public MainView(StepRepository stepRepository, WorkflowRepository workflowRepository, MapperFactory mapperFactory) {
		this.stepRepository = stepRepository;
		this.accordion = new Accordion();
		StepComponent question = new StepComponent(stepRepository, mapperFactory);
		TabComponent tabComponent = new TabComponent();
		CrudLayout crudLayout = new CrudLayout(workflowRepository, stepRepository, mapperFactory);
		WorkflowComponent workflowComponent = new WorkflowComponent();

		accordion.add("wf1", new AccordionLayout(2));
		accordion.add("wf2", new AccordionLayout(2));
		addAndExpand(tabComponent, question, crudLayout);

		//addAndExpand(tabComponent, question, workflowComponent);


		// build layout
		addListener();
	}

	private void addListener() {
		//click listeners for switching tabs

	}
}
