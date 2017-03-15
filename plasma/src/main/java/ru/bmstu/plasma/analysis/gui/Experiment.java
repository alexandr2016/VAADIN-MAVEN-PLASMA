package ru.bmstu.plasma.analysis.gui;

import javax.servlet.annotation.WebServlet;

import ru.bmstu.plasma.analysis.channel.ExperimentData;
import ru.bmstu.plasma.analysis.element.ArrayOfElements;
import ru.bmstu.plasma.analysis.element.ElementSpectrLines;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.ChartOptions;
import com.vaadin.addon.charts.PointClickEvent;
import com.vaadin.addon.charts.PointClickListener;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.HorizontalAlign;
import com.vaadin.addon.charts.model.Lang;
import com.vaadin.addon.charts.model.LayoutDirection;
import com.vaadin.addon.charts.model.Legend;
import com.vaadin.addon.charts.model.Marker;
import com.vaadin.addon.charts.model.MarkerSymbolEnum;
import com.vaadin.addon.charts.model.PlotOptionsColumn;
import com.vaadin.addon.charts.model.PlotOptionsLine;
import com.vaadin.addon.charts.model.VerticalAlign;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.ZoomType;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ContextClickEvent;
import com.vaadin.event.ContextClickEvent.ContextClickListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.ClientConnector.AttachEvent;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ColorPicker;
import com.vaadin.ui.ColorPickerArea;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.colorpicker.ColorPickerGradient;
import com.vaadin.ui.components.colorpicker.ColorPickerSelect;

import elemental.json.JsonArray;

@Theme("mytheme")
public class Experiment extends UI {
	// Считывание точек из базы данных
	private ExperimentData myExperiment = new ExperimentData(1);
	private ArrayOfElements myElements = new ArrayOfElements(1);
	
	private Table tableChannel0;
	private Table tableChannel1;
	private Table tableChannel2;
	private Table tableChannel3;
	
	private static double curX;
	private static double curY;

