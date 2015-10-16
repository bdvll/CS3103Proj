package client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class Client{
	
	public static int SERVER_PORT = 33333;
	ArrayList<String> ownedFiles = new ArrayList<String>();
	FileStorage fileStorage = new FileStorage();
	int bootstrapPort;
	
	public static void main (String[] args){
		new Client();
	}
	public Client(){
		Scanner scan = new Scanner(System.in);

		System.out.println("Client running");
		System.out.println("Announce a file");
		String filename = scan.nextLine();
		System.out.println("Specify path");
		String path= scan.nextLine();
		announceFile(filename, path);
		
		
	}
	
	//tells bootstrapper that client has a certain file
	private void announceFile(String filename, String path){
		
		System.out.println(fileStorage.put(filename, path));
		
		try {
			String myAddress = Inet4Address.getLocalHost().getHostAddress();
			Socket bootstrapSock = new Socket(Inet4Address.getByName("127.0.0.1"), SERVER_PORT);
			System.out.println("Created socket to bootstrapper");
			PrintWriter output = new PrintWriter(new BufferedOutputStream(bootstrapSock.getOutputStream()));
			BufferedReader input = new BufferedReader(new InputStreamReader(bootstrapSock.getInputStream()));
			
			String test = input.readLine();
//			System.out.println("msg: "+input.readLine());
//			while(!(input.readLine().equals("RDY"))){
//				System.out.println("Waiting for ready");
//			}
			output.write("ANC:"+myAddress.toString()+":"+filename+"\n");
			output.flush();
			System.out.println("wrote 'ANC:"+myAddress.toString()+":"+filename+" to bootstrapper");
			String response = input.readLine();
			System.out.println(response);
			if(response.equals("OK")){
				System.out.println("Success: Bootstrapper accepted file announcement");
				output.close();
				input.close();
				bootstrapSock.close();
			}else{
				System.out.println("Warning: Bootstrapper did not accept file announcement");
				output.close();
				input.close();
				bootstrapSock.close();
			}
		} catch (Exception e) {
			System.out.println("Exception in announceFile");
			e.printStackTrace();
		}
	}
	private void downloadFile(String filename){
		String[] peers = getPeers(filename);
		int fragments = peers.length;

		for(int i = 0; i < peers.length; ++i){
			//START A THREAD
			//IN THAT THREAD CONNECT TO peer
			//Tell peer he is i:th out of X peers
			//DOWNLOAD 1 FRAGMENT OF FILE FROM PEER
			//PUT INTO BUFFER
			//CLOSE CONNECTIONS
			//EXIT THREADS
		}
		//APPEND ALL BUFFERS
		//WRITE BUFFER TO FILE 
	}
	//Get peers who have the file we want
	private String[] getPeers(String filename){
		String[] connectPeers;
		try {
			Socket bootstrapSock = new Socket(Inet4Address.getByName("localhost"), SERVER_PORT);
			PrintWriter output = new PrintWriter(new BufferedOutputStream(bootstrapSock.getOutputStream()));
			BufferedReader input = new BufferedReader(new InputStreamReader(bootstrapSock.getInputStream()));
			output.write("GET:"+filename);
			String peer = input.readLine();
			peer = peer.substring(1, peer.length()-2);
			connectPeers = peer.split(", ");
			output.close();
			input.close();
			bootstrapSock.close();
			return connectPeers;
		} catch (Exception e) {
			System.out.println("Exception in getPeers");
			e.printStackTrace();
		}
	return null;
	}
	
}
