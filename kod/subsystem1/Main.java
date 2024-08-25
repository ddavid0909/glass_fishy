/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package subsystem1;

import entities.Korisnik;
import entities.Mesto;
import java.util.List;
import java.util.StringTokenizer;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.core.Response;

/**
 *
 * @author fafulja
 */
public class Main {
    @Resource(lookup="jms/__defaultConnectionFactory")
    private static ConnectionFactory cf;
    @Resource(lookup="QSPS1")
    private static Queue q1;
    @Resource(lookup="QPS1S")
    private static Queue q2;
    @Resource(lookup="QPSPSPS")
    private static Queue q3;
    /**
     * @param args the command line arguments
     */
    
    private static void sendEndMessage(JMSContext context, String text, int id, int size, JMSProducer producer, String message) throws JMSException {
        TextMessage mess = context.createTextMessage(text);
        mess.setIntProperty("id", id);
        mess.setIntProperty("size", size);
        mess.setText(message);
        System.out.println(message);
        producer.send(q2, mess);
        
    }
    
    private static void sendAnswerToSubsystem(JMSContext context, Queue q, String entity, int status, int id, JMSProducer producer) throws JMSException {
        TextMessage tm = context.createTextMessage(status + "/" + entity);
                            tm.setIntProperty("id", id);
                            System.out.println(entity);
                            producer.send(q, tm);
                            
    }
    
    
    private static void notify2And3(JMSContext context, JMSProducer producer, String method, String request) throws JMSException {
        // send request to subsystem 2
            TextMessage tm = context.createTextMessage(request);
            tm.setStringProperty("tip", method);
            tm.setIntProperty("id", 12);
            producer.send(q3, tm);
        // send request to subsystem 3
            tm = context.createTextMessage(request);
            tm.setStringProperty("tip", method);
            tm.setIntProperty("id", 13);
            producer.send(q3, tm);
            
        // receive responses.
            JMSConsumer consumer1 = context.createConsumer(q3, "id = " + 21);
            JMSConsumer consumer2 = context.createConsumer(q3, "id = " + 31);
            
            consumer1.receive();
            consumer2.receive();
            
            consumer1.close();
            consumer2.close();
            
    }
    
