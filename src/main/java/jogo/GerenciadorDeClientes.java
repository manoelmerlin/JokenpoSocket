package jogo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class GerenciadorDeClientes extends Thread {
    
    private Socket cliente;
    private String nomeJogador;
    private String modoJogo;
    private String jogada;
    private String vencedor;
    private Jokenpo jokenpo;
    private String jogadaCPU;
    private static final Map<String, GerenciadorDeClientes> clientes = new HashMap<String, GerenciadorDeClientes>();
    private BufferedReader leitor;
    private PrintWriter entrada;
    private static int countPlayers;
    
    public GerenciadorDeClientes(Socket cliente) {
        this.cliente = cliente;
        start();
    }
    
    @Override
    public void run(){
        try {
            leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            entrada = new PrintWriter(cliente.getOutputStream(), true);
            jokenpo = new Jokenpo();
            entrada.println("Por favor escreva seu nome");
            String msg = leitor.readLine();
            this.nomeJogador = msg;
            countPlayers++;
            System.out.println("Bem vindo ao jokenpô " + this.nomeJogador);
            clientes.put("player" + countPlayers , this);
            while(true) {                
                if (msg.equals("3")) {
                    break;
                }
                
                if (this.modoJogo == null) {
                    entrada.println(this.nomeJogador + " Qual modo você deseja jogar 1 - Player x CPU OU 2 - Player x Player?");
                    msg = leitor.readLine();
                }
                
                if (msg.equals("1")) {
                    this.modoJogo = msg;
                } else {
                    this.modoJogo = "2";
                }

                if (this.modoJogo.equals("1")) {         
                    entrada.println(this.nomeJogador + " Você está jogando contra a máquina ! faça sua jogada Pedra, Papel ou Tesoura");
                    msg = leitor.readLine();
                    this.jogada = msg;
                    if (jokenpo.validarJogada(this.jogada)) {
                        this.jogadaCPU = jokenpo.retornarJogadaCpu();
                        Thread.sleep(2000);
                        entrada.println("Você jogou " + this.jogada + " E a máquina jogou " + this.jogadaCPU);
                        Thread.sleep(2000);
                        this.vencedor = jokenpo.retornarVencedor(this.nomeJogador, "CPU", this.jogada, this.jogadaCPU);
                        Thread.sleep(2000);
                        
                        if (this.vencedor.equals("empate")) {
                            entrada.println("O jogo empatou");
                        } else if (this.vencedor.equals(this.nomeJogador)) {
                            entrada.println("Parabéns " + this.vencedor + " você foi o vencedor");
                        } else {
                            entrada.println("Que pena " + this.nomeJogador + " você é muito ruim e perdeu para a máquina");
                        }
                        
                        Thread.sleep(2000);
                        entrada.println("1 - Jogar novamente Ou 3 - Sair");
                        msg = leitor.readLine();
                    }
                } else if (this.modoJogo.equals("2")) {
                    GerenciadorDeClientes playerOne = clientes.get("player1");
                    GerenciadorDeClientes playerTwo = clientes.get("player2");

                    if (playerTwo == null) {
                        entrada.println("Aguarde pelo player 2");
                        msg = leitor.readLine();
                    } else {
                        if (this.jogada == null) {
                            entrada.println("Faça sua jogada o player " + playerTwo.nomeJogador + " entrou na sala");
                            msg = leitor.readLine();
                            
                            playerOne.entrada.println("Faça sua jogada " + this.nomeJogador);
                            msg = leitor.readLine();
                        }
                                           
                        if (msg.equals("pedra")) {
                            this.jogada = msg;
                            entrada.println("Você jogou " + msg + " aguarde o outro player jogar");
                        }
                        
                        msg = leitor.readLine();
                        
                        
                    }
                }
            }
        } catch (IOException ex) {
            System.err.println("Conexão fechada");
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    public PrintWriter getEntrada() {
        return entrada;
    }
    
    public BufferedReader getLeitor() {
        return leitor;
    }
}
