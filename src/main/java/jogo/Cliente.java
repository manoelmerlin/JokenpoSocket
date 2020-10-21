/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author manoel
 */
public class Cliente {
    public static void main(String[] args) {
        try {
            final Socket cliente = new Socket("localhost", 9999);
            
            new Thread(){
                @Override
                public void run() {
                    try {
                        BufferedReader leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                        while (true) {
                            String msg = leitor.readLine();
                            System.out.println("O servidor disse " + msg);
                        }
                    } catch (IOException ex) {
                        System.out.println("Erro ao ler mensagem");
                        ex.printStackTrace();
                    }
                }
            }.start();
            
            PrintWriter entrada = new PrintWriter(cliente.getOutputStream(), true);
            BufferedReader leitorCliente = new BufferedReader(new InputStreamReader(System.in));
            String msg;
            while (true) {
                msg = leitorCliente.readLine();
                entrada.println(msg);
                if (msg.equals("3")) {
                    System.out.println("Desconectado com sucesso");
                    System.exit(0);
                    break;
                }
            }  
        } catch (IOException ex) {
            System.out.println("Erro ao conectar no servidor");
        }
    }
  
}
