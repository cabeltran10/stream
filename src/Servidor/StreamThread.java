package Servidor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class StreamThread extends Thread{

	private int id;
	private String rutaVideo;
	private int puerto;

	public StreamThread(int pId, String pRuta, int pPuerto) {
		id = pId;
		rutaVideo = pRuta;
		puerto = pPuerto;
	}

	@Override
	public void run() {
		System.out.println("arranco");

		try {
			File file = new File(rutaVideo);
			File archivo = new File(rutaVideo);
			DatagramSocket ds = new DatagramSocket(); 

			InetAddress ip = InetAddress.getLocalHost(); 
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			FileInputStream fis = new FileInputStream(file);

			byte[] buf = new byte[4096];
			System.out.println("Tamanho de paquetes: " + buf.length);
			int n;
			while (-1 != (n = fis.read(buf)))
				baos.write(buf, 0, n);

			byte[] videoBytes = baos.toByteArray();

			int i = 0;

			String tam ="t:"+videoBytes.length;
			System.out.println(tam);
			byte[]tamanho = tam.getBytes();

			DatagramPacket text = new DatagramPacket(tamanho, tamanho.length, ip, puerto);
			ds.send(text);
			Thread.sleep(50);
			while (i<videoBytes.length) { 
				byte[] temp = new byte[4096];
				int j = 0;
				for (; i < videoBytes.length && j<temp.length; i++) {
					temp[j] = videoBytes[i];
					j++;
				}
				String inp = "Hola, prueba"; 

				DatagramPacket DpSend = new DatagramPacket(temp, temp.length, ip, puerto); 
				ds.send(DpSend); 
				Thread.sleep(0, 1);
				if (inp.equals("bye")) 
					break; 
			} 
		}

		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
