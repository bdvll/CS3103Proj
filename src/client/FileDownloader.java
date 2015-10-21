package client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;

public class FileDownloader implements Runnable{

	public static int SERVER_PORT;
	private FileStorage fileStorage;
	public FileDownloader(FileStorage fileStorage){
		this.fileStorage = fileStorage;
	}
	public void run() {
		
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(SERVER_PORT);
			while(true){
				Socket connectionSocket = serverSocket.accept();
				ConnectionHandler connection = new ConnectionHandler(connectionSocket, fileStorage);
				connection.run();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public class ConnectionHandler implements Runnable{
		Socket connectionSocket;
		FileStorage fileStorage;

		public ConnectionHandler(Socket socket, FileStorage filestorage){
			connectionSocket = socket;
			
		}
		public void run() {
			PrintWriter output;
			try {
				output = new PrintWriter(new BufferedOutputStream(connectionSocket.getOutputStream()));
				BufferedReader input = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				String requestedFile = input.readLine();
				//RECIEVE INFO ON OTHER PEERS (GET MY POSITION i)
				//OPEN REQUESTED FILE
				
				//IF (i == numberOfPeers-1) from -> (Filesize/numberOfPeers)*i to-> EOF
				//ELSE RETURN from - (Filesize/numberOfPeers)*i to - (Filesize/numberOfPeers)*i+1
				
				String path = fileStorage.get(requestedFile);
				//RandomAccessFile file = new RandomAccessFile(name, mode)
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
	}
}
