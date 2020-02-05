package troubleshooting.component;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
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
    //private VerticalLayout optionFormLayout = new VerticalLayout();
    private TextField stepName;
    private int index = 1;
    private int ci = 2;
    private Button save = new Button("Save");
    private Button reset = new Button("Reset");
    private Button add = new Button("Add");
    private Button remove = new Button("Remove");
    private Checkbox isFinal = new Checkbox("Do not call");
    private Label infoLabel;
    private Binder<StepDto> binder;
    private StepDto currStep;

    private List<TextField> components;
    BoundMapperFacade<Step,StepDto> mapper;

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

        bindForm();

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
        save.addClickListener(event -> {
                if (binder.validate().isOk()) {
                infoLabel.setText("Saved bean values: " + currStep);
                Step s = mapper.mapReverse(currStep);
                stepRepository.save(s);
                UI ui = UI.getCurrent();
                ui.navigate(ui.getRouter().getUrl(MainView.class));

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
