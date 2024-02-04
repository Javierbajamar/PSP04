package Actividad_3_2;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class EnvioFicherosCliente2 {
    public static void main(String[] args) {
        String hostname = "localhost";
        //puerto que usamos
        int port = 1500;

        //creamos un serverSocket
        try (Socket socket = new Socket(hostname, port);
             //creamos el PrintWriter y el BufferedReader de la conexión
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             //creamos el BufferedReader de la conexión
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             //creamos el Scanner de la conexión
             Scanner scanner = new Scanner(System.in)) {

            System.out.print("Ingrese el nombre del fichero a solicitar: ");
            //le solicitamos el nombre del fichero
            String fileName = scanner.nextLine();
            out.println(fileName);

            String serverResponse;

            while (!(serverResponse = in.readLine()).equals("FIN_DEL_ARCHIVO")) {
                if (serverResponse.startsWith("ERROR:")) {
                    System.out.println(serverResponse);
                    return;
                }
                System.out.println(serverResponse);
            }

            System.out.println("Archivo recibido correctamente.");

        } catch (UnknownHostException e) {
            System.err.println("No se pudo conectar al host: " + hostname);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("No se pudo obtener I/O para la conexión con: " + hostname);
            e.printStackTrace();
        }
    }
}