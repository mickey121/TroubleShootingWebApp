package troubleshooting.component;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import troubleshooting.dao.Step;


@Tag("div")
public class OptionComponent extends Composite<Div> {
    private Label label;
    private TextField option;

    public OptionComponent(int index) {
        Step step = new Step();
        label = new Label();
        option = new TextField(Integer.toString(index));
        addForm();
    }

    private void addForm() {
        Binder<Step> binder = new Binder<>();
        HorizontalLayout layout = new HorizontalLayout();
        option.setClearButtonVisible(true);
        option.setValueChangeMode(ValueChangeMode.EAGER);
        option.setRequiredIndicatorVisible(true);

        binder.forField(option)
                .withValidator(new StringLengthValidator("Option can not be empty", 1, null))
                .bind(Step::getDefaultOption, null);

        layout.add(option);

        getContent().add(label, option);
    }
}
