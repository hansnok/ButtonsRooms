package cl.webcursos.salas.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;



public class Salas implements EntryPoint {
	public int contador = 1;


	public String mensaje = "";

	public static native void console(String text)
	/*-{
    console.log(text);
}-*/;
	public static <T extends JavaScriptObject> T parseJson(String jsonStr)
	{
		return JsonUtils.safeEval(jsonStr);
	}


	public void onModuleLoad() {
		

		//final String url=RootPanel.get("salas").getElement().getAttribute("moodleurl");
		//final int type=Integer.parseInt(RootPanel.get("salas").getElement().getAttribute("type"));
		//final int date=Integer.parseInt(RootPanel.get("salas").getElement().getAttribute("fecha"));
		//final int campusid=Integer.parseInt(RootPanel.get("salas").getElement().getAttribute("campus"));
		//final String size=RootPanel.get("salas").getElement().getAttribute("size");
		//final int multiply=Integer.parseInt(RootPanel.get("salas").getElement().getAttribute("multiply"));
		//final int finalDate=Integer.parseInt(RootPanel.get("salas").getElement().getAttribute("finalDate"));
		//final String days=RootPanel.get("salas").getElement().getAttribute("days");
		//final int frequency=Integer.parseInt(RootPanel.get("salas").getElement().getAttribute("frequency"));
		//final int maxreservasenpantalla=Integer.parseInt(RootPanel.get("salas").getElement().getAttribute("reservasdia"));
		//final int maxreservasSemana=Integer.parseInt(RootPanel.get("salas").getElement().getAttribute("reservassemana"));
		//final int userreservasenpantalla = Integer.parseInt(RootPanel.get("salas").getElement().getAttribute("userdailybooking"));
		//final int userreservasSemana=Integer.parseInt(RootPanel.get("salas").getElement().getAttribute("userweeklybooking"));
		
		//incluidos
		final String url="http://localhost/moodlegit/local/reservasalas/ajax/data.php";
		final int type=1;
		final int date=1419303600;
		final int campusid=1;
		final String size = "1-25";
		final int multiply = 0;
		final int finalDate=1419908400;
		final String days="LMWJVS";
		final int frequency = 1;
		final int maxreservasenpantalla = 2;//traigo cantidad de reservas en el dia
		final int maxreservasSemana=6;
		final int userreservasenpantalla = 0;//traigo cantidad de reservas en el dia
		final int userreservasSemana=0;
		final boolean admin =false;
		
		//no incluidos
		
		final boolean rev = false;
		final boolean resources = false;
		final int capSobre= 0;//traigo si puede sobre escribir

		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
		//sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")); // give a timezone reference for formating (see comment at the bottom
		//String formattedDate = sdf.format(date);
		
		
		
		final int totalreservashoy=maxreservasenpantalla-userreservasenpantalla;
		final int totalreservassemana=maxreservasSemana-userreservasSemana;
		final VerticalPanel vPanel = new VerticalPanel();
		vPanel.setSpacing(5); 
		AjaxRequest.moodleUrl = url;
		String param= "&campusid="+campusid+"&type="+type+"&date="+date+"&rev="+rev+"&resources="+resources+"&multiply="+multiply+"&size="+size+"&finalDate="+finalDate+"&days="+days+"&frequency="+frequency;
		AjaxRequest.ajaxRequest("action=getbooking"+ param, new AsyncCallback<AjaxData>() {
			int anterior=0;
			String anteriorS;
			String primerId;
			@Override
			public void onSuccess(AjaxData result) {

				Map<String, String> values = AjaxRequest.getValueFromResult(result);
				List<Map<String, String>> valuesModulos = AjaxRequest.getValuesFromResultString(values.get("Modulos"));
				List<Map<String, String>> valuesSalas = AjaxRequest.getValuesFromResultString(values.get("Salas"));
				HorizontalPanel hModulosPanel = new HorizontalPanel();
				hModulosPanel.setSpacing(4);

				HTML html = new HTML("<div style='hight: 10px; width: 53px;'></div>",true);
				hModulosPanel.add(html);

				for(Map<String, String> modules : valuesModulos) {							
					ToggleButton toggleButton = new ToggleButton(modules.get("name"));
					toggleButton.setEnabled(false);
					toggleButton.setStylePrimaryName("Boton-marco");
					hModulosPanel.add(toggleButton);

				}
				vPanel.add(hModulosPanel);
				for(Map<String, String> Salas : valuesSalas) {							
					final String nombresala=Salas.get("nombresala");
					final int idSala=Integer.parseInt(Salas.get("salaid"));
					final String horaInicio = Salas.get("horaInicio");
					final String horaFin = Salas.get("horaFin");

					HorizontalPanel hPanel = new HorizontalPanel();
					hPanel.setSpacing(4);
					ToggleButton botoncito = new ToggleButton(nombresala);
					botoncito.setEnabled(false);
					botoncito.setStylePrimaryName("Boton-marco");
					hPanel.add(botoncito);

					List<Map<String, String>> valuesDisp = AjaxRequest.getValuesFromResultString(Salas.get("disponibilidad"));
					for(Map<String, String> Disp : valuesDisp) {	
						final boolean estadoFinal= false;

						final SalasButton boton = new SalasButton(
								nombresala, // Sala
								idSala,
								Disp.get("horaInicio"),
								Disp.get("horaFin"),								
								Disp.get("modulonombre"),
								Integer.parseInt(Disp.get("moduloid")), // Módulo
								Integer.parseInt(Disp.get("ocupada")) == 1,//si esta reservada
								capSobre == 1,// Si se puede sobreescribir
								"",
								"",
								new ClickHandler() {

									public void onClick(ClickEvent event) {

										SalasButton btn = (SalasButton) event.getSource();

										if(btn.isDown()){
											if(admin == false){
												
											if(totalreservassemana!=0){
											if(totalreservashoy!=0){

												for(SalasButton bt : getHermanosVerticales(btn)) {

													bt.setDown(false);
												}
												
												if(cuantosApretados(btn, estadoFinal) > totalreservashoy) {
													btn.setDown(false);
													Window.alert("No puedes seleccionar más de "+totalreservashoy+" módulo(s) el día de hoy.");
													return;
												}
											}else{
												btn.setDown(false);
												Window.alert("No puedes reservar más módulos el día de hoy.");
											}
											}else{
												btn.setDown(false);
												Window.alert("No puedes reservar más módulos esta semana.");
												
											}
										}
										}
									}

								});

						hPanel.add(boton);
					}
					vPanel.add(hPanel);
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("Callback error");
			}
		});

		final FormPanel form = new FormPanel();
		form.setAction("");
		// Because we're going to add a FileUpload widget, 
		// we'll need to set the form to use the POST method, 
		// and multipart MIME encoding.
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);

		// Create a panel to hold all of the form widgets.
		VerticalPanel panel = new VerticalPanel();
		panel.setSpacing(4);
		form.setWidget(panel);

		final TextBox nombreEvento = new TextBox();	

		final TextBox asistentes = new TextBox();

		final  TextBox correo = new TextBox();

		final String nombreEventoTxt = "Nombre del evento";
		nombreEvento.setValue(nombreEventoTxt);
		nombreEvento.setStylePrimaryName("Text-Box");
		nombreEvento.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				nombreEvento.setValue(null); 

				if(asistentes.getValue().length()== 0){

					asistentes.setValue("Asistentes");
				}
				if(correo.getValue().length()== 0){
					correo.setValue("Correo electrónico");

				}

			}
		});
		final String asistentesTxt="Asistentes";
		asistentes.setValue(asistentesTxt);
		asistentes.setStylePrimaryName("Text-Box");
		asistentes.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				asistentes.setValue(null);   

				if(nombreEvento.getValue().length()== 0 ){
					nombreEvento.setValue("Nombre del evento");

				}
				if(correo.getValue().length()== 0){
					correo.setValue("Correo electrónico");

				}
			}
		});
		final String correoTxt="Correo electrónico";
		correo.setValue(correoTxt);
		correo.setStylePrimaryName("Text-Box");
		correo.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				correo.setValue(null);   

				if(asistentes.getValue().length()== 0){

					asistentes.setValue("Asistentes");
				}
				if(nombreEvento.getValue().length()== 0){
					nombreEvento.setValue("Nombre del evento");

				}
			}
		});

		panel.add(nombreEvento);
		panel.add(asistentes);
		panel.add(correo);
		// Add a 'submit' button.
		panel.add(new Button("Submit", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				form.submit();					
			}
		}));

		// Add an event handler to the form.
		form.addSubmitHandler(new FormPanel.SubmitHandler() {
			@Override
			public void onSubmit(SubmitEvent event) {
				// This event is fired just before the form is submitted. 
				// We can take this opportunity to perform validation.

				if (nombreEvento.getText().length() == 0 || nombreEvento.getText().equals(nombreEventoTxt)) {
					Window.alert("Debe escribir un  nombre de evento.");
					nombreEvento.setStylePrimaryName("Text-Box-vacio");
					nombreEvento.setValue("Nombre del evento");

					event.cancel();
				}else{
					nombreEvento.setStylePrimaryName("Text-Box");

				}	
				if (asistentes.getText().length() == 0 || asistentes.getText().equals(asistentesTxt)) {
					Window.alert("Debe escribir la cantidad de asistentes.");
					asistentes.setStylePrimaryName("Text-Box-vacio");
					asistentes.setValue("Asistentes");

					event.cancel();
				}	else{
					asistentes.setStylePrimaryName("Text-Box");


				}

				if (correo.getText().length() == 0 || correo.getText().equals(correoTxt)) {
					Window.alert("Debe escribir el correo de usuario.");

					correo.setStylePrimaryName("Text-Box-vacio");
					correo.setValue("Correo electrónico");

					event.cancel();
				}	else{
					correo.setStylePrimaryName("Text-Box");

				}	
			}
		});

		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {

				AjaxRequest.ajaxRequest("action=info", new AsyncCallback<AjaxData>() {

					@Override
					public void onSuccess(AjaxData result) {
						int rows = getApretados(vPanel).size() + 1;	
						
						VerticalPanel vPanelForm = new VerticalPanel();
						vPanelForm.setSpacing(4); 

						final Grid g = new Grid(rows, 5);
						g.setText(0,0,"Nombre sala");
						g.setText(0,1,"Módulo");
						g.setText(0,2,"Hora inicio");
						g.setText(0,3,"Hora término");
						g.setText(0,4,"Estado");
						g.setBorderWidth(1);
						List<Map<String, String>> values = getApretados(vPanel);
						
						String sala = "inicio";
						String modulo ="inicio";
						String nombreSalas="inicio";
						String nombreModulo="inicio";
						String  inicio="inicio";
						String  termino="inicio";
						for(Map<String, String> Disp : values) {
							AjaxRequest.moodleUrl = url;
							sala= sala+","+Disp.get("idSala");
							modulo= modulo+","+Disp.get("idModulo");
							nombreSalas=nombreSalas+","+Disp.get("nombreSala");
							nombreModulo=nombreModulo+","+Disp.get("nombreModulo");
							inicio=inicio+","+Disp.get("inicio");
							termino=termino+","+Disp.get("termino");
						}
						
						
							String param = "&inicio="+inicio+"&termino="+termino+"&nombremodulo="+nombreModulo+"&nombresala="+nombreSalas+"&moduleid="+modulo+"&room="+sala+"&date="+date+"&name="+nombreEvento.getValue()+"&asistentes="+asistentes.getValue()+"&multiply="+multiply+"&finalDate="+finalDate+"&days="+days+"&frequency="+frequency;
							
							AjaxRequest.ajaxRequest("action=submission"+param, new AsyncCallback<AjaxData>() {

								@Override
								public void onSuccess(AjaxData result) {
									Map<String, String> values = AjaxRequest.getValueFromResult(result);
									
									List<Map<String, String>> valuesReservas = AjaxRequest.getValuesFromResultString(values.get("ok"));
									List<Map<String, String>> valuesReservasE = AjaxRequest.getValuesFromResultString(values.get("err"));
									for(Map<String, String> reservas : valuesReservas) {	
									g.setText(contador,0,reservas.get("nombresala"));
									g.setText(contador,1,reservas.get("nombremodulo"));
									g.setText(contador,2,reservas.get("inicio"));
									g.setText(contador,3,reservas.get("termino"));
									g.setText(contador,4,"OK");
									contador++;
									}
									for(Map<String, String> reservas : valuesReservasE) {	
										g.setText(contador,0,reservas.get("nombresala"));
										g.setText(contador,1,reservas.get("nombremodulo"));
										g.setText(contador,2,reservas.get("inicio"));
										g.setText(contador,3,reservas.get("termino"));
										g.setText(contador,4,"Error");
										contador++;
										
										
									}
								
								}
								public void onFailure(Throwable caught) {
									System.out.println("Callback error");
								}

							});

						
						
						Map<String, String> valuesU = AjaxRequest.getValueFromResult(result);
						Map<String, String> valuesUser = AjaxRequest.getValueFromResultString(valuesU.get("User"));


						Date fecha = new Date(date * 1000L); // *1000 is to convert seconds to milliseconds
						Date fechaFinal = new Date(finalDate * 1000L); // *1000 is to convert seconds to milliseconds
						DateTimeFormat fmt = DateTimeFormat.getFormat("d/M/yyyy");
						
						
						
						Grid info = new Grid(7, 2);
						info.setBorderWidth(0);
						info.setText(0,0,"Nombre del evento:");
						info.setText(0,1,nombreEvento.getValue());
						info.setText(1,0,"Fecha inicio:");
						info.setText(1,1,fmt.format(fecha));
						info.setText(2,0,"Fecha término:");
						info.setText(2,1,fmt.format(fechaFinal));
						info.setText(3,0,"Frecuencia:");
						info.setText(3,1,"Cada "+frequency+" semana(s)");
						info.setText(4,0,"N° de asistentes:");
						info.setText(4,1,asistentes.getValue());
						info.setText(5,0,"Responsable");
						info.setText(5,1,valuesUser.get("firstname") +" "+valuesUser.get("lastname"));
						info.setText(6,0,"Correo responsable:");
						info.setText(6,1,valuesUser.get("email"));

						vPanelForm.add(info);
						vPanelForm.add(g);
						RootPanel.get("salas").clear();
						RootPanel.get("salas").add(vPanelForm);
					}

					@Override
					public void onFailure(Throwable caught) {
						System.out.println("Callback error");
					}
				});

			}
		});

		DecoratorPanel decoratorPanel = new DecoratorPanel();
		decoratorPanel.add(form);


		RootPanel.get("salas").add(vPanel);
		RootPanel.get("salas").add(decoratorPanel);
		
	}

	private List<SalasButton> getHermanos(SalasButton btn) {

		List<SalasButton> hermanos = new ArrayList<SalasButton>();

		VerticalPanel vpanel = (VerticalPanel) btn.getParent().getParent();
		for(int i=0; i < vpanel.getWidgetCount(); i++) {
			HorizontalPanel hpanel = (HorizontalPanel) vpanel.getWidget(i);
			for(int j=0; j < hpanel.getWidgetCount(); j++) {
				if(!(hpanel.getWidget(j) instanceof SalasButton))
					continue;
				SalasButton bt = (SalasButton) hpanel.getWidget(j);
				if(bt != btn)
					hermanos.add(bt);
			}
		}

		return hermanos;
	}

	private List<SalasButton> getHermanosVerticales(SalasButton btn) {

		List<SalasButton> hermanos = new ArrayList<SalasButton>();

		VerticalPanel vpanel = (VerticalPanel) btn.getParent().getParent();

		for(int i=0; i < vpanel.getWidgetCount(); i++) {
			HorizontalPanel hpanel = (HorizontalPanel) vpanel.getWidget(i);
			for(int j=0; j < hpanel.getWidgetCount(); j++) {
				if(!(hpanel.getWidget(j) instanceof SalasButton))
					continue;
				SalasButton bt = (SalasButton) hpanel.getWidget(j);

				if(bt != btn && bt.getModuloid() == btn.getModuloid())
					hermanos.add(bt);
			}
		}

		return hermanos;
	}
	private List<Map<String, String>> getApretados(VerticalPanel vPanel) {
		List<Map<String, String>> hermanos = new ArrayList<Map<String,String>>();

		//List<String> hermanos = new ArrayList<String>();
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
					hermanos.add(obj);
				}

			}
		}

		return hermanos;
	}
	private int cuantosApretados(SalasButton btn, boolean reserva) {
		int total = 0;
		if(reserva == false){
			if(btn.isDown())
				total++;
			for(SalasButton bt : getHermanos(btn)) {
				if(bt.isDown())
					total++;
			}
		}
		return total;


	}


}
