package Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import Protocolo.Protocolo;

public class ServidorThread extends Thread{
	private int id;
	private boolean ocupado;
	private Socket s;
	private Object monitor;

	public ServidorThread(int pId, Object pMonitor) {
		id = pId;
		ocupado = false;
		s = null;
		monitor = pMonitor;
	}

	@Override
	public void run() {
		Long TI = System.currentTimeMillis();
		while(s==null){}
		ocupado=true;
		try {
			PrintWriter out = new PrintWriter(s.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String inputLine, outputLine;

			Protocolo kkp = new Protocolo();
			outputLine = kkp.processInput(null);
			out.println(outputLine);

			while ((inputLine = in.readLine()) != null) {
				outputLine = kkp.processInput(inputLine + ";" +id);
				out.println(outputLine);
				System.out.println(outputLine);
				System.out.println(inputLine);
				String[]a = outputLine.split(";");
				if (outputLine.equals("Bye.")) {
					break;
				}
			}
			s.close();
			Servidor.cantidadUsuarios--;
		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen on port "
					+ s.getPort() + " or listening for a connection");
			System.out.println(e.getMessage());
		}
		Long TF = System.currentTimeMillis();
		Long tiempoT = TF - TI;
		synchronized (monitor) {
			if(Servidor.tiempoPromAtencion == 0) {
				Servidor.tiempoPromAtencion += tiempoT;
			}
			else {
				Servidor.tiempoPromAtencion = (Servidor.tiempoPromAtencion + tiempoT)/2;
			}	
		}
		ocupado = false;
	}

	public void assingSocket(Socket pS) {
		s = pS;
		System.out.println("Se asigno correctamente el socket " + s.getLocalPort());
	}
}
