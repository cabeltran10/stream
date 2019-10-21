package Servidor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Servidor {
	public static HashMap<String, String> base = new HashMap<>();
	public static int cantidadUsuarios = -1;
	public static double tiempoPromAtencion = 0.0;
	public static boolean empiezo = false;
	public static StreamThread[] threadS = new StreamThread[5];
	public static String[] rutas = new String[10];

	public static void empezar() {
		System.out.println("Cambie");
		for (int i = 0; i < threadS.length; i++) {
			threadS[i].start();
		}
	}

	public static void main(String[] args) throws IOException {

		if (args.length != 1) {
			System.err.println("Usage: java Servidor <port number>");
			System.exit(1);
		}

		for (int i = 0; i < rutas.length; i++) {
			rutas[i] = "/Users/mac/Desktop/video.wmv";
		}

		for (int i = 0; i < threadS.length; i++) {
			threadS[i] = new StreamThread(i, rutas[i], 10001 + i);
		}

		Object monitor = new Object();

		int portNumber = Integer.parseInt(args[0]);

		ServidorThread[] servidoresDelegados = new ServidorThread[150];

		for (int i = 0; i < servidoresDelegados.length; i++) {
			servidoresDelegados[i] = new ServidorThread(i,monitor);
		}

		ServerSocket serverSocket = null;
		Socket clientSocket = null;
		int i = 0;
		try {

			while(true) {
				InetAddress ip = InetAddress.getLocalHost(); 
				System.out.println(ip);
				System.out.println("Estoy esperando en: " + portNumber);
				serverSocket = new ServerSocket(portNumber);
				cantidadUsuarios++;
				System.out.println("La cantidad de usuarios en este momento es: " + cantidadUsuarios);
				System.out.println("El tiempo promedio de conexion es: " + tiempoPromAtencion);
				System.out.println("La cantidad de usuarios en cola es: 0");
				System.out.println("El tiempo promedio en cola es: 0");
				clientSocket = serverSocket.accept();
				servidoresDelegados[i].assingSocket(clientSocket);
				servidoresDelegados[i].start();
				i = ++i % servidoresDelegados.length;
				serverSocket.close();
			}

		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen on port "
					+ portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
