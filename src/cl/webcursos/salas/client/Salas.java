package cl.webcursos.salas.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.webcursos.salas.client.AjaxRequest;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;


public class Salas implements EntryPoint{
	
	/** Ajax url **/
	private String moodleurl = null;
	private String buttonsRoomsDivId = null;
	
	/** User information **/
	private int isAdmin = -1;
	private String userName = null;
	private String userLastName = null;
	private String userEmail = null;
	
	/** Auxiliary variable for controlling loading modules  **/
	private boolean initiate = false;
	
	/**  **/
	private VerticalPanel matrixButtons = null;
	private HorizontalPanel firstRowButtons = null;
	private HorizontalPanel rowButtons = null;
	private FormPanel form = null;
	private VerticalPanel formPanel = null;
	private TextBox eventName = null;
	private String eventNameTxt = null;
	private TextBox attendeesAmount = null;
	private String attendeesAmountTxt = null;
	private TextBox emailForm = null;
	private String emailFormTxt = null;
	private VerticalPanel resultPanelBookings = null;
	private DecoratorPanel decoratordecoratorPanelFormPanel = null;
	
	/** Variables extracted from the div **/
	private int initialDate = -1;
	// typeRoom = {1: class room, 2: study room, 3: reunion room}
	private int typeRoom = -1;
	private int idCampus = -1;
	private int userDayReservations = -1;
	private int userWeeklyBooking = -1;
	private int maxDailyBookings = -1;
	private int maxWeeklyBookings = -1;
	private String size = null;
	private int endDate = -1;
	private String selectDays = null;
	private int weeklyFrequencyBookings = -1;
	private int advOptions = -1;
	private int counter = -1;

	public static native void console(String text)
	/*-{
    console.log(text);
	}-*/;
	public static <T extends JavaScriptObject> T parseJson(String jsonStr)
	{
		return JsonUtils.safeEval(jsonStr);
	}

	public void onModuleLoad() {

		// List of errors after trying to initialize
		ArrayList<String> errors = new ArrayList<String>();
		
		buttonsRoomsDivId = "buttonsRooms";
		if(RootPanel.get(buttonsRoomsDivId) == null) {
			errors.add("Can not initalize. Buttons Rooms requires an existing DIV tag with id: buttonsRooms.");
			Window.alert("GWT impossible initialize");
			return;
		}
		
		if(RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("moodleurl") != null && !RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("moodleurl").equals("")){
			moodleurl = RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("moodleurl");
		}else{
			errors.add("Invalid Moodle ajax url");
		}

		try {
			// Extract information from the div
			
			if(RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("initialDate") != null && !RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("initialDate").equals("")) {
				initialDate = Integer.parseInt(RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("initialDate")); 
			}

			if(RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("typeRoom") != null && !RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("typeRoom").equals("")) {
				typeRoom = Integer.parseInt(RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("typeRoom"));
			}
			
			if(RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("campus") != null && !RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("campus").equals("")) {
				idCampus = Integer.parseInt(RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("campus"));
			}
			
			if(RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("userDayReservations") != null && !RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("userDayReservations").equals("")) {
				userDayReservations = Integer.parseInt(RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("userDayReservations"));
			}
			
			if(RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("userWeeklyBooking") != null && !RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("userWeeklyBooking").equals("")) {
				userWeeklyBooking = Integer.parseInt(RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("userWeeklyBooking"));
			}
			
			if(RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("maxDailyBookings") != null && !RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("maxDailyBookings").equals("")) {
				maxDailyBookings = Integer.parseInt(RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("maxDailyBookings"));
			}
			
			if(RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("maxWeeklyBookings") != null && !RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("maxWeeklyBookings").equals("")) {
				maxWeeklyBookings = Integer.parseInt(RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("maxWeeklyBookings"));
			}
			
			if(RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("size") != null && !RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("size").equals("")) {
				size = RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("size");
			}
			
			if(RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("endDate") != null && !RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("endDate").equals("")) {
				endDate = Integer.parseInt(RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("endDate"));
			}
			
			if(RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("selectDays") != null){
				selectDays = RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("selectDays");
			}
			
			if(RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("weeklyFrequencyBookings") != null && !RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("weeklyFrequencyBookings").equals("")) {
				weeklyFrequencyBookings = Integer.parseInt(RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("weeklyFrequencyBookings"));
			}
			
			if(RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("advOptions") != null && !RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("advOptions").equals("")) {
				advOptions = Integer.parseInt(RootPanel.get(buttonsRoomsDivId).getElement().getAttribute("advOptions"));
			}			
			
		} catch (Exception e) {
			errors.add("Form error process, check that the input is correctly entered");
		}

		if(errors.size() > 0) {
			Label errorsLabel = new Label();
			String text = "";
			for(int i=0; i<errors.size(); i++) {
				text += "\n" + errors.get(i);
			}
			errorsLabel.setText(text);
			errorsLabel.setTitle("Fatal error while initializing modules");

			RootPanel.get(buttonsRoomsDivId).clear();
			RootPanel.get(buttonsRoomsDivId).add(errorsLabel);
		}else{
			AjaxRequest.moodleUrl = moodleurl;
			AjaxRequest.ajaxRequest("action=info", new AsyncCallback<AjaxData>() {
				@Override
				public void onFailure(Throwable caught) {
					RootPanel.get(buttonsRoomsDivId).clear();
					RootPanel.get(buttonsRoomsDivId).add(new HTML("Ajax query error: user data"));					
				}
				
				@Override
				public void onSuccess(AjaxData result) {
					
					Map<String, String> values = AjaxRequest.getValueFromResult(result);
					
					if(!result.getError().equals("")) {
						Window.alert("Ajax query error: user data");
						return;
					}
					// Check if the user is an administrator or not, 0 = false, 1 = true
					isAdmin = Integer.parseInt(values.get("isAdmin"));
					userName = values.get("firstname");
					userLastName = values.get("lastname");
					userEmail = values.get("email");	
				}			
			});
			
			loadInterface();				
		}
	}	
	
