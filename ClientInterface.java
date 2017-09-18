import java.rmi.*;
import java.util.Vector;

public interface ClientInterface extends Remote{

	public String getName() throws RemoteException; 
	public int getDownload() throws RemoteException;
	public Vector<ResourceInterface> getFiles() throws RemoteException;
	public Vector<ResourceInterface> searchFile(String r) throws RemoteException;
	//public void print(Vector<Resource> v) throws RemoteException; 
	public Vector<ResourceInterface> getFilesFromServer() throws RemoteException; 
	
}