	@Override
	protected void init(VaadinRequest request) {
		// Инициализация DataSeries
		try {
			myExperiment.initDataSeries(1);
			myElements.initElements();
		} catch (Exception e1) {
			System.out.println("Произошла ошибка инициализации графика");
			e1.printStackTrace();
		}
		
		/*
		 * Если для лойаута выполнить метод setSizeFull и добавлять все компоненты по центру, то всё будет по центру
		 * Минус - если экран будет очень маленьким, то всё обрежется
		 * Что бы решить проблему нужно для лойаута выполнить метод setSizeUndefined
		 * Минус - всё будет не поцентру, а слева 
		 */
		
		
        // Основной Layout
		GridLayout gridLayout = new GridLayout(1, 4);
    	gridLayout.setMargin(true);
    	gridLayout.setSpacing(true);
    	//gridLayout.setWidth("100%");
    	
    	// Выбор типа графика Counts/Intensity
        OptionGroup singleGroup = new OptionGroup();
        singleGroup.addStyleName("horizontal");
        singleGroup.addItems("Отсчеты", "Относительные интенсивности");
        singleGroup.select("Отсчеты");

        // Графики
        Chart chart = new Chart();
        chart.setWidth("825px");
        chart.setHeight("332px");
        
        Lang lang = new Lang();
        lang.setResetZoom("Сбросить масштаб");
        lang.setResetZoomTitle("Сбросить масштаб в 1:1");
        ChartOptions.get().setLang(lang);
        
        Configuration configuration = chart.getConfiguration();
        configuration.getChart().setType(ChartType.LINE);
        configuration.getChart().setMarginBottom(50);
        configuration.getTitle().setText(null);
        //configuration.getChart().setZoomType(ZoomType.XY);
        configuration.getChart().setZoomType(ZoomType.X);
        configuration.disableCredits();
        configuration.getTooltip().setFormatter("'<b>' + this.series.name + '</b><br>' + this.x + ' : '+ this.y");

        XAxis xAxis = configuration.getxAxis();
        xAxis.setTitle("Длины волн, нм");
        
        YAxis yAxis = configuration.getyAxis();
        yAxis.setMin(0);
        yAxis.setTitle("Отсчеты, отн. ед.");
        yAxis.getTitle().setVerticalAlign(VerticalAlign.MIDDLE);

        // Настройка надписей к DataSeries-ам
        Legend legend = configuration.getLegend();
        legend.setLayout(LayoutDirection.HORIZONTAL);
        legend.setVerticalAlign(VerticalAlign.TOP);
        legend.setHorizontalAlign(HorizontalAlign.CENTER);
        legend.setBorderWidth(0);
        //legend.setY(10d);
        
        Marker marker = new Marker();
        marker.setSymbol(MarkerSymbolEnum.CIRCLE);
        marker.setRadius(0);
        
        DataSeries ds;
        
        ds = myExperiment.getDataSeries(0);
        ds.setName("Master");
        PlotOptionsLine options0 = new PlotOptionsLine();
        options0.setAllowPointSelect(true);
        options0.setColor(new SolidColor("#800080"));
        options0.setMarker(marker);
        ds.setPlotOptions(options0);
        configuration.addSeries(ds);
        
        
        ds = myExperiment.getDataSeries(1);
        ds.setName("Slave 1");
        PlotOptionsLine options1 = new PlotOptionsLine();
        options1.setAllowPointSelect(true);
        options1.setColor(new SolidColor("#0000FF"));
        options1.setMarker(marker);
        ds.setPlotOptions(options1);
        configuration.addSeries(ds);
        
        ds = myExperiment.getDataSeries(2);
        ds.setName("Slave 2");
        PlotOptionsLine options2 = new PlotOptionsLine();
        options2.setAllowPointSelect(true);
        options2.setColor(new SolidColor("#008000"));
        options2.setMarker(marker);
        ds.setPlotOptions(options2);
        configuration.addSeries(ds);
        
        ds = myExperiment.getDataSeries(3);
        ds.setName("Slave 3");
        PlotOptionsLine options3 = new PlotOptionsLine();
        options3.setAllowPointSelect(true);
        options3.setColor(new SolidColor("#FF0000"));
        options3.setMarker(marker);
        ds.setPlotOptions(options3);
        configuration.addSeries(ds);
        
        Marker markerMax = new Marker();
        markerMax.setSymbol(MarkerSymbolEnum.TRIANGLE);
        markerMax.setRadius(5);
        
        ds = myExperiment.getMaxDataSeries();
        ds.setName("Максимумы");
        PlotOptionsLine optionsM = new PlotOptionsLine();
        optionsM.setAllowPointSelect(true);
        optionsM.setColor(SolidColor.ORANGE);
        optionsM.setMarker(markerMax);
        optionsM.setLineWidth(0);
        ds.setPlotOptions(optionsM);
        configuration.addSeries(ds);
        
        ds = myElements.getDataSeries();
        ds.setName("Спектральные линии");
        PlotOptionsColumn specLineOpt = new PlotOptionsColumn();
        specLineOpt.setShadow(false);
        //specLineOpt.setBorderWidth(0.001);
        specLineOpt.setBorderWidth(0);
        specLineOpt.setGrouping(false);
        specLineOpt.setGroupPadding(0);
        specLineOpt.setPointInterval(0);
        specLineOpt.setPointPadding(0);
        specLineOpt.setColor(SolidColor.BLACK);
        ds.setPlotOptions(specLineOpt);
        configuration.addSeries(ds);
        
        chart.drawChart(configuration);

        singleGroup.addValueChangeListener(new ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
            	try {
            		if (singleGroup.isSelected("Отсчеты"))
            		{	
						myExperiment.initDataSeries(1);
						yAxis.setTitle("Отсчеты, отн. ед.");
            		}	
            		else
            		{	
            			myExperiment.initDataSeries(2);
            			yAxis.setTitle("Интенсивности, отн. ед.");
            		}	
            	} catch (Exception e) {
					System.out.println("Произошла ошибка инициализации графика");
					e.printStackTrace();
				}
            	
            	//mxm.clear();
            	//clearTables();
            	
            	chart.drawChart(configuration); 
        }});

        // Работа с максимумами
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setCaption("Максимумы интенсивности");
        buttonsLayout.setStyleName("csstag");
        buttonsLayout.setSpacing(true);
        
        HorizontalLayout autoSearchLayout = new HorizontalLayout();
        autoSearchLayout.setStyleName("csstag");
        autoSearchLayout.setSpacing(true);
        autoSearchLayout.setSizeFull();
        
        TextField minLevel = new TextField();
        minLevel.setCaption("Минимальный уровень");
        minLevel.setValue("300");
        autoSearchLayout.addComponent(minLevel);
        autoSearchLayout.setComponentAlignment(minLevel, Alignment.BOTTOM_LEFT);
        
        TextField maxLevel = new TextField();
        maxLevel.setCaption("Максимальный уровень");
        maxLevel.setValue("1000000");
        autoSearchLayout.addComponent(maxLevel);
        autoSearchLayout.setComponentAlignment(maxLevel, Alignment.BOTTOM_LEFT);
        
        ComboBox halfWidth = new ComboBox("Полуширина линий");
        halfWidth.setInvalidAllowed(false);
        halfWidth.setNullSelectionAllowed(false);
        halfWidth.addItems("3", "5", "7");    
        halfWidth.setValue("5");
        autoSearchLayout.addComponent(halfWidth);
        autoSearchLayout.setComponentAlignment(halfWidth, Alignment.BOTTOM_LEFT);
        
        Button findMaximums = new Button("Автопоиск");
        autoSearchLayout.addComponent(findMaximums);
        autoSearchLayout.setComponentAlignment(findMaximums, Alignment.BOTTOM_LEFT);
        
        findMaximums.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	try {
            		int type, bottom, top, hw;
            		if (singleGroup.isSelected("Отсчеты"))
            			type = 1;
            		else
            			type = 2;
            		bottom = Integer.parseInt(minLevel.getValue());
            		top = Integer.parseInt(maxLevel.getValue());
            		hw = Integer.parseInt(halfWidth.getValue().toString());
					myExperiment.findMaximums(type, bottom, top, hw);
            		myExperiment.addAllMaximumsToGUI(type, bottom, top, hw);
					chart.drawChart(configuration);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });
        
        buttonsLayout.addComponent(autoSearchLayout);
        buttonsLayout.setComponentAlignment(autoSearchLayout, Alignment.MIDDLE_LEFT);
        
        HorizontalLayout editMaxLayout = new HorizontalLayout();
        editMaxLayout.setStyleName("csstag");
        editMaxLayout.setSpacing(true);
        
        OptionGroup editMaxMode = new OptionGroup("Режим редактирования");
        editMaxMode.addItems("Выкл", "Вкл");
        editMaxMode.select("Выкл");
        editMaxLayout.addComponent(editMaxMode);
        editMaxLayout.setComponentAlignment(editMaxMode, Alignment.BOTTOM_LEFT);
        
        Button addMax = new Button("Добавить");
        addMax.setEnabled(false);
        addMax.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	try {
                    int type, bottom, top, hw;
            		if (singleGroup.isSelected("Отсчеты"))
            			type = 1;
            		else
            			type = 2;
            		bottom = Integer.parseInt(minLevel.getValue());
            		top = Integer.parseInt(maxLevel.getValue());
            		hw = Integer.parseInt(halfWidth.getValue().toString());
            		myExperiment.addMaxPoint(curX, curY, type);
					myExperiment.addAllMaximumsToGUI(type, bottom, top, hw);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });
        editMaxLayout.addComponent(addMax);
        editMaxLayout.setComponentAlignment(addMax, Alignment.BOTTOM_LEFT);  
        
        Button remMax = new Button("Удалить");
        remMax.setEnabled(false);
        remMax.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	try {
                    int type, bottom, top, hw;
            		if (singleGroup.isSelected("Отсчеты"))
            			type = 1;
            		else
            			type = 2;
            		bottom = Integer.parseInt(minLevel.getValue());
            		top = Integer.parseInt(maxLevel.getValue());
            		hw = Integer.parseInt(halfWidth.getValue().toString());
            		myExperiment.removeMaxPoint(curX, curY, type);
					myExperiment.addAllMaximumsToGUI(type, bottom, top, hw);
					chart.drawChart(configuration);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });
        editMaxLayout.addComponent(remMax);
        editMaxLayout.setComponentAlignment(remMax, Alignment.BOTTOM_LEFT);
        
        buttonsLayout.addComponent(editMaxLayout);
        buttonsLayout.setComponentAlignment(editMaxLayout, Alignment.MIDDLE_LEFT);
        
        editMaxMode.addValueChangeListener(new ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
            	if (editMaxMode.isSelected("Вкл"))
        		{	
        			addMax.setEnabled(true);
        			remMax.setEnabled(true);
        			marker.setRadius(4);
        			chart.drawChart(configuration);
        		}	
            	else
        		{	
        			addMax.setEnabled(false);
        			remMax.setEnabled(false);
        			marker.setRadius(0);
        			chart.drawChart(configuration);
        		}
        }});
        
        HorizontalLayout maxTables = new HorizontalLayout();
        maxTables.setStyleName("csstag");
        maxTables.setSpacing(true);
        maxTables.setWidth("1130px");
        //maxTables.setWidth("1160");
        // Определение таблиц с максимумами
        // 0
        tableChannel0 = new Table("Master");
        tableChannel0.setStyleName("tableborder");
        //tableChannel0.setSelectable(true);

        tableChannel0.addContainerProperty("λ,нм", Double.class, null);
        tableChannel0.addContainerProperty("Элемент; λ,нм",  Label.class, null);
        tableChannel0.addContainerProperty("I,отн.ед.",  Double.class, null);
        tableChannel0.setColumnWidth("Элемент; λ,нм", 150);
        
        tableChannel0.setHeight("170px");
        tableChannel0.setWidth("100%");
        myExperiment.setChannelTable(tableChannel0, 0);
        
        maxTables.addComponent(tableChannel0);
        maxTables.setComponentAlignment(tableChannel0, Alignment.MIDDLE_LEFT);
        // 1
        tableChannel1 = new Table("Slave 1");
        tableChannel1.setStyleName("tableborder");
        //tableChannel1.setSelectable(true);

        tableChannel1.addContainerProperty("λ,нм", Double.class, null);
        tableChannel1.addContainerProperty("Элемент; λ,нм",  Label.class, null);
        tableChannel1.addContainerProperty("I,отн.ед.",  Double.class, null);
        tableChannel1.setColumnWidth("Элемент; λ,нм", 150);
        
        tableChannel1.setHeight("170px");
        tableChannel1.setWidth("100%");
        myExperiment.setChannelTable(tableChannel1, 1);
        
        maxTables.addComponent(tableChannel1);
        maxTables.setComponentAlignment(tableChannel1, Alignment.MIDDLE_LEFT);
        // 2
        tableChannel2 = new Table("Slave 2");
        tableChannel2.setStyleName("tableborder");
        //tableChannel2.setSelectable(true);

        tableChannel2.addContainerProperty("λ,нм", Double.class, null);
        tableChannel2.addContainerProperty("Элемент; λ,нм",  Label.class, null);
        tableChannel2.addContainerProperty("I,отн.ед.",  Double.class, null);
        tableChannel2.setColumnWidth("Элемент; λ,нм", 150);
        
        tableChannel2.setHeight("170px");
        tableChannel2.setWidth("100%");
        myExperiment.setChannelTable(tableChannel2, 2);
        
        maxTables.addComponent(tableChannel2);
        maxTables.setComponentAlignment(tableChannel2, Alignment.MIDDLE_LEFT);
        // 3
        tableChannel3 = new Table("Slave 3");
        tableChannel3.setStyleName("tableborder");
        //tableChannel3.setSelectable(true);

        tableChannel3.addContainerProperty("λ,нм", Double.class, null);
        tableChannel3.addContainerProperty("Элемент; λ,нм",  Label.class, null);
        tableChannel3.addContainerProperty("I,отн.ед.",  Double.class, null);
        tableChannel3.setColumnWidth("Элемент; λ,нм", 150);
        
        tableChannel3.setHeight("170px");
        tableChannel3.setWidth("100%");
        myExperiment.setChannelTable(tableChannel3, 3);
        
        maxTables.addComponent(tableChannel3);
        maxTables.setComponentAlignment(tableChannel3, Alignment.MIDDLE_LEFT);
        
        // Таблица для поиска элементов
        VerticalLayout searchedElements = new VerticalLayout();
        searchedElements.setStyleName("csstag");
        searchedElements.setSpacing(true);
        searchedElements.setSizeUndefined();
        
        Table searchedElementsTab = new Table("Искомые элементы");
        searchedElementsTab.addContainerProperty("", CheckBox.class, null);
        searchedElementsTab.addContainerProperty("Элемент",  String.class, null);
        searchedElementsTab.addContainerProperty("Цвет",  ColorPicker.class, null);
        searchedElementsTab.addContainerProperty("k",  String.class, null);
        searchedElementsTab.setPageLength(5);
        searchedElementsTab.setWidth("100%");
        searchedElementsTab.setColumnWidth("", 42);
        searchedElementsTab.setColumnWidth("Элемент", 60);
        
        Label redLabel = new Label("asdsadsadsa");
        ComboBox comboBox = new ComboBox();
    	comboBox.setInvalidAllowed(false);
    	comboBox.setNullSelectionAllowed(false);
    	comboBox.addItems(redLabel);    
    	comboBox.setValue(redLabel);
        
        try {
			ElementSpectrLines elem;
	        for (int i = 0; i < myElements.getArray().size(); i++)
	        {
	        	elem = myElements.getArray().get(i);
	        	ColorPicker colorPicker = new ColorPicker();
	        	colorPicker = new ColorPicker("Выберите цвет", Color.BLACK);
	        	colorPicker.setSwatchesVisibility(false);
	        	colorPicker.setHistoryVisibility(false);
	        	colorPicker.setHSVVisibility(false);
	        	
	        	searchedElementsTab.addItem(new Object[]{elem.getCheckBox(), elem.getName(), colorPicker, null}, i + 1);
	        }
		} catch (Exception e1) {
			e1.printStackTrace();
		}   
        
        searchedElements.addComponent(searchedElementsTab);
        searchedElements.setComponentAlignment(searchedElementsTab, Alignment.MIDDLE_CENTER);
        
        HorizontalLayout showCompareButtons = new HorizontalLayout();
        showCompareButtons.setSpacing(true);
        
        Button showElements = new Button("Показать");
        showCompareButtons.addComponent(showElements);
        showCompareButtons.setComponentAlignment(showElements, Alignment.MIDDLE_CENTER);
        
        showElements.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	myElements.initDataSeries();
            	chart.drawChart(configuration);
            }
        });
        
        Button compareElements = new Button("Сравнить");
        compareElements.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	try {
            		int type, bottom, top, hw;
            		if (singleGroup.isSelected("Отсчеты"))
            			type = 1;
            		else
            			type = 2;
            		bottom = Integer.parseInt(minLevel.getValue());
            		top = Integer.parseInt(maxLevel.getValue());
            		hw = Integer.parseInt(halfWidth.getValue().toString());
            		myExperiment.compareMaximums(myElements, 0.1, 1);
					myExperiment.addAllMaximumsToGUI(type, bottom, top, hw);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });
        
        showCompareButtons.addComponent(compareElements);
        showCompareButtons.setComponentAlignment(compareElements, Alignment.MIDDLE_CENTER);
        
        searchedElements.addComponent(showCompareButtons);
        searchedElements.setComponentAlignment(showCompareButtons, Alignment.MIDDLE_CENTER);
        
        HorizontalLayout chartAndSearch = new HorizontalLayout(); 
        chartAndSearch.setStyleName("csstag");
        chartAndSearch.setSpacing(true);
        
        chartAndSearch.addComponent(chart);
        chartAndSearch.setComponentAlignment(chart, Alignment.MIDDLE_LEFT);
        
        chartAndSearch.addComponent(searchedElements);
        chartAndSearch.setComponentAlignment(searchedElements, Alignment.BOTTOM_LEFT);
        
        chart.addPointClickListener(new PointClickListener() {
            @Override
            public void onClick(PointClickEvent event) {
            	curX = event.getX();
                curY = event.getY(); 
            }
        });
