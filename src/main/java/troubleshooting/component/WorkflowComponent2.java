/***package troubleshooting.component;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import troubleshooting.MainView;
import troubleshooting.dao.Workflow;
import troubleshooting.dto.StepDto;
import troubleshooting.dto.WorkflowDto;
import troubleshooting.repo.WorkflowRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Tag("div")
@SpringComponent
@UIScope
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WorkflowComponent2 extends Composite<Div> implements HasComponents {

    private WorkflowRepository workflowRepository;
    private FormLayout workflowFormLayout = new FormLayout();
    //private VerticalLayout optionFormLayout = new VerticalLayout();
    private TextField workflowName;
    private int index = 1;
    private int ci = 2;
    private Button save = new Button("Save");
    private Button reset = new Button("Reset");
    private Button add = new Button("Add");
    private Button remove = new Button("Remove");
   // private Checkbox isFinal = new Checkbox("Do not call");
    private Label infoLabel;
    private Binder<WorkflowDto> binder;
    private WorkflowDto currWorkflow;

   // private List<TextField> components;
    BoundMapperFacade<Workflow,WorkflowDto> mapper;

    public WorkflowComponent2(WorkflowDto workflowDto, MapperFactory mapperFactory) {
        binder = new Binder<>();
        currWorkflow = new WorkflowDto();
        infoLabel = new Label();
        workflowName = new TextField();
       // components = new LinkedList<>();
        this.mapper = mapperFactory.getMapperFacade(Workflow.class, WorkflowDto.class);
        addForm();
        addListener();
    }

    public WorkflowComponent2(WorkflowRepository workflowRepository, MapperFactory mapperFactory) {
        binder = new Binder<>();
        currWorkflow = new WorkflowDto();
        infoLabel = new Label();
        workflowName = new TextField();
       // components = new LinkedList<>();
        this.workflowRepository = workflowRepository;
        this.mapper = mapperFactory.getMapperFacade(Workflow.class, WorkflowDto.class);
        addForm();
        addListener();
    }

    private void addForm() {
        workflowFormLayout.setWidth("1000px");
        workflowFormLayout.getStyle().set("border", "2px solid #9E9E9E");
        workflowFormLayout.getStyle().set("border-radius", "8px");
        workflowFormLayout.getStyle().set("padding", "1%");
        workflowFormLayout.add(infoLabel);
        workflowFormLayout.add(workflowName, 1);
        workflowFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("1000px", 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));


        // Button bar
        HorizontalLayout actions = new HorizontalLayout();
        actions.add(add, remove, save, reset);
        //save.getStyle().set("marginRight", "10px");
        workflowName.setPlaceholder("Workflow Name");
        workflowName.setRequiredIndicatorVisible(true);
        workflowFormLayout.add(actions, 2);

        bindForm();

        add(workflowFormLayout);
    }

    private void bindForm() {
        binder.readBean(currWorkflow);
        binder.forField(workflowName)
                .withValidator(new StringLengthValidator(
                        "Please add the currWorkflow name", 1, 250))
                .bind(WorkflowDto::getWorkflowName, WorkflowDto::setWorkflowName);

        if (!currWorkflow.getSteps().isEmpty()) {
            for(StepDto currStep : currWorkflow.getSteps()) {
                TextField textField = new TextField(Integer.toString(index));
                binder.forField(textField).bind(workflow -> {
                        return workflow.getSteps().get(index++).getStepName();
                    }, (workflow, step1) -> {
                    StepDto s = new StepDto();
                    s.setStepName(step1);
                    workflow.addStep(s);
                });
                workflowFormLayout.addComponentAtIndex(ci, textField);
                components.add(textField);
            }
        }

        binder.bind(isFinal, WorkflowDto::isFinal, WorkflowDto::setFinal);
        binder.setBean(currWorkflow);
    }

    private void addListener() {
        // Click listeners for the buttons
        save.addClickListener(event -> {
                if (binder.validate().isOk()) {
                infoLabel.setText("Saved bean values: " + currWorkflow);
                Workflow s = mapper.mapReverse(currWorkflow);
                workflowRepository.save(s);
                UI ui = UI.getCurrent();
                ui.navigate(ui.getRouter().getUrl(MainView.class));

            } else {
                BinderValidationStatus<WorkflowDto> validate = binder.validate();
                String errorText = validate.getFieldValidationStatuses()
                        .stream().filter(BindingValidationStatus::isError)
                        .map(BindingValidationStatus::getMessage)
                        .map(Optional::get).distinct()
                        .collect(Collectors.joining(", "));
                infoLabel.setText("There are errors: " + errorText);
            }
        });
        reset.addClickListener(event -> {
            // clear fields by setting null
            binder.readBean(null);
            infoLabel.setText("");
            isFinal.setValue(false);
        });
        add.addClickListener(event -> {
            TextField option = new TextField(Integer.toString(index));
            option.setId(Integer.toString(index));
            option.setRequired(true);
            bindStep(option, index++);
            workflowFormLayout.addComponentAtIndex(index, option);
            components.add(option);
        });
        remove.addClickListener(event -> {
            if(components.size() > 0) {
                TextField c = components.remove(components.size() - 1);
                binder.removeBinding(c);
                workflowFormLayout.remove(c);
                index--;
                ci--;
            }
        });
    }

    private void bindStep(TextField step, int i) {
        binder.forField(step).bind(workflow -> {
                return "";
            }, (workflow, step1) -> {
            StepDto o = new StepDto();
            o.setStepName(step1);
            workflow.addStep(o);
        });
    }
} ***/
