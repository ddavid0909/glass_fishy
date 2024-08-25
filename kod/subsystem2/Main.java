/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package subsystem2;

import entities.Kategorija;
import entities.Korisnik;
import entities.Video;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

/**
 *
 * @author fafulja
 */


public class Main {
    @Resource(lookup="jms/__defaultConnectionFactory")
    private static ConnectionFactory cf;
    @Resource(lookup="QSPS2")
    private static Queue q1;
    @Resource(lookup="QPS2S")
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
    
    public static void notify3(JMSContext context, JMSProducer producer, String method, String request) throws JMSException {
        try (JMSConsumer consumer3 = context.createConsumer(q3, "id = " + 32)) {
            // send 23
            TextMessage tm = context.createTextMessage(request);
            tm.setStringProperty("tip", method);
            tm.setIntProperty("id", 23);
            producer.send(q3, tm);
            consumer3.receive();
            // wait for response.
        }
    }
    
    
    public static void main(String[] args) throws JMSException {
       EntityManagerFactory emf = Persistence.createEntityManagerFactory("Subsystem2PU");
       EntityManager em = emf.createEntityManager(); 
        
       JMSContext context = cf.createContext();
       JMSProducer producer = context.createProducer();
       JMSConsumer consumer = context.createConsumer(q1);
       
       // podsistem 2 preko ovog listenera prima poruke od podsistema 1
       JMSConsumer listener = context.createConsumer(q3, "id = " + 12);
       listener.setMessageListener((Message msg)-> {
           TextMessage tm = (TextMessage)msg;
           try {
               // iz teksta poruke se vidi zahtjev, a iz property-ja se vidi tip.
               String odg = tm.getText();
               String property = tm.getStringProperty("tip");
               StringTokenizer st = new StringTokenizer(property, "/");
               st.nextToken();
               property = st.nextToken();
               System.out.println(property);
               fulfillRequest(odg, property, em);
               
               tm = context.createTextMessage(null);
               tm.setIntProperty("id", 21);
               producer.send(q3, tm);
               
           } catch (JMSException ex) {}
           
       
       });
       
       int status = 0;
       String entity = null;
       System.out.println("Pokrenuto");
       int count = 0;
       
        try (JMSConsumer consumer1 = context.createConsumer(q2)) {
            while(consumer1.receiveNoWait() != null) count++;
            while(consumer.receiveNoWait() != null) count++;
        }
       
       System.out.println("Obrisano " + count);
      
       while(true) {
           TextMessage tm = (TextMessage)consumer.receive();
           System.out.println(tm.getText());
           String property = tm.getStringProperty("tip");
           int id = tm.getIntProperty("id");
           StringTokenizer st = new StringTokenizer(property, "/");
           String endpoint = st.nextToken();
           
           switch (endpoint) {
               
               case "Kategorija":
                   endpoint = st.nextToken();
                   if (endpoint.equals("PUT")) {
                       String naziv = tm.getText();
                       List<Kategorija> k = em.createNamedQuery("Kategorija.findByNaziv").setParameter("naziv", naziv).getResultList();
                       if (!k.isEmpty()) {
                            status = 500;
                            entity = "Ova kategorija vec postoji";
                       } else {
                            Kategorija kategorija = new Kategorija();
                            kategorija.setNaziv(naziv);
                            
                            
                            status = 200;
                            entity = "Kategorija dodata";
                       try {
                            em.getTransaction().begin();
                            em.persist(kategorija);
                            em.getTransaction().commit();
                        } finally {
                            if (em.getTransaction().isActive())
                                    em.getTransaction().rollback();
                        }
                      
                       
                       }
                   } else if (endpoint.equals("GET")) {
                       List<Kategorija> kategorije = em.createNamedQuery("Kategorija.findAll").getResultList();
                       int size = kategorije.size();
                       if (kategorije.isEmpty()) {
                                   sendEndMessage(context, null, id, 0, producer, "Nema kategorija");
                                   continue;
                               }
                       for (Kategorija v : kategorije) {
                                   sendEndMessage(context, v.toString(), id, size, producer, v.toString());
                               }
                               continue;
                       
                   }
                   break;
                   
                   
               case "Video":
                   endpoint = st.nextToken();
                   
                   if (endpoint.equals("PUT")) {
                       st = new StringTokenizer(tm.getText(), "/");
                       int idKor = Integer.parseInt(st.nextToken());
                       System.out.println(idKor);
                       Korisnik k = em.find(Korisnik.class, idKor);
                       if (k == null) {
                           status = 500;
                           entity = "Korisnik ne postoji";
                       } else {
                           
                        Date datum = Calendar.getInstance().getTime();
                        Date vrijeme = Calendar.getInstance().getTime();
                        String naziv = st.nextToken();
                        System.out.println(naziv);
                        double trajanje = Double.parseDouble(st.nextToken());
                        System.out.println(trajanje);
                        Video v = new Video();
                        v.setDatum(datum);
                        v.setVrijeme(vrijeme);
                        v.setIdKor(k);
                        v.setNaziv(naziv);
                        v.setTrajanje(trajanje);
                        try {
                            em.getTransaction().begin();
                            em.persist(v);
                            em.getTransaction().commit();
                            } finally {
                                if (em.getTransaction().isActive())
                                    em.getTransaction().rollback();
                            }
                        Main.notify3(context, producer, tm.getStringProperty("tip"), tm.getText());
                        status = 200;
                        entity = "Video je dodat";
                        }
                   } else if (endpoint.equals("DELETE")) {
                       st = new StringTokenizer(tm.getText(), "/");
                       int idVid = Integer.parseInt(st.nextToken());
                       int idKor = Integer.parseInt(st.nextToken());
                       Video v = em.find(Video.class, idVid);
                        if (v == null) {
                            status = 200;
                            entity = "Video ne postoji";
                        } else if (v.getIdKor() != null && v.getIdKor().getIdKor() != idKor) {
                            status = 200;
                            entity = "Niste vlasnik";
                        } else {
                            
                            List<Kategorija> lista = v.getKategorijaList();
                            try {
                                em.getTransaction().begin();
                                for (Kategorija kategorija : lista) {
                                    kategorija.getVideoList().remove(v);
                                }
                                em.remove(v);
                                em.getTransaction().commit();
                                Main.notify3(context, producer, tm.getStringProperty("tip"), tm.getText());
                                } finally {
                                    if (em.getTransaction().isActive())
                                        em.getTransaction().rollback();
                                } 
                            status = 200;
                            entity = "Video je obrisan";
                        
                        }

                   } else if (endpoint.equals("GET")) {
                       if (tm.getText() == null) {
                           List<Video> videi = em.createNamedQuery("Video.findAll").getResultList();
                           int size = videi.size();
                               if (videi.isEmpty())  {
                                   sendEndMessage(context, null, id, 0, producer, "Nema videa");
                                   continue;
                               }
                               
                               for (Video v : videi) {
                                   sendEndMessage(context, v.toString(), id, size, producer, v.toString());
                               }
                               continue;
                       } else {
                           
                           int idVid = Integer.parseInt(tm.getText());
                           Video v = em.find(Video.class, idVid);
                           if (v == null) {
                               Main.sendEndMessage(context, "Video ne postoji", id, 0, producer, "Video ne postoji");
                               continue;
                           } else {
                               List<Kategorija> kategorije = v.getKategorijaList();
                               
                               int size = kategorije.size();
                               if (kategorije.isEmpty()) {
                                   sendEndMessage(context, null, id, 0, producer, "Nema kategorija");
                                   continue;
                               } 
                               for (Kategorija k: kategorije) {
                                   sendEndMessage(context, k.toString(), id, size, producer, k.toString());
                               }
                               continue;
                           }
                       }
                   } else if (endpoint.equals("POST")) {
                       st = new StringTokenizer(tm.getText(), "/");
                       String izbor = st.nextToken();
                       if (izbor.equals("preimenuj")) {
                            int idVid = Integer.parseInt(st.nextToken());
                            String naziv = st.nextToken();
                            Video v = em.find(Video.class, idVid);
                            if (v == null) {
                                status = 500;
                                entity = "Zadat video ne postoji";
                            } else {
                            v.setNaziv(naziv);
                             try {
                                  em.getTransaction().begin();
                                  em.persist(v);
                                  em.getTransaction().commit();
                                  Main.notify3(context, producer, tm.getStringProperty("tip"), tm.getText());
                              } finally {
                                  if (em.getTransaction().isActive())
                                          em.getTransaction().rollback();
                              } 
                               status = 200;
                               entity = "Preimenovan video";
                            }    
                       } else if (izbor.equals("kategorija")) {
                           int idVid = Integer.parseInt(st.nextToken());
                           int idKat = Integer.parseInt(st.nextToken());
                           Video v = em.find(Video.class, idVid);
                           if (v == null) {
                               status = 500;
                               entity = "Video ne postoji";
                               } else {
                                    Kategorija k = em.find(Kategorija.class, idKat);
                                        if (k == null) {
                                            status = 500;
                                            entity = "Kategorija ne postoji";
                                        } else {
                                            entity = null;
                                            List<Kategorija> kategorije = v.getKategorijaList();
                                            for (Kategorija kat : kategorije) {
                                                if (kat.equals(k)) {
                                                    status = 200;
                                                    entity = "Film vec pripada kategoriji";
                                                    break;
                                                    }
                                                }
                                            if (entity == null) {
                                                List<Video> lista = k.getVideoList();
                                                lista.add(v);
                                                v.getKategorijaList().add(k);
                                                k.setVideoList(lista); 
                                                try {
                                                      em.getTransaction().begin();
                                                      em.persist(k);
                                                      em.getTransaction().commit();
                                                  } finally {
                                                      if (em.getTransaction().isActive())
                                                              em.getTransaction().rollback();
                                                  } 

                                                status = 200;
                                                entity = "Dodata kategorija";
                                            }
                                        }
                           }
                       }
                       
                   }
                   break;
                   
                   
                   
           }
        Main.sendAnswerToSubsystem(context, q2, entity, status, id, producer);
       
       }
    }

