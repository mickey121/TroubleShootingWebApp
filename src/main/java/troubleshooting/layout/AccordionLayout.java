package troubleshooting.layout;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;


@SpringComponent
@UIScope
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AccordionLayout extends VerticalLayout {
    Accordion accordion;
    public AccordionLayout(int level) {
        accordion = new Accordion();
        if (level > 0) {
            accordion.add("11", new AccordionLayout(--level));
            accordion.add("22", new AccordionLayout(level));
            add(accordion);
        }
    }
}
