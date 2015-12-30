/*
 Proyecto Java EE, DAGSS-2014
 */

package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
@LocalBean
public class PrescripcionDAO extends GenericoDAO<Prescripcion>{    
     public Prescripcion getPrescripcionesTratamiento(Long idPrescripcion){
       Query q = em.createQuery("SELECT   p  FROM  Prescripcion AS p "
                + "  WHERE (p.id = :idPrescripcion) ");
        q.setParameter("idPrescripcion", idPrescripcion);
        return filtrarResultadoUnico(q);
   }  
}
