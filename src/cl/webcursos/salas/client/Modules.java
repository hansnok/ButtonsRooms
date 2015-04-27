package cl.webcursos.salas.client;


public class Modules {

	public int idModulo;
	public int nombreModulo;
	public String horaInicio;
    public String horaFin;
    //getters
    public int getid() {
		return idModulo;
	}  

    public int getnombre() {
  		return nombreModulo;
  	} 
    public String getinicio() {
  		return horaInicio;
  	} 
    public String getfinal() {
  		return horaFin;
  	} 
    //setters
    public void setid(int id) {
		this.idModulo=id;
	}  

    public void setnombre(int nombre) {
    	this.nombreModulo=nombre;
  	} 
    public void setinicio(String inicio) {
  		this.horaInicio=inicio;
  	} 
    public void setfinal(String fin) {
  		this.horaFin=fin;
  	} 
    
    
}
