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
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 *

 * 
15. Brisanje ocene korisnika za video snimak
 * 
 * 
 * @author fafulja
 */
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Path("ocena")
public class OcenaResurs {
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory cf;
    @Resource(lookup="QSPS3")
    private Queue q1;
    @Resource(lookup="QPS3S")
    private Queue q2;
    
    private int id = 1;
    
    @PUT
    @Path("napravi/{idKor}/{idVid}/{ocena}")
    public Response addVote(@PathParam("idKor") int idKor, @PathParam("idVid") int idVid, @PathParam("ocena") int ocena) throws JMSException {
        if (ocena < 1 || ocena > 5) return Response.status(400).entity("Ocena mora biti broj od 1 do 5").build();
        JMSContext context = cf.createContext();
        JMSProducer producer = context.createProducer();
        StringTokenizer st;
        int status;
        String stat = null;
   
        // posalji zahtjev podsistemu1
        try (JMSConsumer consumer = context.createConsumer(q2, "id = " + id)) {
            // posalji zahtjev podsistemu1
            TextMessage tm = context.createTextMessage("napravi/" + idKor + "/" + idVid + "/" + ocena);
            tm.setStringProperty("tip", "Ocena/PUT");
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
    
    @POST
    @Path("promijeni/{idKor}/{idVid}/{ocena}")
    public Response changeVote(@PathParam("idKor") int idKor, @PathParam("idVid") int idVid, @PathParam("ocena") int ocena) throws JMSException {
        if (ocena < 1 || ocena > 5) return Response.status(400).entity("Ocena mora biti broj od 1 do 5").build();
        JMSContext context = cf.createContext();
        JMSProducer producer = context.createProducer();
        StringTokenizer st;
        int status;
        String stat = null;
   
        // posalji zahtjev podsistemu1
        try (JMSConsumer consumer = context.createConsumer(q2, "id = " + id)) {
            // posalji zahtjev podsistemu1
            TextMessage tm = context.createTextMessage("promijeni/" + idKor + "/" + idVid + "/" + ocena);
            tm.setStringProperty("tip", "Ocena/POST");
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
    @Path("dohvati/{idVid}")
    public Response getVotes(@PathParam("idVid") int idVid) throws JMSException {
        JMSContext context = cf.createContext();
        JMSProducer producer = context.createProducer();

        StringBuilder sb = new StringBuilder();
        
        try (JMSConsumer consumer = context.createConsumer(q2, "id = " + id)) {
            // posalji zahtjev podsistemu1
            TextMessage tm = context.createTextMessage("dohvati/" + idVid);
            tm.setStringProperty("tip", "Ocena/GET");
            tm.setIntProperty("id", id);
            producer.send(q1, tm);
            // primi odgovor od podsistema1
            
            
            TextMessage om = (TextMessage)consumer.receive();
            int size = om.getIntProperty("size");
            if (size == 0) return Response.status(200).entity("Nema ocena za dati video").build();
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
    
    @DELETE
    @Path("obrisi/{idKor}/{idVid}")
    public Response deleteVote(@PathParam("idKor") int idKor, @PathParam("idVid") int idVid) throws JMSException {
         JMSContext context = cf.createContext();
         JMSProducer producer = context.createProducer();
         StringTokenizer st;
         int status;
        // posalji zahtjev podsistemu1
        try (JMSConsumer consumer = context.createConsumer(q2, "id = " + id)) {
            // posalji zahtjev podsistemu1
            TextMessage tm = context.createTextMessage(idKor + "/" + idVid);
            tm.setStringProperty("tip", "Ocena/DELETE");
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
    
}