	private void loadInterface(){
		
		matrixButtons = new VerticalPanel(); 
		matrixButtons.setSpacing(5); 
		
		final int capSobre= 0;//traigo si puede sobre escribir

		final int availabilityBookingToday = maxDailyBookings - userDayReservations; 
		final int availabilityBookingWeek = maxWeeklyBookings - userWeeklyBooking; 
		String param= "&campusid="+idCampus+"&type="+typeRoom+"&date="+initialDate+"&multiply="+advOptions+"&size="+size+"&finalDate="+endDate+"&days="+selectDays+"&frequency="+weeklyFrequencyBookings;
		AjaxRequest.ajaxRequest("action=getbooking"+ param, new AsyncCallback<AjaxData>() {
			@Override
			public void onSuccess(AjaxData result) {
				
				Map<String, String> values = AjaxRequest.getValueFromResult(result);
				List<Map<String, String>> valuesModules = AjaxRequest.getValuesFromResultString(values.get("Modulos"));
				List<Map<String, String>> valuesRooms = AjaxRequest.getValuesFromResultString(values.get("Salas"));
				firstRowButtons = new HorizontalPanel(); 
				firstRowButtons.setSpacing(4);
				// White cell
				HTML dieTip = new HTML("<div style='hight: 10px; width: 53px;'></div>",true);
				firstRowButtons.add(dieTip);						
				// Modules
				for(Map<String, String> modules : valuesModules) {							
					ToggleButton moduleButton = new ToggleButton(modules.get("name"));
					moduleButton.setEnabled(false);
					moduleButton.setStylePrimaryName("Boton-marco");
					firstRowButtons.add(moduleButton);
					firstRowButtons.setCellVerticalAlignment(moduleButton, HasVerticalAlignment.ALIGN_MIDDLE);
					firstRowButtons.setCellHorizontalAlignment(moduleButton, HasHorizontalAlignment.ALIGN_CENTER);
				}
				
				matrixButtons.add(firstRowButtons);
				
				for(Map<String, String> rooms : valuesRooms) {							
					final String nameRoom = rooms.get("nombresala"); 
					final int idRoom = Integer.parseInt(rooms.get("salaid"));
					
					rowButtons = new HorizontalPanel();
					rowButtons.setSpacing(4);
					ToggleButton numberRoom = new ToggleButton(nameRoom); 
					numberRoom.setEnabled(false);
					numberRoom.setStylePrimaryName("Boton-marco"); 
					rowButtons.add(numberRoom);
					rowButtons.setCellVerticalAlignment(numberRoom, HasVerticalAlignment.ALIGN_MIDDLE);
					rowButtons.setCellHorizontalAlignment(numberRoom, HasHorizontalAlignment.ALIGN_CENTER);

					List<Map<String, String>> valuesBookingAvailable = AjaxRequest.getValuesFromResultString(rooms.get("disponibilidad"));
					for(Map<String, String> bookAvaible : valuesBookingAvailable) {	
						
						final SalasButton book = new SalasButton(  
								nameRoom,
								idRoom,
								bookAvaible.get("horaInicio"),
								bookAvaible.get("horaFin"),								
								bookAvaible.get("modulonombre"),
								Integer.parseInt(bookAvaible.get("moduloid")), // Módulo
								Integer.parseInt(bookAvaible.get("ocupada")) == 1,//si esta reservada
								capSobre == 1,// Si se puede sobreescribir
								"",
								"",
								new ClickHandler() {
									public void onClick(ClickEvent event) {

										SalasButton buttonBook = (SalasButton) event.getSource(); 
										if(buttonBook.isDown()){
											if(isAdmin == 1){
												// I am an administrator, no booking restrictions
												
											}else{
												// I am not administered. Availability Booking
												if(availabilityBookingWeek > 0){
													if(availabilityBookingToday > 0){
														for(SalasButton bt : getVerticalButtons(buttonBook)) {
															bt.setDown(false);
														}
												
														if(howManyButtonsDown(buttonBook, false) > availabilityBookingToday) {
															buttonBook.setDown(false);
															Window.alert("No puedes seleccionar más de "+availabilityBookingToday+" módulo(s) el día de hoy.");
															return;
														}																
													}else{
														buttonBook.setDown(false);
														Window.alert("No puedes reservar más módulos el día de hoy.");
													}
												}else{
													buttonBook.setDown(false);
													Window.alert("No puedes reservar más módulos esta semana.");														
												}
											}
										}
									}

								});
						rowButtons.add(book);
					}
					matrixButtons.add(rowButtons);
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("Callback error");
			}
		});
		
		form = new FormPanel();
		form.setAction("");
		form.setEncoding(FormPanel.ENCODING_URLENCODED);
		form.setMethod(FormPanel.METHOD_POST);
		formPanel = new VerticalPanel(); 
		formPanel.setSpacing(4);
		form.setWidget(formPanel);

		eventName = new TextBox();
		eventNameTxt = "Nombre del evento";
		eventName.setValue(eventNameTxt);
		eventName.setStylePrimaryName("Text-Box");
		eventName.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventName.setValue(null); 
				if(attendeesAmount.getValue().length()== 0){
					attendeesAmount.setValue("Asistentes");
				}
				if(emailForm.getValue().length()== 0){
					emailForm.setValue("Correo electrónico");
				}
			}
		});
		
		attendeesAmount = new TextBox(); 
		attendeesAmountTxt = "Asistentes";
		attendeesAmount.setValue(attendeesAmountTxt);
		attendeesAmount.setStylePrimaryName("Text-Box");
		attendeesAmount.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				attendeesAmount.setValue(null);   
				if(eventName.getValue().length()== 0 ){
					eventName.setValue("Nombre del evento");
				}
				if(emailForm.getValue().length()== 0){
					emailForm.setValue("Correo electrónico");
				}
			}
		});
		
		emailForm = new TextBox();
		emailFormTxt = "Correo electrónico";
		emailForm.setValue(emailFormTxt);
		emailForm.setStylePrimaryName("Text-Box");
		emailForm.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				emailForm.setValue(null);   
				if(attendeesAmount.getValue().length()== 0){
					attendeesAmount.setValue("Asistentes");
				}
				if(eventName.getValue().length()== 0){
					eventName.setValue("Nombre del evento");
				}
			}
		});

		formPanel.add(eventName);
		formPanel.add(attendeesAmount);
		formPanel.add(emailForm);
		formPanel.add(new Button("Reservar", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				event.preventDefault();
				form.submit();					
			}
		}));
		
		decoratordecoratorPanelFormPanel = new DecoratorPanel();
		decoratordecoratorPanelFormPanel.add(form);

		RootPanel.get(buttonsRoomsDivId).add(matrixButtons);
		RootPanel.get(buttonsRoomsDivId).add(decoratordecoratorPanelFormPanel);

		// Add an event handler to the form.
		form.addSubmitHandler(new SubmitHandler() {
			@Override
			public void onSubmit(SubmitEvent event) {
				// This event is fired just before the form is submitted. 
				// We can take this opportunity to perform validation.

				if(eventName.getText().length() == 0 || eventName.getText().equals(eventNameTxt)) {
					Window.alert("Debe escribir un  nombre de evento.");
					eventName.setStylePrimaryName("Text-Box-vacio");
					//eventName.setValue("Nombre del evento");
					event.cancel();
				}else{
					eventName.setStylePrimaryName("Text-Box");
				}
				
				if(attendeesAmount.getText().length() == 0 || attendeesAmount.getText().equals(attendeesAmountTxt)) {
					Window.alert("Debe escribir la cantidad de asistentes.");
					attendeesAmount.setStylePrimaryName("Text-Box-vacio");
					//attendeesAmount.setValue("Asistentes");
					event.cancel();
				}else{
					attendeesAmount.setStylePrimaryName("Text-Box");
				}

				if(emailForm.getText().length() == 0 || emailForm.getText().equals(emailFormTxt)) {
					Window.alert("Debe escribir el correo de usuario.");
					emailForm.setStylePrimaryName("Text-Box-vacio");
					//emailForm.setValue("Correo electrónico");
					event.cancel();
				}else{
					emailForm.setStylePrimaryName("Text-Box");

				}	
			}	
			
		});
		
		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				Window.alert(event.getResults());
				Window.alert("cargo submit");
						int rows = getAllButtonsDown(matrixButtons).size() + 1;								
						resultPanelBookings = new VerticalPanel(); 
						resultPanelBookings.setSpacing(4); 

						final Grid dataTable = new Grid(rows, 5); 
						// Header data table
						dataTable.setText(0,0,"Nombre sala");
						dataTable.setText(0,1,"Módulo");
						dataTable.setText(0,2,"Hora inicio");
						dataTable.setText(0,3,"Hora término");
						dataTable.setText(0,4,"Estado");
						dataTable.setBorderWidth(1);
						
						List<Map<String, String>> values = getAllButtonsDown(matrixButtons);							
						String sala = "inicio";
						String modulo ="inicio";
						String nombreSalas="inicio";
						String nombreModulo="inicio";
						String  inicio="inicio";
						String  termino="inicio";
						for(Map<String, String> Disp : values) {
							sala= sala+","+Disp.get("idSala");
							modulo= modulo+","+Disp.get("idModulo");
							nombreSalas=nombreSalas+","+Disp.get("nombreSala");
							nombreModulo=nombreModulo+","+Disp.get("nombreModulo");
							inicio=inicio+","+Disp.get("inicio");
							termino=termino+","+Disp.get("termino");
						}
							counter = 1;		
							String param = "&inicio="+inicio+"&termino="+termino+"&nombremodulo="+nombreModulo+"&nombresala="+nombreSalas+"&moduleid="+modulo+"&room="+sala+"&date="+initialDate+"&name="+eventName.getValue()+"&asistentes="+attendeesAmount.getValue()+"&multiply="+advOptions+"&finalDate="+endDate+"&days="+selectDays+"&frequency="+weeklyFrequencyBookings;								
							AjaxRequest.ajaxRequest("action=submission"+param, new AsyncCallback<AjaxData>() {
								@Override
								public void onSuccess(AjaxData result) {
									Map<String, String> values = AjaxRequest.getValueFromResult(result);
									
									
									List<Map<String, String>> valuesvaluesSatisfactoryBookingsReservas = AjaxRequest.getValuesFromResultString(values.get("well"));
									List<Map<String, String>> valuesErroneousBookings = AjaxRequest.getValuesFromResultString(values.get("errors"));
									
									for(Map<String, String> bookings : valuesvaluesSatisfactoryBookingsReservas) {	
										dataTable.setText(counter,0,bookings.get("nombresala"));
										dataTable.setText(counter,1,bookings.get("nombremodulo"));
										dataTable.setText(counter,2,bookings.get("inicio"));
										dataTable.setText(counter,3,bookings.get("termino"));
										dataTable.setText(counter,4,"OK");
										counter++;
									}
									
									for(Map<String, String> bookings : valuesErroneousBookings) {	
										dataTable.setText(counter,0,bookings.get("nombresala"));
										dataTable.setText(counter,1,bookings.get("nombremodulo"));
										dataTable.setText(counter,2,bookings.get("inicio"));
										dataTable.setText(counter,3,bookings.get("termino"));
										dataTable.setText(counter,4,"Error");
										counter++;																								
									}										
								}
								
								public void onFailure(Throwable caught) {
									System.out.println("Callback error");
								}
							});
							
						Date dateInfo = new Date(initialDate * 1000L); // *1000 is to convert seconds to milliseconds
						Date dateEndInfo = new Date(endDate * 1000L); // *1000 is to convert seconds to milliseconds
						DateTimeFormat fmt = DateTimeFormat.getFormat("d/M/yyyy");

						Grid info = new Grid(7, 2);
						info.setBorderWidth(0);
						info.setText(0,0,"Nombre del evento:");
						info.setText(0,1,eventName.getValue());
						info.setText(1,0,"Fecha inicio:");
						info.setText(1,1,fmt.format(dateInfo));
						info.setText(2,0,"Fecha término:");
						info.setText(2,1,fmt.format(dateEndInfo));
						info.setText(3,0,"Frecuencia:");
						info.setText(3,1,"Cada "+weeklyFrequencyBookings+" semana(s)");
						info.setText(4,0,"N° de asistentes:");
						info.setText(4,1,attendeesAmount.getValue());
						info.setText(5,0,"Responsable");
						info.setText(5,1,userName+" "+userLastName);
						info.setText(6,0,"Correo responsable:");
						info.setText(6,1,userEmail);

						resultPanelBookings.add(info);
						resultPanelBookings.add(dataTable);
						matrixButtons.setVisible(false);
						decoratordecoratorPanelFormPanel.setVisible(false);
						//RootPanel.get(buttonsRoomsDivId).clear();
						RootPanel.get(buttonsRoomsDivId).add(resultPanelBookings);
					
			}		
		});
	
	}
	

	private List<SalasButton> getButtons(SalasButton btn){
		List<SalasButton> buttons = new ArrayList<SalasButton>();
		VerticalPanel vpanel = (VerticalPanel) btn.getParent().getParent();
		for(int i=0; i < vpanel.getWidgetCount(); i++) {
			HorizontalPanel hpanel = (HorizontalPanel) vpanel.getWidget(i);
			for(int j=0; j < hpanel.getWidgetCount(); j++) {
				if(!(hpanel.getWidget(j) instanceof SalasButton))
					continue;
				SalasButton bt = (SalasButton) hpanel.getWidget(j);
				if(bt != btn)
					buttons.add(bt);
			}
		}
		return buttons;
	}

	private List<SalasButton> getVerticalButtons(SalasButton btn){ 
		List<SalasButton> buttons = new ArrayList<SalasButton>(); 
		VerticalPanel vpanel = (VerticalPanel) btn.getParent().getParent();
		for(int i=0; i < vpanel.getWidgetCount(); i++) {
			HorizontalPanel hpanel = (HorizontalPanel) vpanel.getWidget(i);
			for(int j=0; j < hpanel.getWidgetCount(); j++) {
				if(!(hpanel.getWidget(j) instanceof SalasButton))
					continue;
				SalasButton bt = (SalasButton) hpanel.getWidget(j);

				if(bt != btn && bt.getModuloid() == btn.getModuloid())
					buttons.add(bt);
			}
		}
		return buttons;
	}
	
	private List<Map<String, String>> getAllButtonsDown(VerticalPanel vPanel){ 	
		List<Map<String, String>> buttonsDown = new ArrayList<Map<String,String>>();
		for(int i=0; i < vPanel.getWidgetCount(); i++) {
			HorizontalPanel hpanel = (HorizontalPanel) vPanel.getWidget(i);
			for(int j=0; j < hpanel.getWidgetCount(); j++) {
				if(!(hpanel.getWidget(j) instanceof SalasButton))
					continue;
				SalasButton bt = (SalasButton) hpanel.getWidget(j);
				if(bt.isDown()){
					Map<String, String> obj = new HashMap<String, String>();
					obj.put("nombreSala",bt.getSala());
					obj.put("idSala",String.valueOf(bt.getSalaid()));
					obj.put("idModulo",String.valueOf(bt.getModuloid()));
					obj.put("nombreModulo",bt.getModulo());
					obj.put("inicio",bt.getModuloInicio());	
					obj.put("termino",bt.getModuloTermino());
					buttonsDown.add(obj);
				}
			}
		}
		return buttonsDown;
	}
	
	private int howManyButtonsDown(SalasButton btn, boolean book){ 
		int total = 0;
		if(book == false){
			if(btn.isDown())
				total++;
			for(SalasButton bt : getButtons(btn)) {
				if(bt.isDown())
					total++;
			}
		}
		return total;
	}
}
