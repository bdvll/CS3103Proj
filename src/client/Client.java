package client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
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
		System.out.println("CLIENT: Client started");
		Scanner scan = new Scanner(System.in);
		boolean loop = true;
		while(loop){
			System.out.println("Do you wish to announce a file to the file sharing system? (Y/N)");
			String response = scan.nextLine();
			if(response.equals("N")){
				loop = false;
				break;
			}
			System.out.println("CLIENT: Announce a file");
			String filename = scan.nextLine();
			System.out.println("CLIENT: Specify path");
			String path= scan.nextLine();
	
			announceFile(filename, path);
		}
		
	}
	
	//tells bootstrapper that client has a certain file
	private void announceFile(String filename, String path){
		
		System.out.println(fileStorage.put(filename, path));//PLACEHOLDER. READ FILE AND GET FILESIZE
		File file = new File(path);
		long filesize = file.length();
		
		try {
			String myAddress = Inet4Address.getLocalHost().getHostAddress();
			Socket bootstrapSock = new Socket(Inet4Address.getByName("127.0.0.1"), SERVER_PORT);
			System.out.println("Created socket to bootstrapper");
			PrintWriter output = new PrintWriter(new BufferedOutputStream(bootstrapSock.getOutputStream()));
			BufferedReader input = new BufferedReader(new InputStreamReader(bootstrapSock.getInputStream()));

			if(!(input.readLine().equals("RDY"))){
				System.out.println("CLIENT: Ready message from bootstrapper not recieved.");
			}else{
				System.out.println("CLIENT: Ready message from bootstrapper recieved");
			}

			output.write("ANC:"+myAddress.toString()+":"+filename+":"+filesize+"\n");
			output.flush();
			System.out.println("CLIENT: wrote 'ANC:"+myAddress.toString()+":"+filename+":"+filesize+" to bootstrapper");
			String response = input.readLine();
			if(response.equals("OK")){
				System.out.println("CLIENT: Success: Bootstrapper accepted file announcement");
				output.close();
				input.close();
				bootstrapSock.close();
			}else{
				System.out.println("CLIENT: Warning: Bootstrapper did not accept file announcement");
				output.close();
				input.close();
				bootstrapSock.close();
			}
		} catch (Exception e) {
			System.out.println("CLIENT: Exception in announceFile");
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
			System.out.println("CLIENT: Exception in getPeers");
			e.printStackTrace();
		}
	return null;
	}
	
}
