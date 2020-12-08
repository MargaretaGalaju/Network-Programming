package UDP;

import java.io.IOException;
import java.util.Scanner;

public class HttpServerApplication {

	public static void main(String[] args) throws IOException {
		int port = 8080; // Default port number
		String directory = "C:\\Users\\margo\\University\\PR\\NP\\UDP-Protocol"; // Default directory

		UDP.UDPServer server = new UDP.UDPServer(); // server object
		Scanner keyboard = new Scanner(System.in); // User input
		String httpsRequest;

		System.out.print("Please enter your https command (Enter 'quit' to quit application): ");
		httpsRequest = keyboard.nextLine();

		while (!httpsRequest.equalsIgnoreCase("quit")) {
			String[] splitRequest = httpsRequest.split(" ");

			for (int i = 0; i < splitRequest.length; i++) {

				if (splitRequest[i].contains("-p")) {
					port = Integer.parseInt(splitRequest[i + 1]);
				}

				if (splitRequest[i].contains("-d")) {
					for (int j = i + 1; j < splitRequest.length; j++) {
						directory += splitRequest[j];
					}
				}
			}

			System.out.println("");
			System.out.println("Starting server...");
			System.out.println("Running at port: " + port);
			System.out.println("Directory: " + directory);
			System.out.println("");

			// Connect to Server
			server.listenAndServe(port, directory);

			System.out.print("Please enter 'quit' if you want to quit else keep going by inputing any other command: ");
			httpsRequest = keyboard.nextLine();
		}

		System.out.println("Application terminated!");
	}
}