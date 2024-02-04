package Actividad_3_1;

import java.io.*;
import java.net.*;
import java.util.Random;



public class AdivinaNumeroServer {
    public static void main(String[] args) {
        int port = 2000;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor iniciado en el puerto " + port + ". Esperando al cliente...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.out.println("Excepción al intentar iniciar el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                System.out.println("Cliente conectado.");
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                Random random = new Random();
                int numerosecreto = random.nextInt(101);
                int guess = -1;
                while (guess != numerosecreto) {
                    String input = in.readLine();
                    try {
                        guess = Integer.parseInt(input);
                        if (guess < numerosecreto) {
                            out.println("El numero secreto es mayor");
                        } else if (guess > numerosecreto) {
                            out.println("El numero secreto es menor");
                        } else {
                            out.println("¡Correcto! ese es el numero secreto ");
                        }
                    } catch (NumberFormatException e) {
                        out.println("ingresa un número válido.");
                    }
                }
                System.out.println("Número adivinado. Cerrando servidor.");
            } catch (IOException e) {
                System.out.println("Excepción al intentar conectar con el cliente: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
