package troubleshooting.layout;

import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.crud.CrudVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import ma.glasnost.orika.MapperFactory;

import troubleshooting.dao.Workflow;
import troubleshooting.provider.StepDataProvider;
import troubleshooting.provider.WorkflowDataProvider;
import troubleshooting.repo.StepRepository;
import troubleshooting.dao.Step;
import troubleshooting.repo.WorkflowRepository;
import troubleshooting.service.StepService;

public class CrudStepLayout extends VerticalLayout {
    private com.vaadin.flow.data.binder.Binder<Step> binder;
    private Crud<Step> crud;
    private StepDataProvider dataProvider;

    public CrudStepLayout(StepRepository stepRepository, StepService stepService, MapperFactory mapperFactory) {
        crud = new Crud<>(Step.class, createEntityEditor());
        Crud.removeEditColumn(crud.getGrid());

        crud.getGrid().addItemDoubleClickListener(
                e -> crud.edit(e.getItem(), Crud.EditMode.EXISTING_ITEM));

        dataProvider = new StepDataProvider(stepRepository);
        crud.setDataProvider(dataProvider);
        crud.addSaveListener(e -> dataProvider.persist(e.getItem()));
        crud.addDeleteListener(e -> dataProvider.delete(e.getItem()));

        crud.getGrid().removeColumnByKey("id");
        crud.getGrid().removeColumnByKey("workflows");

        crud.addThemeVariants(CrudVariant.NO_BORDER);
        add(crud);
    }

    public void reloadSteps(Long workflowId) {
        dataProvider.setWorkflowIdAndRefresh(workflowId);
    }

    private CrudEditor<Step> createEntityEditor() {
        TextField stepName = new TextField("Step name");
        FormLayout form = new FormLayout(stepName);
        binder = new Binder<>(Step.class);
        binder.bind(stepName, Step::getStepName, Step::setStepName);

        return new BinderCrudEditor<>(binder, form);
    }
}
