import java.net.MalformedURLException;
import java.rmi.*;
import java.util.Vector;

public interface ServerInterface extends Remote {
	
	public String getName() throws RemoteException; 
	public Vector<ResourceInterface> getFiles() throws RemoteException;
	public void printServers() throws RemoteException; 
	public Vector<String> getAllServers() throws RemoteException; 
	public void addFilesFromClient(Vector<ResourceInterface> r) throws RemoteException; 
	public boolean addClient(ClientInterface c) throws RemoteException; 
	public void disconnect() throws RemoteException, NotBoundException, MalformedURLException; 
	public Vector<ClientInterface> getClients() throws RemoteException; 
	
}
