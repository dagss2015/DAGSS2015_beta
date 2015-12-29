/*
 Proyecto Java EE, DAGSS-2013 sdighodghds
 */
package es.uvigo.esei.dagss.controladores.medico;

import es.uvigo.esei.dagss.controladores.autenticacion.AutenticacionControlador;
import es.uvigo.esei.dagss.dominio.daos.CitaDAO;
import es.uvigo.esei.dagss.dominio.daos.FarmaciaDAO;
import es.uvigo.esei.dagss.dominio.daos.MedicamentoDAO;
import es.uvigo.esei.dagss.dominio.daos.MedicoDAO;
import es.uvigo.esei.dagss.dominio.daos.PacienteDAO;
import es.uvigo.esei.dagss.dominio.daos.RecetaDAO;
import es.uvigo.esei.dagss.dominio.daos.TratamientoDAO;
import es.uvigo.esei.dagss.dominio.entidades.Medico;
import es.uvigo.esei.dagss.dominio.entidades.Cita;
import es.uvigo.esei.dagss.dominio.entidades.EstadoCita;
import es.uvigo.esei.dagss.dominio.entidades.EstadoReceta;
import es.uvigo.esei.dagss.dominio.entidades.Farmacia;
import es.uvigo.esei.dagss.dominio.entidades.Medicamento;
import es.uvigo.esei.dagss.dominio.entidades.TipoUsuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import es.uvigo.esei.dagss.dominio.entidades.Paciente;
import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import es.uvigo.esei.dagss.dominio.entidades.Receta;
import es.uvigo.esei.dagss.dominio.entidades.Tratamiento;
import static java.lang.Math.round;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author ribadas
 */

@Named(value = "medicoControlador")
@SessionScoped
public class MedicoControlador implements Serializable {

    private Medico medicoActual;
    private String farmacia;
    private String medicamento;
    private String dni;
    private String numeroColegiado;
    private String password;
    private Cita citaActual;
    private Date diaActual;
    private Tratamiento tratamientoActual;
    private Receta recetaActual;
    private Farmacia farmaciaActual;
    private Prescripcion prescripcionActual;
    private Medicamento medicamentoActual;
    private List<Paciente> pacientes;
    private List<Cita> citasMedico;
    private List<Tratamiento> tratamientos;
    private List<Receta> recetas;

    @Inject
    private AutenticacionControlador autenticacionControlador;
    

    @EJB
    private MedicoDAO medicoDAO;
    @EJB
    private PacienteDAO pacienteDAO;
    @EJB
    private CitaDAO citaDAO;
    @EJB
    private TratamientoDAO tratamientoDAO;
    @EJB
    private RecetaDAO recetaDAO;
    @EJB
    private MedicamentoDAO medicamentoDAO;
     @EJB
    private FarmaciaDAO farmaciaDAO;

    /**
     * Creates a new instance of AdministradorControlador
     */
    public MedicoControlador() {
        diaActual=Calendar.getInstance().getTime();
    }
    public void letMe(){
        System.out.println(tratamientos.get(0).getPaciente().getNombre());
    }
    public void setFarmacia(String name){
        farmacia=name;
    }
    public void setMedicamento(String name){
        medicamento=name;
    }
    public String getMedicamento(){
        return medicamento;
    }
    public void cargarMedicamento(){
        //medicamentoActual=medicamentoDAO.getMedicamentoNombre(medicamento);
    }
    public void cargarFarmacia(){
        farmaciaActual=farmaciaDAO.getFarmaciaNombre(farmacia);
    }
    public void cargarRecetas(){
        recetas=recetaDAO.getRecetaTratamiento(tratamientoActual.getId());
    }
    public void cargarTratamientos(){
        tratamientos=tratamientoDAO.getTratamientoPaciente(citaActual.getPaciente().getId());
    }
    public void cargarPacientes(){
        pacientes= pacienteDAO.buscarPorMedico(medicoActual.getId()); 
    }
    public void cargarCitasHoy(Date hoy){    
        citasMedico=citaDAO.getCitasMedico(medicoActual.getId(),hoy);
    }
    public Receta getRecetaActual(){
        return recetaActual;
    }
    public  String getFarmacia(){
        return farmacia;
    }
    public Date getDiaActual(){
        return diaActual;
    }
    public void setDiaActual(Date dia){
        diaActual=dia;
    }
    public List<Receta> getRecetas(){
        return recetas;
    }
    public List<Tratamiento> getTratamientos(){
        letMe();
        return tratamientos;
    }
    public List<Paciente>  getPacientes(){
       return pacientes;           
    }
    public List<Cita>  getCitasMedico(){
       return citasMedico;           
    }
    public Cita getCitaActual(){
        return citaActual;
    }
    public Tratamiento getTratamientoActual(){
        return tratamientoActual;
    }
    
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNumeroColegiado() {
        return numeroColegiado;
    }

