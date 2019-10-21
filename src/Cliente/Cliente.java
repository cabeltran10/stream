package Cliente;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {
	public static void main(String[] args) throws IOException {
		File file3 = new File("/Users/mac/Documents/workspace/Final/src/hola.wmv");

		Scanner sc = new Scanner(System.in); 
		System.out.println(args.length);
		if (args.length != 2) {
			System.err.println("Usage: java EchoClient <host name> <port number>");
			System.exit(1);
		}

		String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);

		Long timeI = System.currentTimeMillis();
		try  {
			Socket kkSocket = new Socket(hostName, portNumber);
			PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			String fromServer;
			String fromUser;
			int k = -1;
			while ((fromServer = in.readLine()) != null) {
				String[]a = fromServer.split(";");
				System.out.println("Server: " + fromServer);
				if (fromServer.equals("Bye.")) {
					break;
				} else if(a[0].equalsIgnoreCase("ver stream")) {
					DatagramSocket ds = new DatagramSocket(Integer.parseInt(a[1])); 
					byte[] receive = new byte[4096];

					System.out.println("a");
					DatagramPacket DpReceive = null; 
					int i = 0;

					int tam = 50000000;

					byte[] arrayF = new byte[tam];

					while(k < tam) {
						if(k==-1) {
							DatagramPacket temp = new DatagramPacket(receive, receive.length); 
							ds.receive(temp); 
							if(data(receive).toString().startsWith("t")) {
								String hola = data(receive).toString();
								String[]x = hola.split(":");
								int tamA = Integer.parseInt(x[1]);
								tam = tamA;
								k++;
							}
						} else {
							DpReceive = new DatagramPacket(receive, receive.length); 
							ds.receive(DpReceive); 

							i++;

							for (int j = 0; j < receive.length; j++) {
								if(k<arrayF.length){
									arrayF[k] = receive[j];
									k++;
								}
							}
							receive = new byte[4096]; 
						}
					}

					try {
						FileOutputStream fileOuputStream = new FileOutputStream(file3);
						fileOuputStream.write(arrayF);
						Long timeF = System.currentTimeMillis();
						System.out.println("Tiempo total de transmisión: " + (timeF - timeI));
					} catch (Exception e) {
						// TODO: handle exception
					}

				}

				fromUser = stdIn.readLine();
				if (fromUser != null) {
					System.out.println("Client: " + fromUser);
					out.println(fromUser);
				}
			}
			kkSocket.close();
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " +
					hostName);
			System.exit(1);
		}
	}

	public static StringBuilder data(byte[] a) { 
		if (a == null) 
			return null; 
		StringBuilder ret = new StringBuilder(); 
		int i = 0; 
		while (i<a.length && a[i] != 0 ) { 
			ret.append((char) a[i]); 
			i++; 
		} 
		return ret; 
	}
}
