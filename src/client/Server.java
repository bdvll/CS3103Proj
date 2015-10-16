package client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{

	public static int SERVER_PORT;
	@Override
	public void run() {
		
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(SERVER_PORT);
			Socket connectionSocket = serverSocket.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
