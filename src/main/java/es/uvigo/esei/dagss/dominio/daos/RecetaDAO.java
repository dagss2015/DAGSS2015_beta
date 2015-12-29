/*
 Proyecto Java EE, DAGSS-2014
 */

package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Receta;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
@LocalBean
public class RecetaDAO extends GenericoDAO<Receta>{
     
     public List<Receta> getRecetaTratamiento(Long idReceta){
       Query q = em.createQuery("SELECT   r  FROM  Receta  AS r "
                + "  WHERE (r.prescripcion.tratamiento.id = :idReceta) ");
        q.setParameter("idReceta", idReceta);
        return q.getResultList();
   }
    
}
