/*
 Proyecto Java EE, DAGSS-2014
 */
package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Medicamento;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
@LocalBean
public class MedicamentoDAO extends GenericoDAO<Medicamento> {
     public Medicamento getMedicamentoId(long name){
         Query q = em.createQuery("SELECT m FROM Medicamento AS m "
                + "  WHERE (m.id LIKE :nombre) ");
        q.setParameter("nombre",name);        
        return filtrarResultadoUnico(q); 
    }
}