    public static void main(String[] args) throws JMSException {
       EntityManagerFactory emf = Persistence.createEntityManagerFactory("Subsystem1PU");
       EntityManager em = emf.createEntityManager(); 
        
       JMSContext context = cf.createContext();
       JMSProducer producer = context.createProducer();
       JMSConsumer consumer = context.createConsumer(q1);
       int status = 0;
       String entity = null;
       System.out.println("Pokrenuto");
       int count = 0;
       
        try (JMSConsumer consumer1 = context.createConsumer(q2)) {
            while(consumer1.receiveNoWait() != null) count++;
            while(consumer.receiveNoWait() != null) count++;
        }
       
       System.out.println("Obrisano " + count);
       
       while (true) {
           // primi poruku
           TextMessage tm = (TextMessage)consumer.receive();
           System.out.println(tm.getText());
           String property = tm.getStringProperty("tip");
           int id = tm.getIntProperty("id");
           StringTokenizer st = new StringTokenizer(property, "/");
           String endpoint = st.nextToken();
           
           switch(endpoint) {
               case "Korisnik":
                   endpoint = st.nextToken();
                   if (endpoint.equals("PUT")) {
                       // {ime}/{mejl}/{godiste}/{pol}/{idMes}
                       
                       st = new StringTokenizer(tm.getText(), "/");
                       
                       String ime = st.nextToken();
                       String mejl = st.nextToken();
                       int godiste = Integer.parseInt(st.nextToken());
                       String pol = st.nextToken();
                       int idMes = Integer.parseInt(st.nextToken());
                       
                       Mesto mesto = em.find(Mesto.class, idMes);
                       if (mesto == null) {
                            Main.sendAnswerToSubsystem(context, q2, "Zadato mjesto ne postoji", 400, id, producer);
                            continue;
                       }
                       List<Korisnik> korisnici = em.createNamedQuery("Korisnik.findByMejl").setParameter("mejl", mejl).getResultList();
                       if (!korisnici.isEmpty()){
                           Main.sendAnswerToSubsystem(context, q2, "Korisnik sa zadatim mejlom vec postoji", 400, id, producer);
                           continue;
                       }
                        //JMSContext context = cf.createContext();
                        //JMSProducer producer = context.createProducer();
                        Korisnik k = new Korisnik();
                        k.setIdMes(mesto);
                        k.setIme(ime);
                        k.setMejl(mejl);
                        k.setPol(pol);
                        k.setGodiste(godiste);
                       
                       // TO - DO zavrsi ovde / transakcija plus javi podsistemu 2 o promjeni.
                       try {
                                  em.getTransaction().begin();
                                  em.persist(k);
                                  em.getTransaction().commit();
                                  Main.notify2And3(context, producer, "Korisnik/PUT", tm.getText());
                              } finally {
                                  entity = "Korisnik je dodat";
                                  status = 200;
                                  if (em.getTransaction().isActive())
                                          em.getTransaction().rollback();
                              } 
                   } else if (endpoint.equals("GET")) {
                        List<Korisnik> videi = em.createNamedQuery("Korisnik.findAll").getResultList();
                           int size = videi.size();
                               if (videi.isEmpty())  {
                                   sendEndMessage(context, null, id, 0, producer, "Nema videa");
                                   continue;
                               }
                               
                               for (Korisnik v : videi) {
                                   sendEndMessage(context, v.toString(), id, size, producer, v.toString());
                               }
                               continue;
                   } else if (endpoint.equals("POST")) {
                       // mail/{idKor}/mejl
                       Korisnik k = null;
                       st = new StringTokenizer(tm.getText(), "/");
                       endpoint = st.nextToken();
                       int idKor = Integer.parseInt(st.nextToken());
                       String text = st.nextToken();
                       if (endpoint.equals("mail")) {
                           
                           List<Korisnik> korisnici = em.createNamedQuery("Korisnik.findByMejl").setParameter("mejl", text).getResultList();
                           if (!korisnici.isEmpty()) {
                               status = 500;
                               entity = "Korisnik sa mejlom vec postoji";
                               Main.sendAnswerToSubsystem(context, q2, entity, status, id, producer);
                               continue;
                           }
                           k = em.find(Korisnik.class, idKor);
                           if (k == null) {
                               status = 500;
                               entity = "Zadati korisnik ne postoji";
                               Main.sendAnswerToSubsystem(context, q2, entity, status, id, producer);
                               continue;
                           }
                                k.setMejl(text);
                                try {
                                  em.getTransaction().begin();
                                  em.persist(k);
                                  em.getTransaction().commit();
                                  Main.notify2And3(context, producer, "Korisnik/POST", tm.getText());
                              } finally {
                                  entity = "Mejl promijenjen";
                                  status = 200;
                                  if (em.getTransaction().isActive())
                                          em.getTransaction().rollback();
                              } 
                            
                                
                       } else if (endpoint.equals("city")) {
                           List<Mesto> mesta = em.createNamedQuery("Mesto.findByNaziv").setParameter("naziv", text).getResultList();
                           if (mesta.isEmpty()) {
                               status = 500;
                               entity = "Zadato mjesto ne postoji";
                           } else {
                                k = em.find(Korisnik.class, idKor);

                                if (k == null) {
                                    status = 500;
                                    entity = "Korisnik ne postoji";
                                } else {
                                    k.setIdMes(mesta.get(0));
                                    try {
                                        em.getTransaction().begin();
                                        em.persist(k);
                                        em.getTransaction().commit();
                                    } finally {
                                        entity = "Grad promijenjen";
                                        status = 200;
                                        if (em.getTransaction().isActive())
                                          em.getTransaction().rollback();
                                    } 
                                    
                                    
                                    }
                           }
                           
                       }

                       
                       
                       
                       // city/{idKor}/naziv
                   }
                   break;
               case "Mesto":
                     endpoint = st.nextToken();
                     if (endpoint.equals("PUT")) {
                        String ime = tm.getText();
                        List<Mesto> mesta = em.createNamedQuery("Mesto.findByNaziv").setParameter("naziv", ime).getResultList();
                        if (!mesta.isEmpty()) {
                            status = 400;
                            entity = "Grad " + ime + " vec postoji";
                            Main.sendAnswerToSubsystem(context, q2, entity, status, id, producer);
                            continue;
                        }
                        Mesto mesto = new Mesto();
                        mesto.setNaziv(ime);
                        try {
                            em.getTransaction().begin();
                            em.persist(mesto);
                            em.getTransaction().commit();
                        } finally {
                            entity = "Dodat grad " + ime;
                            status = 200;
                            if (em.getTransaction().isActive())
                              em.getTransaction().rollback();
                        } 
                         
                         
                     } else if (endpoint.equals("GET")) {
                           List<Mesto> mesta = em.createNamedQuery("Mesto.findAll").getResultList();
                           int size = mesta.size();
                               if (mesta.isEmpty())  {
                                   sendEndMessage(context, null, id, 0, producer, "Nema videa");
                                   continue;
                               }
                               
                               for (Mesto v : mesta) {
                                   sendEndMessage(context, v.toString(), id, size, producer, v.toString());
                               }
                               continue;
                     
                     
                     
                     }
                   
                   
                   
                   
                   
                   
                   
                   break;
           }
           
           
          Main.sendAnswerToSubsystem(context, q2, entity, status, id, producer);
       }
       
       
    }
    
}
