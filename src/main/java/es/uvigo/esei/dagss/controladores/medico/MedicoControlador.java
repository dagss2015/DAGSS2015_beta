/*
 Proyecto Java EE, DAGSS-2013 sdighodghds
 */
package es.uvigo.esei.dagss.controladores.medico;

import es.uvigo.esei.dagss.controladores.autenticacion.AutenticacionControlador;
import es.uvigo.esei.dagss.dominio.daos.CitaDAO;
import es.uvigo.esei.dagss.dominio.daos.MedicoDAO;
import es.uvigo.esei.dagss.dominio.daos.PacienteDAO;
import es.uvigo.esei.dagss.dominio.entidades.Medico;
import es.uvigo.esei.dagss.dominio.entidades.Cita;
import es.uvigo.esei.dagss.dominio.entidades.EstadoCita;
import es.uvigo.esei.dagss.dominio.entidades.TipoUsuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import es.uvigo.esei.dagss.dominio.entidades.Paciente;
import es.uvigo.esei.dagss.dominio.entidades.Tratamiento;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ribadas
 */

@Named(value = "medicoControlador")
@SessionScoped
public class MedicoControlador implements Serializable {

    private Medico medicoActual;
    private String dni;
    private String numeroColegiado;
    private String password;
    private Cita citaActual;
    private Date diaActual;
    private List<Paciente> pacientes;
    private List<Cita> citasMedico;
    private List<Tratamiento> tratamientos;

    @Inject
    private AutenticacionControlador autenticacionControlador;
    

    @EJB
    private MedicoDAO medicoDAO;
    @EJB
    private PacienteDAO pacienteDAO;
    @EJB
    private CitaDAO citaDAO;

    /**
     * Creates a new instance of AdministradorControlador
     */
    public MedicoControlador() {
        diaActual=Calendar.getInstance().getTime();
    }
    
    public void cargarPacientes(){
        pacientes= pacienteDAO.buscarPorMedico(medicoActual.getId()); 
    }
    public void cargarCitasHoy(Date hoy){    
        citasMedico=citaDAO.getCitasMedico(medicoActual.getId(),hoy);
    }
    public Date getDiaActual(){
        return diaActual;
    }
    public List<Tratamiento> getTratamiento(){
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
    public String doShowCita(Cita citaActual) {
        this.citaActual=citaActual;
        return "detallesCita";
    }
    public String doShowTratamiento() {
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
    
    
    
    
}
