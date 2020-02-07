package troubleshooting.component;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
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
import troubleshooting.MainView;
import troubleshooting.dao.Step;
import troubleshooting.dto.OptionDto;
import troubleshooting.dto.StepDto;
import troubleshooting.repo.StepRepository;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag("div")
@SpringComponent
@UIScope
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StepComponent extends Composite<Div> implements HasComponents {

    private StepRepository stepRepository;
    private FormLayout stepFormLayout = new FormLayout();
    private FormLayout stepViewLayout = new FormLayout();
    //private VerticalLayout optionFormLayout = new VerticalLayout();
    private TextField stepName;
    private int index = 1;
    private int ci = 2;
    private Button save = new Button("Save");
    private Button reset = new Button("Reset");
    private Button add = new Button("Add");
    private Button remove = new Button("Remove");
    private Button previous = new Button("Previous");
    private Button next = new Button("Next");

    private Checkbox isFinal = new Checkbox("Do not call");
    private Label infoLabel;
    private Binder<StepDto> binder;
    private StepDto currStep;
    public String name;

    TextField stepTitle;
    TextField stepOptions;
    int displayIndex = 0;
    Step displayedStep;

    private List<TextField> components;
    BoundMapperFacade<Step,StepDto> mapper;

    public StepComponent(String arg) {
        this.name = arg;
    }

    public StepComponent(StepDto stepDto, MapperFactory mapperFactory) {
        binder = new Binder<>();
        currStep = new StepDto();
        infoLabel = new Label();
        stepName = new TextField();
        components = new LinkedList<>();
        this.mapper = mapperFactory.getMapperFacade(Step.class, StepDto.class);
        addForm();
        addListener();
    }

    public StepComponent(StepRepository stepRepository, MapperFactory mapperFactory) {
        binder = new Binder<>();
        currStep = new StepDto();
        infoLabel = new Label();
        stepName = new TextField();
        components = new LinkedList<>();
        this.stepRepository = stepRepository;
        this.mapper = mapperFactory.getMapperFacade(Step.class, StepDto.class);
        addForm();
        addListener();
    }

    public FormLayout getFormLayout() {
        return stepFormLayout;
    }

    public FormLayout getStepViewLayout() {
        return stepViewLayout;
    }

    public Binder<StepDto> getBinder() {
        return binder;
    }

    private void addForm() {
        stepFormLayout.setWidth("1000px");
        stepFormLayout.getStyle().set("border", "2px solid #9E9E9E");
        stepFormLayout.getStyle().set("border-radius", "8px");
        stepFormLayout.getStyle().set("padding", "1%");
        stepFormLayout.add(infoLabel);
        stepFormLayout.add(stepName, 1);
        stepFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("1000px", 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));

        // Button bar
        HorizontalLayout actions = new HorizontalLayout();
        actions.add(add, remove, save, reset);
        //save.getStyle().set("marginRight", "10px");
        stepName.setPlaceholder("Step Name");
        stepName.setRequiredIndicatorVisible(true);
        stepFormLayout.add(actions, 2);

        stepTitle = new TextField();
        stepOptions = new TextField();
        stepViewLayout.setWidth("1000px");
        stepViewLayout.getStyle().set("border", "2px solid #9E9E9E");
        stepViewLayout.getStyle().set("border-radius", "8px");
        stepViewLayout.getStyle().set("padding", "1%");
        stepViewLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("1000px", 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));

        stepViewLayout.addComponentAsFirst(stepTitle);
        stepViewLayout.addComponentAsFirst(stepOptions);
        stepTitle.setValue("initial step name");
        stepOptions.setValue("initial step option");
        stepViewLayout.addComponentAsFirst(new Label("Options"));

        // Button bar
        HorizontalLayout stepViewActions = new HorizontalLayout();
        stepViewActions.add(previous, next);
        stepViewLayout.add(stepViewActions, 2);

        bindForm();

        add(stepViewLayout);
        add(stepFormLayout);
    }

    private void bindForm() {
        binder.readBean(currStep);
        binder.forField(stepName)
                .withValidator(new StringLengthValidator(
                        "Please add the currStep name", 1, null))
                .bind(StepDto::getStepName, StepDto::setStepName);

        if (!currStep.getOptions().isEmpty()) {
            for(OptionDto currOption : currStep.getOptions()) {
                TextField textField = new TextField(Integer.toString(index));
                binder.forField(textField).bind(step -> {
                        return step.getOptions().get(index++).getText();
                    }, (step, option1) -> {
                    OptionDto o = new OptionDto();
                    o.setText(option1);
                    step.addOption(o);
                });
                stepFormLayout.addComponentAtIndex(ci, textField);
                components.add(textField);
            }
        }

        binder.bind(isFinal, StepDto::isFinal, StepDto::setFinal);
        binder.setBean(currStep);
    }

    private void addListener() {
        // Click listeners for the buttons
        previous.addClickListener(event -> {
            if (displayIndex >= 0) {
                stepOptions.setValue(displayedStep.options.get(displayIndex).getText());
            }
            if (displayIndex > 0) {
                displayIndex--;
            }
        });
        next.addClickListener(event -> {
            if (displayIndex < displayedStep.options.size()) {
                stepOptions.setValue(displayedStep.options.get(displayIndex).getText());
            }
            if (displayIndex < displayedStep.options.size() - 1) {
                displayIndex++;
            }
        });

        save.addClickListener(event -> {
                if (binder.validate().isOk()) {
                infoLabel.setText("Saved bean values: " + currStep);
                Step s = mapper.mapReverse(currStep);
                stepRepository.save(s);
                UI ui = UI.getCurrent();
                ui.navigate(ui.getRouter().getUrl(MainView.class));

                stepTitle.setValue(s.getStepName());
                displayIndex = 0;
                displayedStep = s;
            } else {
                BinderValidationStatus<StepDto> validate = binder.validate();
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
            bindOption(option, index++);
            stepFormLayout.addComponentAtIndex(index, option);
            components.add(option);
        });
        remove.addClickListener(event -> {
            if(components.size() > 0) {
                TextField c = components.remove(components.size() - 1);
                binder.removeBinding(c);
                stepFormLayout.remove(c);
                index--;
                ci--;
            }
        });
    }

    private void bindOption(TextField option, int i) {
        binder.forField(option).bind(step -> {
                return "";
            }, (step, option1) -> {
            OptionDto o = new OptionDto();
            o.setText(option1);
            step.addOption(o);
        });
    }
}