/*
        JavaScript.getCurrent().addFunction("com.example.foo.myfunc",
                new JavaScriptFunction() {
        			@Override
        			public void call(JsonArray arguments) {
        				try {
        					String message = arguments.getString(0);
        					//int    value   = arguments.getInt(1);
        					Notification.show("Message: " + message);
        				} catch (Exception e) {
        					Notification.show("Error: " + e.getMessage());
        				}
        			}
        });

        Link link = new Link("Send Message", new ExternalResource("javascript:com.example.foo.myfunc('asdasdasdas', 123)"));
        
        gridLayout.addComponent(link, 0, 0);
        gridLayout.setComponentAlignment(link, Alignment.MIDDLE_CENTER);
        */        
        
        JavaScript.getCurrent().execute("setInterval(function() { var x = document.getElementsByClassName('v-caption-red'); for (i = 0; i < x.length; i++) { x[i].innerHTML='<span class=\"v-captiontext my-color-label\">Крас.</span>'; }}, 500);");
        JavaScript.getCurrent().execute("setInterval(function() { var x = document.getElementsByClassName('v-caption-green'); for (i = 0; i < x.length; i++) { x[i].innerHTML='<span class=\"v-captiontext my-color-label\">Зел.</span>'; }}, 500);");
        JavaScript.getCurrent().execute("setInterval(function() { var x = document.getElementsByClassName('v-caption-blue'); for (i = 0; i < x.length; i++) { x[i].innerHTML='<span class=\"v-captiontext my-color-label\">Син.</span>'; }}, 500);");
        JavaScript.getCurrent().execute("setInterval(function() { var x = document.getElementsByClassName('v-button-caption'); for (i = 0; i < x.length; i++) { if (x[i].textContent == 'OK') { x[i].textContent = 'ОК'; }}}, 500);");
        JavaScript.getCurrent().execute("setInterval(function() { var x = document.getElementsByClassName('v-button-caption'); for (i = 0; i < x.length; i++) { if (x[i].textContent == 'Cancel') { x[i].textContent = 'Выход'; }}}, 500);");
        
        
        gridLayout.addComponent(singleGroup, 0, 0);
        gridLayout.setComponentAlignment(singleGroup, Alignment.MIDDLE_CENTER);
        
        gridLayout.addComponent(chartAndSearch, 0, 1);
        gridLayout.setComponentAlignment(chartAndSearch, Alignment.MIDDLE_CENTER); 
        
        gridLayout.addComponent(buttonsLayout, 0, 2);
        gridLayout.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);

        gridLayout.addComponent(maxTables, 0, 3);
        gridLayout.setComponentAlignment(maxTables, Alignment.MIDDLE_CENTER);
        
        setContent(gridLayout);
		
	}
	
}
