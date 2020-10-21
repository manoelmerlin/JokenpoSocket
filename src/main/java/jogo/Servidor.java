package jogo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {  
    private static int countClientes = 0;
    public static void main(String[] args) {
        ServerSocket servidor = null;
        
        try {
            servidor = new ServerSocket(9999);
            System.out.println("servidor startado na porta 9999");
            while(true){
                Socket cliente = servidor.accept();
                countClientes++;
                new GerenciadorDeClientes(cliente);
            }
        } catch (IOException e) {
            try {
                if(servidor != null)
                    servidor.close();
            } catch (IOException e1) {}

            System.err.println("a porta está ocupada ou servidor foi fechado");
            e.printStackTrace();
        }
    }
}
