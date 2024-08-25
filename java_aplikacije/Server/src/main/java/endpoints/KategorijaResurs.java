/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package endpoints;


import java.util.StringTokenizer;
import javax.annotation.Resource;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author fafulja
 */
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Path("kategorija")
public class KategorijaResurs {

    
    //@PersistenceContext(name = "PS2PU")
   //EntityManager em;
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory cf;
    @Resource(lookup="QSPS2")
    private Queue q1;
    @Resource(lookup="QPS2S")
    private Queue q2;
    
    private int id = 1;
    

    
    @Path("napravi/{naziv}")
    @PUT
    public Response createCategory(@PathParam("naziv") String naziv) throws JMSException {
         
         JMSContext context = cf.createContext();
         JMSProducer producer = context.createProducer();
         StringTokenizer st;
         int status;
        // posalji zahtjev podsistemu1
        try (JMSConsumer consumer = context.createConsumer(q2, "id = " + id)) {
            // posalji zahtjev podsistemu1
            TextMessage tm = context.createTextMessage(naziv);
            tm.setStringProperty("tip", "Kategorija/PUT");
            tm.setIntProperty("id", id);
            producer.send(q1, tm);
            // primi odgovor od podsistema1
            TextMessage om = (TextMessage)consumer.receive();
            st = new StringTokenizer(om.getText(), "/");
            String stat = st.nextToken();
            status = Integer.parseInt(stat);
        }
         return Response.status(200).entity(st.nextToken()).build();
    }
    
    @GET
    public Response getKategorije() throws JMSException {
        
        JMSContext context = cf.createContext();
        JMSProducer producer = context.createProducer();
        StringBuilder sb = new StringBuilder();
        
        try (JMSConsumer consumer = context.createConsumer(q2, "id = " + id)) {
            // posalji zahtjev podsistemu1
            TextMessage tm = context.createTextMessage(null);
            tm.setStringProperty("tip", "Kategorija/GET");
            tm.setIntProperty("id", id);
            producer.send(q1, tm);
            // primi odgovor od podsistema1
            
            
            TextMessage om = (TextMessage)consumer.receive();
            int size = om.getIntProperty("size");
            if (size == 0) return Response.status(200).entity("Nema kategorija").build();
            String contents = om.getText();
            sb.append(contents).append("\n");
            for (int i = 1 ; i < size; i++) {
                om = (TextMessage)consumer.receive();
                contents = om.getText();
                sb.append(contents).append("\n");
            }
                
            
            
        }
         return Response.status(200).entity(sb.toString()).build();
        
    }
    
    
}
