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
 * @author fafulja
 */

@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Path("video")
public class VideoResurs {
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory cf;
    @Resource(lookup="QSPS2")
    private Queue q1;
    @Resource(lookup="QPS2S")
    private Queue q2;
    
    private int id = 1;
    
    
    @PUT
    @Path("napravi/{naziv}/{trajanje}/{idKor}")
    public Response createVideo(@PathParam("naziv") String naziv, 
             @PathParam("trajanje") double trajanje, @PathParam("idKor") int idKor) throws JMSException {
         JMSContext context = cf.createContext();
         JMSProducer producer = context.createProducer();
         StringTokenizer st;
         int status;
        // posalji zahtjev podsistemu1
        try (JMSConsumer consumer = context.createConsumer(q2, "id = " + id)) {
            // posalji zahtjev podsistemu1
            TextMessage tm = context.createTextMessage(idKor + "/" + naziv + "/" + trajanje);
            tm.setStringProperty("tip", "Video/PUT");
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
    
    @POST
    @Path("preimenuj/{idVid}/{naziv}")
    public Response updateName(@PathParam("idVid") int idVid, @PathParam("naziv") String naziv) throws JMSException {
         JMSContext context = cf.createContext();
         JMSProducer producer = context.createProducer();
         StringTokenizer st;
         int status;
        // posalji zahtjev podsistemu1
        try (JMSConsumer consumer = context.createConsumer(q2, "id = " + id)) {
            // posalji zahtjev podsistemu1
            TextMessage tm = context.createTextMessage("preimenuj" + "/" + idVid + "/" + naziv);
            tm.setStringProperty("tip", "Video/POST");
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
    
    @POST
    @Path("kategorija/{idVid}/{idKat}")
    public Response addCategory(@PathParam("idVid") int idVid, @PathParam("idKat") int idKat) throws JMSException {
         JMSContext context = cf.createContext();
         JMSProducer producer = context.createProducer();
         StringTokenizer st;
         int status;
        // posalji zahtjev podsistemu1
        try (JMSConsumer consumer = context.createConsumer(q2, "id = " + id)) {
            // posalji zahtjev podsistemu1
            TextMessage tm = context.createTextMessage("kategorija" + "/" + idVid + "/" + idKat);
            tm.setStringProperty("tip", "Video/POST");
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
    public Response getVideos() throws JMSException {
        JMSContext context = cf.createContext();
        JMSProducer producer = context.createProducer();

        StringBuilder sb = new StringBuilder();
        
        try (JMSConsumer consumer = context.createConsumer(q2, "id = " + id)) {
            // posalji zahtjev podsistemu1
            TextMessage tm = context.createTextMessage(null);
            tm.setStringProperty("tip", "Video/GET");
            tm.setIntProperty("id", id);
            producer.send(q1, tm);
            // primi odgovor od podsistema1
            
            
            TextMessage om = (TextMessage)consumer.receive();
            int size = om.getIntProperty("size");
            if (size == 0) return Response.status(200).entity("Nema videa").build();
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
    @Path("{idVid}")
    public Response getCategories(@PathParam("idVid") int idVid) throws JMSException {
        JMSContext context = cf.createContext();
        JMSProducer producer = context.createProducer();
        StringBuilder sb = new StringBuilder();
        
        // primi samo odgovore na svoj zahtjev
        try (JMSConsumer consumer = context.createConsumer(q2, "id = " + id);) {
            TextMessage tm = context.createTextMessage();
            tm.setStringProperty("tip", "Video/GET");
            tm.setText(Integer.toString(idVid));
            tm.setIntProperty("id", id);
            producer.send(q1, tm);
            // producer salje zahtjev
            // primi odgovor od podsistema1
            
            
            TextMessage om = (TextMessage)consumer.receive();
            // consumer prima samo odgovore koji su na njegov id
            int size = om.getIntProperty("size");
            
            // consumer cita size. Ako je size dobar, on nastavlja da cita toliko poruka
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
    
    @DELETE
    @Path("{idVid}/{idKor}")
    public Response deleteVideo(@PathParam("idVid") int idVid, @PathParam("idKor") int idKor) throws JMSException {
         JMSContext context = cf.createContext();
         JMSProducer producer = context.createProducer();
         StringTokenizer st;
         int status;
        // posalji zahtjev podsistemu1
        try (JMSConsumer consumer = context.createConsumer(q2, "id = " + id)) {
            // posalji zahtjev podsistemu1
            TextMessage tm = context.createTextMessage(idVid + "/" + idKor);
            tm.setStringProperty("tip", "Video/DELETE");
            tm.setIntProperty("id", id);
            producer.send(q1, tm);
            // primi odgovor od podsistema1
            TextMessage om = (TextMessage)consumer.receive();
            st = new StringTokenizer(om.getText(), "/");
            String stat = st.nextToken();
            status = Integer.parseInt(stat);
        }
         return Response.status(20).entity(st.nextToken()).build();
        // TO DO obavijesti i podsistem 3 da ukloni ono sto treba
        
    }
}
