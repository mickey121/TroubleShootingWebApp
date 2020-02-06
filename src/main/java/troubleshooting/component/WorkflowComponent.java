package troubleshooting.component;

//list of workflows that exists being fetched from backend
//should include two buttons: add workflow,

//name, last updated, and some other columns that can be
// used as placeholders for now,
//think about how user can add new workflow
//add workflow button to a new row
//save button

//grid with names of workflow, content of grid should be workflow name
//clicking on a workflow in the workflow grid list takes you to a step view
//step view looks like simple grid too, you can keep enter in your step
//this grid has two things extra: a checkbox saying is this final, and then a dropdown menu saying what are next steps
//from this step, can go to any of the already created steps
//no more options, only workflow and step view
//as soon as you check a step as final, it becomes an option in the backend

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Data;
import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import troubleshooting.MainView;
import troubleshooting.dao.Step;
import troubleshooting.dao.Workflow;
import troubleshooting.dto.StepDto;
import troubleshooting.dto.WorkflowDto;
import troubleshooting.repo.WorkflowRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Tag("div")
@SpringComponent
@UIScope
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WorkflowComponent extends Composite<Div> implements HasComponents {
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
     private Checkbox isFinal = new Checkbox("Do not call");
    private Label infoLabel;
    private Binder<WorkflowDto> binder;
    private WorkflowDto currWorkflow;
    private List<TextField> components;


    // private List<TextField> components;
    BoundMapperFacade<Workflow,WorkflowDto> mapper;

    //the original save button should make new row pop up in grid
//maybe change from grid to treegrid

    public WorkflowComponent() {
        VerticalLayout vbox = new VerticalLayout();
        HorizontalLayout hbox = new HorizontalLayout();

        //grid holds workflowDto,
        Grid<WorkflowDto> workflowDtoGrid = new Grid<>(WorkflowDto.class);
        List<WorkflowDto> WorkflowDtoList = new ArrayList<>();
        WorkflowDtoList.add(new WorkflowDto("workflowDto1"));
        WorkflowDtoList.add(new WorkflowDto("workflowDto2"));
        workflowDtoGrid.setItems(WorkflowDtoList);

        hbox.add(workflowDtoGrid);
        add(workflowDtoGrid);
        workflowDtoGrid.setHeight("300px");
        workflowDtoGrid.setWidth("1000px");
        workflowDtoGrid.setColumns("workflowName");

        workflowDtoGrid.setSelectionMode(Grid.SelectionMode.NONE);

//        formLayout.add(name, age);
//        formLayout.addFormItem(name, "Name");
//        formLayout.addFormItem(age, "Age");
//        formLayout.addFormItem(column, "Column");
//
//        workflowDtoGrid.addItemClickListener(
//                event -> {
//                    name.setText(event.getItem().getFirstName());
//                    age.setText(String.valueOf(event.getItem().getAge()));
//                    column.setText(event.getColumn().getKey());
//                });
    }

    public WorkflowComponent(WorkflowDto workflowDto, MapperFactory mapperFactory) {
        binder = new Binder<>();
        currWorkflow = new WorkflowDto("workflowDto obj from first constructor");
        infoLabel = new Label();
        workflowName = new TextField();
        // components = new LinkedList<>();
        this.mapper = mapperFactory.getMapperFacade(Workflow.class, WorkflowDto.class);
        addForm();
        addListener();
    }

    public WorkflowComponent(WorkflowRepository workflowRepository, MapperFactory mapperFactory) {
        binder = new Binder<>();
        currWorkflow = new WorkflowDto("workflowDto obj from second constructor");
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
}

