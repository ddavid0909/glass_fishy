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
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 *
 *  Kreiranje paketa
10. Promena mesečne cene za paket
11. Kreiranje pretplate korisnika na paket
 */

@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Path("paket")
public class PaketResurs {
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory cf;
    @Resource(lookup="QSPS3")
    private Queue q1;
    @Resource(lookup="QPS3S")
    private Queue q2;
    
    private int id = 1;
    
    @PUT
    @Path("napravi/{cijena}")
    public Response createPackage(@PathParam("cijena") float cijena) throws JMSException {
        JMSContext context = cf.createContext();
        JMSProducer producer = context.createProducer();
        StringTokenizer st;
        int status;
        String stat = null;
   
        // posalji zahtjev podsistemu1
        try (JMSConsumer consumer = context.createConsumer(q2, "id = " + id)) {
            // posalji zahtjev podsistemu1
            TextMessage tm = context.createTextMessage("napravi/" + Double.toString(cijena));
            tm.setStringProperty("tip", "Paket/PUT");
            tm.setIntProperty("id", id);
            producer.send(q1, tm);
            // primi odgovor od podsistema1
            TextMessage om = (TextMessage)consumer.receive();
            st = new StringTokenizer(om.getText(), "/");
            stat = st.nextToken();
            status = Integer.parseInt(stat);
        }

        return Response.status(200).entity("Paket je napravljen").build();
    }
    
    @POST
    @Path("promijeni/{idPak}/{novaCijena}")
    public Response addCategory(@PathParam("idPak") int idPak, @PathParam("novaCijena") float nc) throws JMSException {
         JMSContext context = cf.createContext();
         JMSProducer producer = context.createProducer();
         StringTokenizer st;
         int status;
        // posalji zahtjev podsistemu1
        try (JMSConsumer consumer = context.createConsumer(q2, "id = " + id)) {
            // posalji zahtjev podsistemu1
            TextMessage tm = context.createTextMessage("promijeni" + "/" + idPak + "/" + nc);
            tm.setStringProperty("tip", "Paket/POST");
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
    
    @PUT
    @Path("pretplata/{idKor}/{idPak}")
    public Response createSub(@PathParam("idKor") int idKor, @PathParam("idPak") int idPak) throws JMSException {
        JMSContext context = cf.createContext();
        JMSProducer producer = context.createProducer();
        StringTokenizer st;
        int status;
        String stat = null;
   
        // posalji zahtjev podsistemu1
        try (JMSConsumer consumer = context.createConsumer(q2, "id = " + id)) {
            // posalji zahtjev podsistemu1
            TextMessage tm = context.createTextMessage("pretplata/" + idKor + "/" + idPak);
            tm.setStringProperty("tip", "Paket/PUT");
            tm.setIntProperty("id", id);
            producer.send(q1, tm);
            // primi odgovor od podsistema1
            TextMessage om = (TextMessage)consumer.receive();
            st = new StringTokenizer(om.getText(), "/");
            stat = st.nextToken();
            status = Integer.parseInt(stat);
        }

        return Response.status(200).entity(st.nextToken()).build();
    }
    
    @GET 
    public Response getPackages() throws JMSException {
        JMSContext context = cf.createContext();
        JMSProducer producer = context.createProducer();

        StringBuilder sb = new StringBuilder();
        
        try (JMSConsumer consumer = context.createConsumer(q2, "id = " + id)) {
            // posalji zahtjev podsistemu1
            TextMessage tm = context.createTextMessage(null);
            tm.setStringProperty("tip", "Paket/GET");
            tm.setIntProperty("id", id);
            producer.send(q1, tm);
            // primi odgovor od podsistema1
            
            
            TextMessage om = (TextMessage)consumer.receive();
            int size = om.getIntProperty("size");
            if (size == 0) return Response.status(200).entity("Nema paketa").build();
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

    @GET
    @Path("pretplate/{idKor}")
    public Response getSubs(@PathParam("idKor") int idKor) throws JMSException {
        JMSContext context = cf.createContext();
        JMSProducer producer = context.createProducer();

        StringBuilder sb = new StringBuilder();
        
        try (JMSConsumer consumer = context.createConsumer(q2, "id = " + id)) {
            // posalji zahtjev podsistemu1
            TextMessage tm = context.createTextMessage(Integer.toString(idKor));
            tm.setStringProperty("tip", "Paket/GET");
            tm.setIntProperty("id", id);
            producer.send(q1, tm);
            // primi odgovor od podsistema1
            
            
            TextMessage om = (TextMessage)consumer.receive();
            int size = om.getIntProperty("size");
            if (size == 0) return Response.status(200).entity("Nema pretplata").build();
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
