/*
 Proyecto Java EE, DAGSS-2013
 */

package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Paciente;
import es.uvigo.esei.dagss.dominio.entidades.Tratamiento;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
@LocalBean
public class TratamientoDAO extends GenericoDAO<Tratamiento>{
   public List<Tratamiento> getTratamientoPaciente(Long idPaciente){
       Query q = em.createQuery("SELECT t FROM Tratamiento AS t "
                + "  WHERE (t.paciente.id = :idPaciente) ");
        q.setParameter("idPaciente", idPaciente);
        return q.getResultList();
   }
}
