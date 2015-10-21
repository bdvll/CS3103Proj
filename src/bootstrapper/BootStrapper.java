package bootstrapper;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import client.FileStorage;
import client.FileDownloader.ConnectionHandler;

public class BootStrapper implements Runnable {
	public HashMap<FileInfo, ArrayList<String>> bootstrap = new HashMap<FileInfo, ArrayList<String>>();
	public static int SERVER_PORT = 33333;
	
	public BootStrapper(){
		run();
	}
	
	public static void main(String[] args){
		System.out.println("Bootstrapper started!");
		new BootStrapper();
	}
	
	public void run(){
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(SERVER_PORT);
			while(true){
				Socket connectionSocket = serverSocket.accept();
				System.out.println("BOOTSTRAPPER: Accepted incoming request from"+connectionSocket.getRemoteSocketAddress());
				Thread connectionThread = new Thread(new ConnectionHandler(connectionSocket));
				connectionThread.start();
				System.out.println("BOOTSTRAPPER: created connection thread");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		public class ConnectionHandler implements Runnable{
			Socket connectionSocket;
			FileStorage fileStorage;
	
			public ConnectionHandler(Socket socket){
				connectionSocket = socket;
				
			}
			public void run() {
				PrintWriter output;
				try {
					output = new PrintWriter(new BufferedOutputStream(connectionSocket.getOutputStream()));
					BufferedReader input = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
					output.write("RDY\n");
					output.flush();
					System.out.println("BOOTSTRAPPER: Wrote RDY");
					System.out.println("BOOTSTRAPPER: Waiting for message");
					String request = input.readLine();
					System.out.println("BOOTSTRAPPER: message: "+request);
					if(request.startsWith("GET:")){
						String[] requestArray = request.split(":");
						output.write(bootstrap.get(requestArray[1]).toString());
					}else if(request.startsWith("ANC:")){
						
						String[] requestArray = request.split(":");
						String ip = requestArray[1];
						String file = requestArray[2];
						int size = Integer.parseInt(requestArray[3]);
						System.out.println("BOOTSTRAPPER: Caught ANC request, adding "+ip+" to list of peers for file "+file+" size: "+size+" bytes");
						if(!(bootstrap.containsKey(file))){
							ArrayList<String> peerList = new ArrayList<String>();
							peerList.add(ip);
							FileInfo newFile = new FileInfo(file, size);
							bootstrap.put(newFile,peerList);
						}else{
						ArrayList<String> peers = bootstrap.get(file);
						peers.add(ip);
						}
					}
					System.out.println("BOOTSTRAPPER: Sending ACK for file announcement");
					output.write("OK\n");
					output.flush();
					System.out.println(bootstrap.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
	
			}
			
		}
	}
