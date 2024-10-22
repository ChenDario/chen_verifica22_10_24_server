package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Random;

public class MioThread extends Thread {
    private Socket socketClient;


    public MioThread(Socket s){
        this.socketClient = s;
    }

    public void run(){

        try {
            boolean newGame = true;
            do {
                BufferedReader in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
                DataOutputStream out = new DataOutputStream(socketClient.getOutputStream());
                //Numero random
                Random random = new Random();
                int numeroRandom = random.nextInt(100);

                //Stringa/numero da indovinare in ricevuta
                String stringRicevuta;
                int numRicevuto;
                //Messaggio da mandare al client
                String messaggioClient = "";
                //Cont tentativi
                int cont = 0;

                System.out.println("\n Numero da indovinare: " + numeroRandom);
                do {
                    cont++;
                    stringRicevuta = in.readLine(); //Leggere il numero dal client
                    System.out.println("La stringa ricevuta: " + stringRicevuta); //Leggiamo il messaggio del client
                    
                    numRicevuto = Integer.valueOf(stringRicevuta);

                    if(numRicevuto < numeroRandom)
                        out.writeBytes("<" + "\n"); //Se il numero è minore
                    if(numRicevuto > numeroRandom)
                        out.writeBytes(">" + "\n"); //Se il numero è maggiore
                    if(numRicevuto == numeroRandom){
                        out.writeBytes("=" + "\n"); //Numero Indovinato
                        out.writeBytes( cont + "" + "\n"); 

                    }
                } while (numRicevuto != numeroRandom);

                //Messaggio dal client per verificare se si vuole continuare a giocare o meno
                messaggioClient = in.readLine();
                if(messaggioClient.equalsIgnoreCase("yes") || messaggioClient.equalsIgnoreCase("new game"))
                    newGame = true;
                if(messaggioClient.equalsIgnoreCase("no"))
                    newGame = false;
            } while (newGame);

            this.socketClient.close();

        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        System.out.println("\n Collegamento interrotto");
    }
}
