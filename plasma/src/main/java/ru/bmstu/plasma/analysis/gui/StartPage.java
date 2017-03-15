package ru.bmstu.plasma.analysis.gui;

import javax.servlet.annotation.WebServlet;

import ru.bmstu.plasma.analysis.db.PlasmaDB;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class StartPage extends UI {
    @Override
    protected void init(VaadinRequest vaadinRequest) {        
    	
    	//******************************************************************************************************************
    	// Интерфейс входа в систему
    	GridLayout gridLayout = new GridLayout(3, 3);
    	gridLayout.setSizeFull();
    	gridLayout.setSpacing(true);
    	gridLayout.setMargin(true);
    	
    	FormLayout formLayout = new FormLayout();
        formLayout.setSizeUndefined();
        
        Label labelMain = new Label();
        labelMain.setCaption("Московский государственный технический университет имени Н.Э. Баумана");
        
        Label labelEnter = new Label();
        labelEnter.setCaption("Вход в систему");
        formLayout.addComponent(labelEnter);
        formLayout.setComponentAlignment(labelEnter, Alignment.MIDDLE_LEFT);
        
        TextField userName = new TextField();
        userName.setCaption("Имя");
        formLayout.addComponent(userName);
        formLayout.setComponentAlignment(userName, Alignment.MIDDLE_LEFT);
        
        PasswordField password = new PasswordField();
        password.setCaption("Пароль");
        formLayout.addComponent(password);
        formLayout.setComponentAlignment(password, Alignment.MIDDLE_LEFT);
        
        Button enter = new Button();
        enter.setCaption("Войти");
        enter.setClickShortcut(KeyCode.ENTER);
        formLayout.addComponent(enter);
        formLayout.setComponentAlignment(enter, Alignment.MIDDLE_LEFT);
        
        Notification loginError = new Notification("Неправильное имя пользователя или пароль");
        
        gridLayout.addComponent(labelMain, 0, 0, 2, 0);
        gridLayout.setComponentAlignment(labelMain, Alignment.MIDDLE_LEFT); 
        
        gridLayout.addComponent(formLayout, 1, 1);
        gridLayout.setComponentAlignment(formLayout, Alignment.MIDDLE_CENTER);    
        
        setContent(gridLayout);
        // Интерфейс входа в систему
        //******************************************************************************************************************
        
        
        //******************************************************************************************************************
    	// Интерфейс списка экспериментов
        GridLayout secondPage = new GridLayout(1, 2);
        secondPage.setWidth("100%");
        secondPage.setSpacing(true);
        secondPage.setMargin(true);
    	
    	Table table = new Table("Список экспериментов");
    	table.setSelectable(true);

    	// Определение столбцов таблицы
    	table.addContainerProperty("Номер", Integer.class, null);
    	table.addContainerProperty("Название",  String.class, null);
    	table.addContainerProperty("Описание",  String.class, null);
    	table.setColumnWidth("Номер", 100);
    	
    	// Добавление сток в таблицу
    	for (int i = 1; i <= 5; i++)
    		table.addItem(new Object[]{i, "Эксперимент №" + i, "Спектральный анализ плазмы"}, i);

    	// Показывать только 5 строк в таблице
    	table.setPageLength(5);
    	
    	// Размер таблицы
        table.setWidth("100%");
        //table.setHeight("100%");
        
        Button selectExperiment = new Button("Выбрать");
        selectExperiment.setEnabled(false);
        
        secondPage.addComponent(table, 0, 0);
        secondPage.setComponentAlignment(table, Alignment.MIDDLE_CENTER);
        
        secondPage.addComponent(selectExperiment, 0, 1);
        secondPage.setComponentAlignment(selectExperiment, Alignment.TOP_RIGHT);
        
        // Делаем переход на следующую страницу
        BrowserWindowOpener popupOpener = new BrowserWindowOpener(Experiment.class);
        popupOpener.setWindowName("_self");
        popupOpener.extend(selectExperiment);
        // Интерфейс списка экспериментов
        //******************************************************************************************************************
    	
        //******************************************************************************************************************
    	// Обработчики событий
        enter.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	if (("admin".equals(userName.getValue()) && "admin".equals(password.getValue())) 
            			|| PlasmaDB.checkAccount(userName.getValue(), password.getValue()))
                {
                	VaadinSession.getCurrent().setAttribute(userName.getValue(), password.getValue());
                	setContent(secondPage);
                }
            	else
            		loginError.show(Page.getCurrent());
            }
        });
        
        selectExperiment.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	VaadinSession.getCurrent().setAttribute("experimentNumber", "1");
            }
        });
        
        table.addItemClickListener(new ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
            	if (event.isDoubleClick())
            		return;
            	
            	if (event.getButton() == ItemClickEvent.BUTTON_RIGHT)
            		return;
            	
            	String s1 = "" + event.getItemId();
            	String s2 = "" + table.getValue();
            	if (s1.equals(s2))
            		selectExperiment.setEnabled(false);
            	else
            		selectExperiment.setEnabled(true);
            }
        });
    }
    // Обработчики событий
    //******************************************************************************************************************

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = StartPage.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
