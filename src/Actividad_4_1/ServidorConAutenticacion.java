package Actividad_4_1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class ServidorConAutenticacion {
    public static void main(String[] args) {
        int port = 2000;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor escuchando en el puerto " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                Scanner scanner = new Scanner(clientSocket.getInputStream());
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Solicitar autenticación
                out.println("Ingrese nombre de usuario:");
                String username = scanner.nextLine();
                out.println("Ingrese contraseña:");
                String password = scanner.nextLine();

                // Verificar credenciales
                if (!username.equals("javier") || !password.equals("secreta")) {
                    out.println("Acceso denegado.");
                    return;
                }

                out.println("Acceso concedido. Comandos disponibles: LIST, GET <filename>, EXIT");
                String command;
                while (scanner.hasNextLine() && !(command = scanner.nextLine()).equals("EXIT")) {
                    switch (command) {
                        case "LIST":
                            File currentDir = new File(".");
                            String[] files = currentDir.list();
                            if (files != null) {
                                Arrays.stream(files).forEach(out::println);
                            } else {
                                out.println("No se pudieron listar los archivos.");
                            }
                            break;
                        case "EXIT":
                            out.println("Sesión terminada.");
                            break;
                        default:
                            if (command.startsWith("GET ")) {
                                String filename = command.substring(4);
                                File file = new File(filename);
                                if (file.exists() && !file.isDirectory()) {
                                    BufferedReader fileReader = new BufferedReader(new FileReader(file));
                                    String line;
                                    while ((line = fileReader.readLine()) != null) {
                                        out.println(line);
                                    }
                                    fileReader.close();
                                } else {
                                    out.println("ERROR: El archivo no existe.");
                                }
                            } else {
                                out.println("Comando no reconocido.");
                            }
                            break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Error al manejar la conexión del cliente: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
