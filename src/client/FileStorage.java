package client;

import java.util.concurrent.ConcurrentHashMap;

public class FileStorage {
	
	private ConcurrentHashMap<String,String> files = new ConcurrentHashMap<String,String>();

	public String put(String filename, String path){
		files.put(filename, path);
		return "Put "+filename+" in file map.";
	}
	public String get(String filename){
		if(files.containsKey(filename)){
			return files.get(filename);
		}
		else{
			return "File: "+filename+" was not found in file map.";
		}
	}
}
