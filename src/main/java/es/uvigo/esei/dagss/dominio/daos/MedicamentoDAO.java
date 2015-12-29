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
    public List<Medicamento> getMedicamentos(){
       Query q = em.createQuery("SELECT * FROM Receta ");
        return q.getResultList();
   }
    public Medicamento getMedicamentoNombre(String name){
         Query q = em.createQuery("SELECT m FROM Medicamento AS m "
                + "  WHERE (m.nombre LIKE :patron)");
        q.setParameter("nombre",name);        
        return filtrarResultadoUnico(q); 
    }
}
