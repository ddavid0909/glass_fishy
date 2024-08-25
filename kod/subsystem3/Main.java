/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package subsystem3;

import entities.Gledanje;
import entities.Korisnik;
import entities.Ocena;
import entities.OcenaPK;
import entities.Paket;
import entities.Pretplata;
import entities.Video;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
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

/**
 *

 */
public class Main {
    @Resource(lookup="jms/__defaultConnectionFactory")
    private static ConnectionFactory cf;
    @Resource(lookup="QSPS3")
    private static Queue q1;
    @Resource(lookup="QPS3S")
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
    
    
    
    
    public static void main(String[] args) throws JMSException {
       EntityManagerFactory emf = Persistence.createEntityManagerFactory("Subsystem3PU");
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
       
       JMSConsumer listener = context.createConsumer(q3, "id = " + 13);
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
               tm.setIntProperty("id", 31);
               producer.send(q3, tm);
               
           } catch (JMSException ex) {}
           
       
       });
       
       JMSConsumer listener2 = context.createConsumer(q3, "id = " + 23);
       listener2.setMessageListener((Message msg)-> {
           TextMessage tm = (TextMessage)msg;
           try {
               // iz teksta poruke se vidi zahtjev, a iz property-ja se vidi tip.
               String odg = tm.getText();
               String property = tm.getStringProperty("tip");
               StringTokenizer st = new StringTokenizer(property, "/");
               st.nextToken();
               property = st.nextToken();
               System.out.println(property);
               fulfillRequest2(odg, property, em);
               
               tm = context.createTextMessage(null);
               tm.setIntProperty("id", 32);
               producer.send(q3, tm);
               
           } catch (JMSException ex) {}
           
       
       });
       
       
       
      
       while(true) {
           TextMessage tm = (TextMessage)consumer.receive();
           System.out.println(tm.getText());
           String property = tm.getStringProperty("tip");
           System.out.println(property);
           int id = tm.getIntProperty("id");
           StringTokenizer st = new StringTokenizer(property, "/");
           String endpoint = st.nextToken();
           
           switch(endpoint) {
               
                case "Paket" :
                   endpoint = st.nextToken();
                   System.out.println(endpoint);
                   if (endpoint.equals("PUT")) {
                       st = new StringTokenizer(tm.getText(), "/");
                       endpoint = st.nextToken();
                       // "napravi/{cijena}"
                       if (endpoint.equals("napravi")) {
                           float cijena = Float.parseFloat(st.nextToken());
                           Paket paket = new Paket();
                           paket.setCijena(cijena);
                           
                           try {
                            em.getTransaction().begin();
                            em.persist(paket);
                            em.getTransaction().commit();
                            } finally {
                                if (em.getTransaction().isActive())
                                    em.getTransaction().rollback();
                            }
                           Main.sendAnswerToSubsystem(context, q2, "Paket je napravljen", 200, id, producer);
                           continue;
                           
                        }
                       if (endpoint.equals("pretplata")) {
                           int idKor = Integer.parseInt(st.nextToken());
                           int idPak = Integer.parseInt(st.nextToken());
                           System.out.println(idKor + " " + idPak);
                           Korisnik k = em.find(Korisnik.class, idKor);
                           if (k == null) {
                               Main.sendAnswerToSubsystem(context, q2, "Pokusavate da dodate pretplatu za korisnika koji ne postoji", 400, id, producer);
                               continue; 
                           }
                           Paket p = em.find(Paket.class, idPak);
                           if (p == null) {
                              Main.sendAnswerToSubsystem(context, q2, "Pokusavate da se pretplatite na nepostojeci paket", 400, id, producer);
                              continue;
                            }
                            System.out.println("Dosli doovde");
                            List<Pretplata> pretplate = em.createQuery("SELECT p FROM Pretplata p WHERE p.idKor = :id").setParameter("id", k).getResultList();
                            boolean update = true;
                            Date provjera = null;
                            Date datum = Calendar.getInstance().getTime();
                            Date vrijeme = Calendar.getInstance().getTime();
                            for (Pretplata pret: pretplate) {
                                provjera = pret.getDatum();
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(provjera);
                                calendar.add(Calendar.DATE, 30);
                                provjera = calendar.getTime();
                                if (provjera.after(datum)) {
                                    update = false;
                                    break;
                                }
                            }
                            if (!update) {
                              Main.sendAnswerToSubsystem(context, q2, "Trenutna pretplata jos vazi", 400, id, producer);
                              continue;
                            }
                            Pretplata pret = new Pretplata();
                            pret.setCijena(p.getCijena());
                            pret.setDatum(datum);
                            pret.setVrijeme(vrijeme);
                            pret.setIdKor(k);
                            pret.setIdPak(p);
                            try {
                            em.getTransaction().begin();
                            em.persist(pret);
                            em.getTransaction().commit();
                            } finally {
                                if (em.getTransaction().isActive())
                                    em.getTransaction().rollback();
                            }
                            Main.sendAnswerToSubsystem(context, q2, "Pretplata je napravljena", 200, id, producer);
                            continue;
                            
                           
                       } 
                       
                       
                    }
                    if (endpoint.equals("POST")) {
                            st = new StringTokenizer(tm.getText(), "/");
                            endpoint = st.nextToken();
                            if (endpoint.equals("promijeni")) {
                                   // "promijeni/{idPak}/{novaCijena}"
                                    int idPak = Integer.parseInt(st.nextToken());
                                    float novaCijena = Float.parseFloat(st.nextToken());

                                    Paket p = em.find(Paket.class, idPak);
                                    if (p == null) {
                                       Main.sendAnswerToSubsystem(context, q2, "Pokusavate da promijenite cijenu nepostojeceg paketa", 400, id, producer);
                                       continue;
                                    }
                                    if (p.getCijena() != novaCijena){

                                       p.setCijena(novaCijena);
                                       try {
                                        em.getTransaction().begin();
                                        em.persist(p);
                                        em.getTransaction().commit();
                                        } finally {
                                            if (em.getTransaction().isActive())
                                                em.getTransaction().rollback();
                                        }


                                    }
                                    Main.sendAnswerToSubsystem(context, q2, "Cijena uspjesno azurirana", 200, id, producer);
                                    continue;

                               }       
                            
                    }
                       
                   
                   if (endpoint.equals("GET")) {
                       endpoint = tm.getText();
                       if (endpoint == null) {
                            List<Paket> kategorije = em.createNamedQuery("Paket.findAll").getResultList();
                            System.out.println("Paketi dohvaceni");
                            int size = kategorije.size();
                            if (kategorije.isEmpty()) {
                                   sendEndMessage(context, null, id, 0, producer, "Nema paketa");
                                   continue;
                               }
                            for (Paket v : kategorije) {
                                   sendEndMessage(context, v.toString(), id, size, producer, v.toString());
                               }
                           
                           continue;
                       }
                       else  {
                           int idKor = Integer.parseInt(endpoint);
                           Korisnik k = em.find(Korisnik.class, idKor);
                           if (k == null) {
                               sendEndMessage(context, null, id, 0, producer, "Korisnik ne postoji");
                               continue; 
                           }
                           List<Pretplata> pretplate = em.createQuery("SELECT p FROM Pretplata p WHERE p.idKor = :id ORDER BY p.datum").setParameter("id", k).getResultList();
                           int size = pretplate.size();
                            if (pretplate.isEmpty()) {
                                   sendEndMessage(context, null, id, 0, producer, "Nema paketa");
                                   continue;
                               }
                            for (Pretplata v : pretplate) {
                                   sendEndMessage(context, v.toString(), id, size, producer, v.toString());
                               }
                           
                           continue;
                       }
                   }
                   
                break;
                
                case "Ocena":
                    endpoint = st.nextToken();
                    if (endpoint.equals("PUT")) {
                        st = new StringTokenizer(tm.getText(), "/");
                        endpoint = st.nextToken();
                        if (endpoint.equals("napravi")) {
                            int idKor = Integer.parseInt(st.nextToken());
                            int idVid = Integer.parseInt(st.nextToken());
                            int ocena = Integer.parseInt(st.nextToken());
                            Korisnik k = em.find(Korisnik.class, idKor);
                            if (k == null) {
                                Main.sendAnswerToSubsystem(context, q2, "Korisnik ne postoji", 400, id, producer);
                                continue;
                            }
                            Video v = em.find(Video.class, idVid);
                            if (v == null) {
                                Main.sendAnswerToSubsystem(context, q2, "Video ne postoji", 400, id, producer);
                                continue;
                            }
                            List<Ocena> ocene = em.createQuery("SELECT o FROM Ocena o WHERE o.korisnik = :k AND o.video = :v")
                                    .setParameter("k", k).setParameter("v", v).getResultList();
                            if (!ocene.isEmpty()) {
                                Main.sendAnswerToSubsystem(context, q2, "Vec ste ocijenili ovaj video. Mozete promijeniti ocjenu, ali ne dodati novu", 400, id, producer);
                                continue;
                            }
                            Ocena o = new Ocena();
                            o.setKorisnik(k);
                            o.setVideo(v);
                            o.setOcena(ocena);
                            o.setDatum(Calendar.getInstance().getTime());
                            o.setVrijeme(Calendar.getInstance().getTime());
                            o.setOcenaPK(new OcenaPK(idKor, idVid));
                            
                            ocene = v.getOcenaList();
                            ocene.add(o);
                            v.setOcenaList(ocene);
                            
                            try {
                                        em.getTransaction().begin();
                                        em.persist(o);
                                        em.getTransaction().commit();
                                        } finally {
                                            if (em.getTransaction().isActive())
                                                em.getTransaction().rollback();
                                        }
                            Main.sendAnswerToSubsystem(context, q2, "Ocena je dodata", 200, id, producer);
                            continue;
                        }
                        
                    } else if (endpoint.equals("POST")) {
                        st = new StringTokenizer(tm.getText(), "/");
                        endpoint = st.nextToken();
                        if (endpoint.equals("promijeni")) {
                            int idKor = Integer.parseInt(st.nextToken());
                            int idVid = Integer.parseInt(st.nextToken());
                            int ocena = Integer.parseInt(st.nextToken());
                            Korisnik k = em.find(Korisnik.class, idKor);
                            if (k == null) {
                                Main.sendAnswerToSubsystem(context, q2, "Korisnik ne postoji", 400, id, producer);
                                continue;
                            }
                            Video v = em.find(Video.class, idVid);
                            if (v == null) {
                                Main.sendAnswerToSubsystem(context, q2, "Video ne postoji", 400, id, producer);
                                continue;
                            }
                            Ocena o = em.find(Ocena.class, new OcenaPK(idKor, idVid));
                            if (o == null) {
                                Main.sendAnswerToSubsystem(context, q2, "Prvo dodati ocenu", 400, id, producer);
                                continue;
                            }
                            o.setOcena(ocena);
                            o.setDatum(Calendar.getInstance().getTime());
                            o.setVrijeme(Calendar.getInstance().getTime());
                            try {
                                        em.getTransaction().begin();
                                        em.persist(o);
                                        em.getTransaction().commit();
                                        } finally {
                                            if (em.getTransaction().isActive())
                                                em.getTransaction().rollback();
                                        }
                            Main.sendAnswerToSubsystem(context, q2, "Ocena je promijenjena", 200, id, producer);
                            continue;
                          
                    }
                    
                    
                    } else if (endpoint.equals("GET")) {
                        st = new StringTokenizer(tm.getText(), "/");
                        endpoint = st.nextToken();
                        if (endpoint.equals("dohvati")) {
                            int idVid = Integer.parseInt(st.nextToken());
                            Video v = em.find(Video.class, idVid);
                            if (v == null) {
                                Main.sendAnswerToSubsystem(context, q2, "Video ne postoji", 400, id, producer);
                                continue;
                            }
                            List<Ocena> ocene = em.createQuery("SELECT o FROM Ocena o WHERE o.video = :vid").setParameter("vid", v).getResultList();
                            int size = ocene.size();
                            if (ocene.isEmpty()) {
                                   sendEndMessage(context, null, id, 0, producer, "Nema videa");
                                   continue;
                               }
                            for (Ocena o : ocene) {
                                        sendEndMessage(context, o.toString(), id, size, producer, o.toString());
                               }
                               continue;
                            
                            
                        }
                    } else if (endpoint.equals("DELETE")) {
                        st = new StringTokenizer(tm.getText(), "/");
                        int idKor = Integer.parseInt(st.nextToken());
                        int idVid = Integer.parseInt(st.nextToken());
                        Ocena o = em.find(Ocena.class, new OcenaPK(idKor, idVid));
                        if (o == null) {
                                Main.sendAnswerToSubsystem(context, q2, "Ocjena ne postoji", 400, id, producer);
                                continue;
                            }
                        try {
                            em.getTransaction().begin();
                            em.remove(o);
                            em.getTransaction().commit();
                            } finally {
                                if (em.getTransaction().isActive())
                                    em.getTransaction().rollback();
                            }
                            Main.sendAnswerToSubsystem(context, q2, "Ocjena je obrisana", 200, id, producer);
                            continue;                        
                            }   
                break;
                   
                case "Gledanje":
                    endpoint = st.nextToken();
                    if (endpoint.equals("GET")) {
                        int idVid = Integer.parseInt(tm.getText());

                        Video v = em.find(Video.class, idVid);
                        if (v == null) {
                            Main.sendAnswerToSubsystem(context, q2, "Video ne postoji", 400, id, producer);
                            continue;
                        }
                        List<Gledanje> gledanja = em.createQuery("SELECT o FROM Gledanje o WHERE o.idVid = :vid").setParameter("vid", v).getResultList();
                        int size = gledanja.size();
                        if (gledanja.isEmpty()) {
                               sendEndMessage(context, null, id, 0, producer, "Nema videa");
                               continue;
                           }
                        for (Gledanje o : gledanja) {
                                    sendEndMessage(context, o.toString(), id, size, producer, o.toString());
                           }
                           continue;

                    } else if (endpoint.equals("PUT")) {
                        st = new StringTokenizer(tm.getText(), "/");
                        int idKor = Integer.parseInt(st.nextToken());
                        int idVid = Integer.parseInt(st.nextToken());
                        int pocetak = Integer.parseInt(st.nextToken());
                        int trajanje = Integer.parseInt(st.nextToken());
                        Korisnik k = em.find(Korisnik.class, idKor);
                        if (k == null) {
                            Main.sendAnswerToSubsystem(context, q2, "Korisnik ne postoji", 400, id, producer);
                            continue;
                        }
                        Video v = em.find(Video.class, idVid);
                        if (v == null) {
                            Main.sendAnswerToSubsystem(context, q2, "Video ne postoji", 400, id, producer);
                            continue;
                        }
                        if (v.getTrajanje() < pocetak + trajanje) {
                            Main.sendAnswerToSubsystem(context, q2, "Snimak nije dovoljno veliki", 400, id, producer);
                            continue;
                        }
                        Gledanje gledanje = new Gledanje();
                        
                        gledanje.setDatum(Calendar.getInstance().getTime());
                        gledanje.setVrijeme(Calendar.getInstance().getTime());
                        gledanje.setIdKor(k);
                        gledanje.setIdVid(v);
                        gledanje.setTrajanje(trajanje);
                        gledanje.setPocetak(pocetak);
                        
                        List<Gledanje> gledanja = v.getGledanjeList();
                        gledanja.add(gledanje);
                        v.setGledanjeList(gledanja);
                        
                        try {
                            em.getTransaction().begin();
                            em.persist(gledanje);
                            em.getTransaction().commit();
                            } finally {
                                if (em.getTransaction().isActive())
                                    em.getTransaction().rollback();
                            }
                            Main.sendAnswerToSubsystem(context, q2, "Gledanje je dodato", 200, id, producer);
                            continue;   
                        
                        
                    }
                break;
           }
           }
           
       }

    private static void fulfillRequest2(String odg, String property, EntityManager em) {
        StringTokenizer st; /*= new StringTokenizer(property, "/");
        
        property = st.nextToken();*/
        System.out.println(property);
        
        if (property.equals("DELETE")) {
            st = new StringTokenizer(odg, "/");
            int idVid = Integer.parseInt(st.nextToken());
            Video v = em.find(Video.class, idVid);
            
            List<Ocena> ocene = v.getOcenaList();
            List<Gledanje> gledanja = v.getGledanjeList();
            
            try {
                em.getTransaction().begin();
                for (Ocena o: ocene) {
                    em.remove(o);
                }
                for (Gledanje g: gledanja) {
                    em.remove(g);
                }
                em.getTransaction().commit();
                } finally {
                    if (em.getTransaction().isActive())
                        em.getTransaction().rollback();
                }
            //em.flush();
            try {
                em.getTransaction().begin();
                em.remove(v);
                em.getTransaction().commit();
           } finally {
                if (em.getTransaction().isActive())
                        em.getTransaction().rollback();
            }
            
            
        } else if (property.equals("POST")) {
            st = new StringTokenizer(odg, "/");
            st.nextToken();
            
            int idVid = Integer.parseInt(st.nextToken());
            String naziv = st.nextToken();
            
            Video v = em.find(Video.class, idVid);
            v.setNaziv(naziv);
            try {
                 em.getTransaction().begin();
                 em.persist(v);
                 em.getTransaction().commit();
             } finally {
                 if (em.getTransaction().isActive())
                         em.getTransaction().rollback();
             }
            
            
            
        } else if (property.equals("PUT")) {
            System.out.println(odg);
            st = new StringTokenizer(odg, "/");
            
            int idKor = Integer.parseInt(st.nextToken());
            String naziv = st.nextToken();
            double trajanje = Double.parseDouble(st.nextToken());
            
            
            Korisnik k = em.find(Korisnik.class, idKor);
            Video v = new Video();
            
            Date datum = Calendar.getInstance().getTime();
            Date vrijeme = Calendar.getInstance().getTime();
      
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
            
            
        }
        
    }
    }
    

