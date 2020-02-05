package troubleshooting.component;

//list of workflows that exists being fetched from backend
//should include two buttons: add workflow,

//name, last updated, and some other columns that can be
// used as placeholders for now,
//think about how user can add new workflow
//add workflow button to a new row
//save button

//grid with names of workflow, content of grid should be workflow name

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.HasComponents;

import com.vaadin.flow.component.grid.Grid;
import troubleshooting.dao.Step;

import java.util.ArrayList;
import java.util.List;

@Tag("div")
public class WorkflowComponent extends Composite<Div> implements HasComponents {
    private Grid grid;
    private VerticalLayout vbox;
    private HorizontalLayout hbox;
    private List<Row> rowList;

    public WorkflowComponent() {
        VerticalLayout vbox = new VerticalLayout();
        HorizontalLayout hbox = new HorizontalLayout();

//        Grid<String> grid = new Grid<>(String.class);
//
//        grid.addColumn("1");
//        grid.addColumn("2");
//        grid.addColumn("3");
        Grid<Row> grid = new Grid<>(Row.class);

        rowList = new ArrayList<>();
        rowList.add(new Row("first row"));
        rowList.add(new Row("second row"));
        rowList.add(new Row("third row"));
        rowList.add(new Row("fourth row"));
        grid.setItems(rowList);

//        grid.setColumns("name", "firstName");

//        grid.setHeight("300px");
//        grid.setColumns("id", "firstName", "lastName");
//        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);
//        add(grid);

        add(grid);
    }

    public class Row {
        public String key;
        String elem1;
        String elem2;
        String elem3;
        String elem4;

        public Row(String arg) {
            key = arg;
            elem1 = key + ": elem1";
            elem2 = key + ": elem2";
            elem3 = key + ": elem3";
            elem4 = key + ": elem4";
        }
     }
}
