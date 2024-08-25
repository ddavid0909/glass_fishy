/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package klaplikacija;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
/**
 *
 * @author fafulja
 */
public class KlAplikacija {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        while (true) {
            System.out.println("Unesite broj zahtjeva:");
            System.out.println("1. Kreiranje grada\n" +
                               "2. Kreiranje korisnika\n" +
                               "3. Promena email adrese za korisnika\n" +
                               "4. Promena mesta za korisnika\n" +
                               "5. Kreiranje kategorije\n" +
                               "6. Kreiranje video snimka\n" +
                               "7. Promena naziva video snimka\n" +
                               "8. Dodavanje kategorije video snimku\n" +
                               "9. Kreiranje paketa\n" +
                               "10. Promena mesečne cene za paket\n" +
                               "11. Kreiranje pretplate korisnika na paket\n" +
                               "12. Kreiranje gledanja video snimka od strane korisnika\n" +
                               "13. Kreiranje ocene korisnika za video snimak\n" +
                               "14. Menjanje ocene korisnika za video snimak\n" +
                               "15. Brisanje ocene korisnika za video snimak\n" +
                               "16. Brisanje video snimka od strane korisnika koji ga je kreirao\n" +
                               "17. Dohvatanje svih mesta\n" +
                               "18. Dohvatanje svih korisnika\n" +
                               "19. Dohvatanje svih kategorija\n" +
                               "20. Dohvatanje svih video snimaka\n" +
                               "21. Dohvatanje kategorija za određeni video snimak\n" +
                               "22. Dohvatanje svih paketa\n" +
                               "23. Dohvatanje svih pretplata za korisnika\n" + 
                               "24. Dohvatanje svih gledanja za video snimak\n" + 
                               "25. Dohvatanje svih ocena za video snimak"
                    );
                    
            Scanner scanner = new Scanner(System.in);   
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice > 25 || choice < 1) {
                    System.out.println("Nepravilan parametar");
                    return;
            }
            
            
            String method = null;
            String request = null;
            String ime;
            String mejl;
            int godiste;
            String pol;
            int idMes;
            double trajanje;
            int tr;
            int pocetak;
            int ocena;
            int idKor;
            int idVid;
            int idKat;
            int idPak;
            float cijena;
            