    public void setNumeroColegiado(String numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Medico getMedicoActual() {
        return medicoActual;
    }
   

    public void setMedicoActual(Medico medicoActual) {
        this.medicoActual = medicoActual;
    }
    public Prescripcion getPrescripcionActual(){
            return prescripcionActual;
    }

    private boolean parametrosAccesoInvalidos() {
        return (((dni == null) && (numeroColegiado == null)) || (password == null));
    }

    private Medico recuperarDatosMedico() {
        Medico medico = null;
        if (dni != null) {
            medico = medicoDAO.buscarPorDNI(dni);
        }
        if ((medico == null) && (numeroColegiado != null)) {
            medico = medicoDAO.buscarPorNumeroColegiado(numeroColegiado);
        }
        return medico;
    }

    public String doLogin() {
        String destino = null;
        if (parametrosAccesoInvalidos()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se ha indicado un DNI ó un número de colegiado o una contraseña", ""));
        } else {
            Medico medico = recuperarDatosMedico();
            if (medico == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No existe ningún médico con los datos indicados", ""));
            } else {
                if (autenticacionControlador.autenticarUsuario(medico.getId(), password,
                        TipoUsuario.MEDICO.getEtiqueta().toLowerCase())) {
                    medicoActual = medico;
                    cargarPacientes();
                    cargarCitasHoy(Calendar.getInstance().getTime());
                    destino = "privado/index";
                 
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Credenciales de acceso incorrectas", ""));
                }
            }
        }
        return destino;
    }

    //Acciones
    public String doNewTratamiento(){
        tratamientoActual=new Tratamiento(citaActual.getPaciente(),citaActual.getMedico(), "nada aun", diaActual, diaActual);
        return "newTratamiento";
    }
    public String doNewReceta(){
        prescripcionActual = new Prescripcion();
        recetaActual=new Receta();
        
        return "newReceta";
    }
    /////SUPUESTAMENTE AKI UN NULL POINTER
    public void cantidadMedicamento(){
   
       // recetaActual.setCantidad(round(y/x));
       recetaActual.setCantidad(2);
    }
    
    
    public String doShowCita(Cita citaActual) {
        this.citaActual=citaActual;
        cargarTratamientos();
        return "detallesCita";
    }
    public String doShowTratamiento(Tratamiento tratamientoActual) {
        this.tratamientoActual=tratamientoActual;
        cargarRecetas();
        return "detallesTratamiento";
    }
    public String doNewDay(Date dia) {  
        diaActual=dia;
        cargarCitasHoy(dia);
        return "index";
    }
    ///marcado citas
    public void doMarcarCitaCompletada(Cita cita){
        cita.setEstado(EstadoCita.COMPLETADA);
        citaDAO.actualizar(cita);
        cargarCitasHoy(diaActual);
    }
    public void doMarcarCitaAnulada(Cita cita){
        cita.setEstado(EstadoCita.ANULADA);
        citaDAO.actualizar(cita);
        cargarCitasHoy(diaActual);
    }
    public void doMarcarCitaAusente(Cita cita){
        cita.setEstado(EstadoCita.AUSENTE);
        citaDAO.actualizar(cita);
        cargarCitasHoy(diaActual);
    }
    public void doMarcarCitaPlanificada(Cita cita){
        cita.setEstado(EstadoCita.PLANIFICADA);
        citaDAO.actualizar(cita);
        cargarCitasHoy(diaActual);
    }
    public String getEstadoCita(Cita cita){
        return cita.getEstado().name();
    }
    /////////////////////////////
    public void actualizarCitas(SelectEvent event) {
        System.out.println("entra citas "+diaActual.toString());
        cargarCitasHoy(diaActual);
    }
    public void guardarTratamiento(){
        tratamientoDAO.actualizar(tratamientoActual);
        cargarTratamientos();
    }
    public void guardarReceta(){
        Medicamento med=new Medicamento("termal", "agua", "yo", "Coello", 2);
        cantidadMedicamento();
        prescripcionActual.setMedicamento(med);
        recetaActual.setFarmacia(farmaciaActual);
        prescripcionActual.setTratamiento(tratamientoActual);
        recetaActual.setPrescripcion(prescripcionActual);  
        recetaDAO.actualizar(recetaActual);
        cargarRecetas();
    }

}
