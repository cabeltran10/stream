package Protocolo;

import Servidor.Servidor;

public class Protocolo {
	private static final int WAITING = 0;
	private static final int SENT_CONFIRMATION = 1;
	private static final int AUTENTICADO = 2;
	private static final int SOL_STREAM = 3;
	private static final int VIEWING_STREAM = 3;


	private static final int NUMJOKES = 5;

	private int state = WAITING;
	private int currentJoke = 0;

	private String[] clues = { "Turnip", "Little Old Lady", "Atch", "Who", "Who" };
	private String[] answers = { "Turnip the heat, it's cold in here!",
			"I didn't know you could yodel!",
			"Bless you!",
			"Is there an owl in here?",
	"Is there an echo in here?" };

	public String processInput(String theInput) {
		String theOutput = null;

		if (state == WAITING) {
			theOutput = "Bienvenido";
			state = SENT_CONFIRMATION;
		} else if (state == SENT_CONFIRMATION) {
			String[] temp = theInput.split("%");
			String esteEs = temp[0];
			String[] a = esteEs.split(";");

			if(a[0].equalsIgnoreCase(("salir"))) {
				theOutput = "Bye.";
				state = WAITING;
			} else if (a[0].equalsIgnoreCase("Autenticar")) {
				String enviar = "";
				System.out.println(a.length);
				if(a.length>=3) {
					String user = a[1];
					String pass = a[2];
					String passC = Servidor.base.get(user);

					if(passC == null || !pass.equals(passC)) {
						enviar = "Algo salió mal, contrasenha o usuario erróneos";
					} else {
						enviar = "Cliente autenticado, bienvenido " + user;
						state = AUTENTICADO;
					}
				} else {
					enviar = "Error, el comando debe ser autenticar;<nombreUsuario>;<contrsenha> o registrar;<nombreUsuario>;<contrsenha>" + "Vuelva a intentarlo";
				}

				theOutput = enviar + " ";
			} else if(a[0].equalsIgnoreCase("registrar")) {
				String enviar = "";
				if(a.length>=3) {
					String user = a[1];
					String pass = a[2];
					try {
						Servidor.base.put(user, pass);
						enviar = "Usuario registrado, bienvenido " + user;
						state = AUTENTICADO;
					} catch(Exception e) {
						enviar = "Ya existe el usuario";
					}
				} else {
					enviar = "Error, el comando debe ser autenticar;<nombreUsuario>;<contrsenha> o registrar;<nombreUsuario>;<contrsenha>" + "Vuelva a intentarlo";
				}
				theOutput = enviar + " ";
			} else {
				theOutput = "Error, el comando debe ser autenticar;<nombreUsuario>;<contrsenha> o registrar;<nombreUsuario>;<contrsenha>" + "Vuelva a intentarlo";
			}
		} else if (state == AUTENTICADO) {
			String[] temp = theInput.split("%");
			String esteEs = temp[0];
			String[] a = esteEs.split(";");
			if(a[0].equalsIgnoreCase("salir")) {
				theOutput = "Bye.";
				state = WAITING;
			} else if (a[0].equalsIgnoreCase("solicito stream")) {
				theOutput = "10001;10002;10003;10004;10005;10006;10007;10008;10009;10010";
				state = SOL_STREAM;
			} else {
				theOutput = "You're supposed to say \"" + 
						clues[currentJoke] + 
						" who?\"" + 
						"! Try again. Knock! Knock!";
				state = SENT_CONFIRMATION;
			}
		} else if (state == SOL_STREAM) {
			String[] temp = theInput.split("%");
			String esteEs = temp[0];
			String[]a = esteEs.split(";");
			int numStream = 0;
			try {
				if(!a[0].equalsIgnoreCase("salir")) {
					numStream = Integer.parseInt(a[0]);

					if(numStream<10001 || numStream>10010) {
						theOutput = "El número ingresado debe estar entre 10001 y 10010";
					} else {
						Servidor.empezar();
						theOutput = "Ver stream;" + numStream;
						state = VIEWING_STREAM;
					}
				} else if(a[0].equalsIgnoreCase("salir")) {
					theOutput = "Bye.";
					state = WAITING;
				}
			} catch(Exception e) {
				theOutput = "Debe ser ingresado un número";
				e.printStackTrace();
			}
		}
		return theOutput;
	}
}
