/*
 Proyecto Java EE, DAGSS-2014
 */

package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Cita;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;


@Stateless
@LocalBean
public class CitaDAO  extends GenericoDAO<Cita>{    

    public List<Cita> getCitasMedico(Long idMedico,Date fecha){
        Query q = em.createQuery("SELECT c FROM Cita AS c "
                + "  WHERE (c.medico.id = :idMedico AND c.fecha = :fecha ) ");
        q.setParameter("idMedico", idMedico);
        q.setParameter("fecha", fecha);
        return q.getResultList();
    }
}
