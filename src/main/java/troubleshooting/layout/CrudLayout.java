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
import troubleshooting.provider.WorkflowDataProvider;
import troubleshooting.repo.StepRepository;
import troubleshooting.repo.WorkflowRepository;

public class CrudLayout extends VerticalLayout {
    private com.vaadin.flow.data.binder.Binder<Workflow> binder;

    public CrudLayout(WorkflowRepository workflowRepository,
                      StepRepository stepRepository, MapperFactory mapperFactory) {
        Crud<Workflow> crud = new Crud<>(Workflow.class, createEntityEditor());
        Crud.removeEditColumn(crud.getGrid());

        crud.getGrid().addItemDoubleClickListener(
                e -> crud.edit(e.getItem(), Crud.EditMode.EXISTING_ITEM));

        WorkflowDataProvider dataProvider = new WorkflowDataProvider(workflowRepository);
        crud.setDataProvider(dataProvider);
        crud.addSaveListener(e -> dataProvider.persist(e.getItem()));
        crud.addDeleteListener(e -> dataProvider.delete(e.getItem()));

        crud.getGrid().removeColumnByKey("id");
        crud.addThemeVariants(CrudVariant.NO_BORDER);
        add(crud);
    }


    private CrudEditor<Workflow> createEntityEditor() {
        TextField workflowName = new TextField("Workflow name");
        FormLayout form = new FormLayout(workflowName);
        binder = new Binder<>(Workflow.class);
        binder.bind(workflowName, Workflow::getWorkflowName, Workflow::setWorkflowName);


        return new BinderCrudEditor<>(binder, form);
    }
}