            switch(choice) {
                case 1:
                    System.out.println("Unesite naziv mjesta");
                    ime = scanner.nextLine();
                    request = "http://localhost:8080/Server/resources/mesto/napravi/" + ime;
                    
                    method = "PUT";
                    
                    break;
                case 2:
                    // {ime}/{mejl}/{godiste}/{pol}/{idMes}
                    System.out.println("Ime korisnika: ");
                    ime = scanner.nextLine();
                    System.out.println("Mejl korisnika: ");
                    mejl = scanner.nextLine();
                    System.out.println("Godiste korisnika: ");
                    godiste = Integer.parseInt(scanner.nextLine());
                    System.out.println("Pol korisnika: ");
                    pol = scanner.nextLine();
                    System.out.println("idMes korisnika: ");
                    idMes = Integer.parseInt(scanner.nextLine());
                    method = "PUT";
                    request = "http://localhost:8080/Server/resources/korisnik/napravi/" + ime + "/" + mejl + "/" + godiste + "/" + pol + "/" + idMes;
                    
                    break;
                case 3:
                    
                    System.out.println("Id korisnika: ");
                    idKor = Integer.parseInt(scanner.nextLine());
                    System.out.println("Novi mejl korisnika: ");
                    mejl = scanner.nextLine();
                    // ("mail/{idKor}/{mejl}")
                    method = "POST";
                    request = "http://localhost:8080/Server/resources/korisnik/mail/" + idKor + "/"  + mejl;
                    
                    break;
                case 4:
                    
                    System.out.println("Id korisnika: ");
                    idKor = Integer.parseInt(scanner.nextLine());
                    System.out.println("Novi grad korisnika: ");
                    ime = scanner.nextLine();
                    // ("mail/{idKor}/{mejl}")
                    method = "POST";
                    request = "http://localhost:8080/Server/resources/korisnik/city/" + idKor + "/"  + ime;
                    
                    
                    break;
                case 5:
                    // kategorija/napravi/{naziv}")
                    
                    System.out.println("Naziv kategorije: ");
                    ime = scanner.nextLine();
                    method = "PUT";
                    request = "http://localhost:8080/Server/resources/kategorija/napravi/" + ime;
                    
                    break;
                case 6:
                    
                    // napravi/{naziv}/{trajanje}/{idKor}
                    System.out.println("Naziv videa: ");
                    ime = scanner.nextLine();
                    
                    System.out.println("Trajanje videa: ");
                    trajanje = Double.parseDouble(scanner.nextLine());
                    
                    System.out.println("Id korisnika: ");
                    idKor = Integer.parseInt(scanner.nextLine());
                    
                    method = "PUT";
                    request = "http://localhost:8080/Server/resources/video/napravi/" + ime + "/" + trajanje + "/" + idKor;
                    
                          
                    
                    break;
                case 7:
                    
                    // preimenuj/{idVid}/{naziv}
                    System.out.println("Id videa koji se mijenja: ");
                    idVid = Integer.parseInt(scanner.nextLine());
                    
                    System.out.println("Naziv videa: ");
                    ime = scanner.nextLine();
                    
                    method = "POST";
                    request = "http://localhost:8080/Server/resources/video/preimenuj/" + idVid + "/" + ime;
                    
                    break;
                case 8:
                    // "kategorija/{idVid}/{idKat}"
                    System.out.println("id Videa: ");
                    idVid = Integer.parseInt(scanner.nextLine());
                    
                    System.out.println("Id kategorije: ");
                    idKat = Integer.parseInt(scanner.nextLine());
                    
                    method = "POST";
                    request = "http://localhost:8080/Server/resources/video/kategorija/" + idVid + "/" + idKat;
                    
                    
                    break;
                case 9:
                    
                    System.out.println("Cijena paketa: ");
                    cijena = Float.parseFloat(scanner.nextLine());
                    
                    method = "PUT";
                    request = "http://localhost:8080/Server/resources/paket/napravi/" + cijena;
                    
                    break;
                case 10:
                    
                    System.out.println("IdPaketa kome se mijenja cijena: ");
                    idPak = Integer.parseInt(scanner.nextLine());
                    
                    System.out.println("Nova cijena paketa: ");
                    cijena = Float.parseFloat(scanner.nextLine());
                    
                    method = "POST";
                    request = "http://localhost:8080/Server/resources/paket/promijeni/" + idPak + "/" + cijena;
                    
                    break;
                case 11:
                    
                    //pretplata/{idKor}/{idPak}
                    
                    System.out.println("IdKorisnika: ");
                    idKor = Integer.parseInt(scanner.nextLine());
                    
                    System.out.println("IdPaketa: ");
                    idPak = Integer.parseInt(scanner.nextLine());
                    
                    method = "PUT";
                    request = "http://localhost:8080/Server/resources/paket/pretplata/" + idKor + "/" + idPak;
                    
                    break;
                case 12:
                    // napravi/{idKor}/{idVid}/{pocetak}/{trajanje}
                    System.out.println("IdKorisnika: ");
                    idKor = Integer.parseInt(scanner.nextLine());
                    
                    System.out.println("id Videa: ");
                    idVid = Integer.parseInt(scanner.nextLine());
                    
                    System.out.println("Pocetak: ");
                    pocetak = Integer.parseInt(scanner.nextLine());
                    
                    System.out.println("Trajanje: ");
                    tr = Integer.parseInt(scanner.nextLine());
                    
                    method = "PUT";
                    request = "http://localhost:8080/Server/resources/gledanje/napravi/" + idKor + "/" + idVid + "/" + pocetak + "/" + tr;
                           
                    break;
                case 13:
                    
                    // "napravi/{idKor}/{idVid}/{ocena}"
                    System.out.println("IdKorisnika: ");
                    idKor = Integer.parseInt(scanner.nextLine());
                    
                    System.out.println("id Videa: ");
                    idVid = Integer.parseInt(scanner.nextLine());
                    
                    System.out.println("Ocena: ");
                    ocena = Integer.parseInt(scanner.nextLine());
                    
                    method = "PUT";
                    request = "http://localhost:8080/Server/resources/ocena/napravi/" + idKor + "/" + idVid + "/" + ocena;
                    
                    break;
                case 14:
                    // promijeni/{idKor}/{idVid}/{ocena}
                    System.out.println("IdKorisnika: ");
                    idKor = Integer.parseInt(scanner.nextLine());
                    
                    System.out.println("id Videa: ");
                    idVid = Integer.parseInt(scanner.nextLine());
                    
                    System.out.println("Ocena: ");
                    ocena = Integer.parseInt(scanner.nextLine());
                    
                    method = "POST";
                    request = "http://localhost:8080/Server/resources/ocena/promijeni/" + idKor + "/" + idVid + "/" + ocena;

                    break;
                case 15:
                    //obrisi/{idKor}/{idVid}
                    System.out.println("IdKorisnika: ");
                    idKor = Integer.parseInt(scanner.nextLine());
                    
                    System.out.println("id Videa: ");
                    idVid = Integer.parseInt(scanner.nextLine());
                    
                    method = "DELETE";
                    request = "http://localhost:8080/Server/resources/ocena/obrisi/" + idKor + "/" + idVid;
                    
                    break;
                case 16:
                    // Brisanje video snimka od strane korisnika koji ga je kreirao\n
                    System.out.println("id Videa: ");
                    idVid = Integer.parseInt(scanner.nextLine());
                    
                    System.out.println("id Korisnika: ");
                    idKor = Integer.parseInt(scanner.nextLine());
                            
                    
                    method = "DELETE";
                    request = "http://localhost:8080/Server/resources/video/" + idVid + "/" + idKor;

                    break;
                case 17:
                    
                    method = "GET";
                    request = "http://localhost:8080/Server/resources/mesto";
                    
                    break;
                case 18:
                    
                    method = "GET";
                    request = "http://localhost:8080/Server/resources/korisnik";
                    
                    break;
                case 19:
                    
                    method = "GET";
                    request = "http://localhost:8080/Server/resources/kategorija";
                    
                    break;
                case 20:
                    
                    method = "GET";
                    request = "http://localhost:8080/Server/resources/video";
                    
                    break;
                case 21:
                    
                    System.out.println("id Videa: ");
                    idVid = Integer.parseInt(scanner.nextLine());
                    method = "GET";
                    request = "http://localhost:8080/Server/resources/video/" + idVid;
                    
                    break;
                case 22:
                    
                    method = "GET";
                    request = "http://localhost:8080/Server/resources/paket";
                    
                    break;
                case 23:
                    System.out.println("IdKorisnika: ");
                    idKor = Integer.parseInt(scanner.nextLine());
                    // pretplate/{idKor}
                    method = "GET";
                    request = "http://localhost:8080/Server/resources/paket/pretplate/" + idKor;
                    
                    break;
                case 24:
                    
                    // dohvati/{idVid}"
                    System.out.println("id Videa: ");
                    idVid = Integer.parseInt(scanner.nextLine());
                    
                    method = "GET";
                    request = "http://localhost:8080/Server/resources/gledanje/dohvati/" + idVid;
                    
                    
                    break;
                case 25:
                    // dohvati/{idVid}
                    System.out.println("id Videa: ");
                    idVid = Integer.parseInt(scanner.nextLine());
                    
                    method = "GET";
                    request = "http://localhost:8080/Server/resources/ocena/dohvati/" + idVid;
                    
                    break;
                
                
            
            }
           // System.out.println(request);
            // KOD PREUZET SA INTERNETA.
           try {
            // Specify the URL
            URL url = new URL(request);

            // Open a connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to DELETE
            connection.setRequestMethod(method);

            // Get the response code
           // int responseCode = connection.getResponseCode();
           // System.out.println("Response Code: " + responseCode);

            // You can read the response if needed
            // InputStream inputStream = connection.getInputStream();
            // ...
             try (InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line).append('\n');
                }

                System.out.println(response.toString());
            } catch (IOException e) {
               // e.printStackTrace();
            }


            // Close the connection
            connection.disconnect();
        } catch (MalformedURLException e) {
           // e.printStackTrace();
        } catch (IOException e) {
           // e.printStackTrace();
        }
           
            
        }
    }
    
}
