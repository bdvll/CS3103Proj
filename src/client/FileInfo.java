package client;

public class FileInfo {

	public int size; //File size in bytes
	public String name; //Filename
	
	public FileInfo(String name, int size){
		this.size = size;
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof FileInfo)) return false;
		if(obj == this) return true;
		
		FileInfo comp = (FileInfo) obj;
		if((comp.name.equals(this.name)) && (comp.size == this.size)) return true;
		
		return false;
	}

	@Override
	public String toString() {
		return "FileInfo [size=" + size + ", name=" + name + "]";
	}
}