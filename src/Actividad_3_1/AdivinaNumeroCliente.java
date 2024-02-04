package Actividad_3_1;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class AdivinaNumeroCliente {
    public static void main(String[] args) {
        String hostname = "localhost";
        //puerto que usamos
        int port = 2000;

        //creamos un serverSocket
        try (Socket socket = new Socket(hostname, port)) {
            //creamos el PrintWriter y el BufferedReader de la conexión
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //creamos el Scanner de la conexión
            Scanner scanner = new Scanner(System.in);

            System.out.println("Conexion existosa al servidor. Intenta adivinar el número entre 0 y 100.");

            while (true) {
                //le decimos al cliente su numero
                System.out.print("Tu número: ");
                //le solicitamos el numero
                String Usuario = scanner.nextLine();
                out.println(Usuario);

                //
                String Servidor = in.readLine();
                System.out.println("Servidor: " + Servidor);
                //le decimos al cliente que el numero secreto es correcto
                if (Servidor.equals("¡Correcto! ese es el número adivinado")) {
                    break;
                }
            }
        } catch (UnknownHostException e) {
            //
            System.err.println("No se pudo conectar al host: " + hostname);

            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("No se pudo obtener I/O para la conexión con: " + hostname);
            e.printStackTrace();
        }
    }
}