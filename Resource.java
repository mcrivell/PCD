import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;



public class Resource implements ResourceInterface, Serializable {
	private String name; 
	private int parts; 
	private String owner; 
	
	public Resource(String n, int p){
		name = n; 
		parts = p; 
		//owner = o; 
		System.out.println("aggiunta risorsa"); 
	}
	public String getName(){
		return name; 
	}
	public int getParts() {
		return parts; 
	}
	public String getOwner() {
		return owner; 
	}
	public boolean equals(Resource f) {
		boolean result;
		if(f == null)
			result = false;
		else
		    result = name.equals(f.getName()) &&  parts == f.parts;
		return result;
		}
	/*
	public static void main(String[] args) throws IOException{
		
		Resource risorsa = new Resource("R", 5);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		ObjectOutputStream oos = new ObjectOutputStream(baos);

		oos.writeObject(risorsa);

		byte[] bytes = new byte[5]; 
			bytes = baos.toByteArray();
			
		for(int i = 0; i<bytes.length; i++){
			byte temp = bytes[i]; 
			System.out.println(temp); 
		}
			
		
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

		ObjectInputStream ois = new ObjectInputStream(bais);

		try {
			Resource obj = (Resource)ois.readObject();
			System.out.println(obj.getName());  
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		

		
	}*/
}
