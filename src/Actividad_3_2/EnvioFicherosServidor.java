package Actividad_3_2;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class EnvioFicherosServidor {
    public static void main(String[] args) {
        int port = 1500;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor escuchando en el puerto " + port);
            System.out.println("Directorio de trabajo actual: " + new File(".").getAbsolutePath());

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
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                String nombrefichero = in.readLine();
                File fichero = new File(nombrefichero);

                if (fichero.exists() && !fichero.isDirectory()) {
                    out.println("Archivo encontrado. Enviando Fichero al cliente");

                    BufferedReader fileReader = new BufferedReader(new FileReader(fichero));
                    String line;
                    while ((line = fileReader.readLine()) != null) {
                        out.println(line);
                    }
                    //cerramos el fichero
                    fileReader.close();
                    out.println("FIN_DEL_ARCHIVO");
                } else {
                    //si el fichero no existe
                    System.out.println("Error: El archivo solicitado (" + nombrefichero + ") no existe o no se pudo leer.");
                    out.println("ERROR: El archivo no existe.");
                }

            } catch (IOException e) {
                System.out.println("Error al manejar la conexión del cliente: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
