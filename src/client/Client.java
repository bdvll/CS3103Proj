package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {

	ArrayList<String> ownedFiles = new ArrayList<String>();
	FileStorage fileStorage = new FileStorage();
	int bootstrapPort;
	
	//tells bootstrapper that client has a certain file
	private void announceFile(String filename){
		
		try {
			InetAddress myAddress = Inet4Address.getLocalHost();
			Socket bootstrapSock = new Socket(Inet4Address.getByName("bootstrap.se"), bootstrapPort);
			PrintWriter output = new PrintWriter(new BufferedOutputStream(bootstrapSock.getOutputStream()));
			BufferedReader input = new BufferedReader(new InputStreamReader(bootstrapSock.getInputStream()));
			output.write(myAddress.toString()+" announce file: "+filename);
			String response = input.readLine();
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
		ArrayList<String> peers = getPeers(filename);
		for(String peer: peers){
			//START A THREAD
			//IN THAT THREAD CONNECT TO peer
			//DOWNLOAD 1 FRAGMENT OF FILE FROM PEER
			//PUT INTO BUFFER
			//CLOSE CONNECTIONS
			//EXIT THREADS
		}
		//APPEND ALL BUFFERS
		//WRITE BUFFER TO FILE 
	}
	//Get peers who have the file we want
	private ArrayList<String> getPeers(String filename){
		ArrayList<String> connectPeers = new ArrayList<String>();
		try {
			Socket bootstrapSock = new Socket(Inet4Address.getByName("bootstrap.se"), bootstrapPort);
			PrintWriter output = new PrintWriter(new BufferedOutputStream(bootstrapSock.getOutputStream()));
			BufferedReader input = new BufferedReader(new InputStreamReader(bootstrapSock.getInputStream()));
			output.write("GET:"+filename);
			String peer = input.readLine();
			while(!(peer.equals(null))){
				connectPeers.add(peer);
				peer = input.readLine();
			}
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
