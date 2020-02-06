package troubleshooting;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import troubleshooting.component.StepComponent;
import troubleshooting.component.TabComponent;
import troubleshooting.component.WorkflowComponent;
import troubleshooting.layout.AccordionLayout;
import troubleshooting.layout.CrudStepLayout;
import troubleshooting.layout.CrudWorkflowLayout;
import troubleshooting.repo.StepRepository;
import ma.glasnost.orika.MapperFactory;
import troubleshooting.repo.WorkflowRepository;
import troubleshooting.service.StepService;


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

	public MainView(StepRepository stepRepository, WorkflowRepository workflowRepository,
					StepService stepService, MapperFactory mapperFactory) {
		this.stepRepository = stepRepository;
		this.accordion = new Accordion();
		StepComponent question = new StepComponent(stepRepository, mapperFactory);
		TabComponent tabComponent = new TabComponent();
		CrudWorkflowLayout crudWorkflowLayout = new CrudWorkflowLayout(workflowRepository, stepRepository, mapperFactory, stepService);
		CrudStepLayout crudStepLayout = new CrudStepLayout(stepRepository, mapperFactory);
		WorkflowComponent workflowComponent = new WorkflowComponent();

		accordion.add("wf1", new TextField());
		accordion.add("wf2", new AccordionLayout(2));
		addAndExpand(tabComponent, question, crudWorkflowLayout, crudStepLayout);

//		crudStepLayout.

//		crud.getGrid().addItemDoubleClickListener(
//				e -> crud.edit(e.getItem(), Crud.EditMode.EXISTING_ITEM));

		//addAndExpand(tabComponent, question, workflowComponent);
//		add(accordion);

		// build layout
		addListener();
	}

	private void addListener() {
		//click listeners for switching tabs

	}
}