    private static void fulfillRequest(String request, String property, EntityManager em) {
         if (property.equals("PUT")) {
             StringTokenizer st = new StringTokenizer(request, "/");
             String ime = st.nextToken();
             String mejl = st.nextToken();
             int godiste = Integer.parseInt(st.nextToken());
             String pol = st.nextToken();
             
             Korisnik k = new Korisnik();
             k.setGodiste(godiste);
             k.setIme(ime);
             k.setMejl(mejl);
             k.setPol(pol);
             
             try {
                em.getTransaction().begin();
                em.persist(k);
                em.getTransaction().commit();
                } finally {
                if (em.getTransaction().isActive())
                        em.getTransaction().rollback();
                } 
             
             System.out.println("Korisnik je dodat");
             
         } else if (property.equals("POST")) {
             StringTokenizer st = new StringTokenizer(request, "/");
             st.nextToken();
             int idKor = Integer.parseInt(st.nextToken());
             String mejl = st.nextToken();
             Korisnik k = em.find(Korisnik.class, idKor);
             k.setMejl(mejl);
             
            try {
              em.getTransaction().begin();
              em.persist(k);
              em.getTransaction().commit();
              } finally {
              if (em.getTransaction().isActive())
                      em.getTransaction().rollback();
              }         
             
         }
         
    }
    
}
