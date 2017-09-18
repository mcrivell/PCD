import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.*;
import java.util.Iterator;
import java.util.Vector;

@SuppressWarnings("serial")
public class Server extends UnicastRemoteObject implements ServerInterface{
	private ServerGUI serverGui;
	private String name; 
	private Vector<ClientInterface> clients; //all clients connected to the server
	private Vector<ResourceInterface> files; // all files from clients connected to the server
	private static Vector<String> allServers = new Vector<String>(); // all system's servers
	private static final String HOST = "localhost";
	private Object lock = new Object(); 
	
	public Server(String n) throws RemoteException{
		name = n; 
		System.out.println(name); 
		clients = new Vector<ClientInterface>(); 
		files = new Vector<ResourceInterface>(); 
		serverGui = new ServerGUI(n,this);
        ServerCheckDaemon c1 = new ServerCheckDaemon(); 
        c1.start();
        ClientCheckDaemon c2 = new ClientCheckDaemon(); 
        c2.start(); 
	
	}
	public String getName() throws RemoteException{
		return name; 
	}
	public Vector<ClientInterface> getClients() throws RemoteException{
		return clients; 
	}
	public Vector<ResourceInterface> getFiles() throws RemoteException{
		return files; 
	}
	public boolean addClient(ClientInterface c) throws RemoteException{
		boolean ok = false; 
		synchronized(clients){
				if(!clients.contains(c)){
					clients.add(c);
					files.addAll(c.getFiles()); 
					ok = true; 
					//this.addFilesFromClient(c.getFiles()); 
				}
			}
		return ok; 
	}
	
	public void disconnect() throws NotBoundException, RemoteException, MalformedURLException {
       synchronized (lock) {
            Naming.unbind("rmi://" + HOST + "/Server/" + name);
       }
    }
	/*
	
	public synchronized void addFiles(Vector<Resource> r){
		Iterator<Resource> it = r.iterator(); 
		while(it.hasNext()){
			Resource temp = it.next(); 
			if(!files.contains(temp)) 
				files.add(temp); 
		}
		
	}
	
	 public static boolean addServer(Server s) throws RemoteException {
		boolean ok = true; 
		synchronized(allServers){
			Iterator<Server> it = allServers.iterator(); 
			while(it.hasNext())
				if(it.next().getName()== s.getName()) ok = false; 
			if(ok){
				allServers.add(s); 
				System.out.println("aggiunto server: "+s.getName()); 
				}
			}
		return ok; 
		}*/
	class ServerCheckDaemon extends Thread{
		ServerCheckDaemon(){ setDaemon(true); }
		public void run(){
			while(true){
				synchronized(lock){
					try{ 
						String[] list = Naming.list("rmi://" + HOST + "/Server/");
		                serverGui.setServerList(list);
		                allServers.clear();
		                for (int i=0; i<list.length; i++)
		                	allServers.add(list[i]); 	            
		                
					} catch (RemoteException e) {
						e.printStackTrace();
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
					try {
						sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
				}
			}
				}
				
	}
	
	class ClientCheckDaemon extends Thread{
		ClientCheckDaemon(){ setDaemon(true); }
		public void run(){
			while(true){
				synchronized(lock){
					String[] c = new String[clients.size()]; 
					for(int i = 0; i<clients.size(); i++){
						try {
							c[i] = clients.elementAt(i).getName();
						} catch (RemoteException e) {
							 serverGui.appendLog("Client disconnesso\n"); 
	                         clients.remove(i);
						} 
					}
					serverGui.setClientList(c);
					try {
						sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
				try {
					checkOtherServers();
				} catch (Exception e) {
					System.out.println("CheckServerThread report: Server non più presente nel sistema");
					}
				} 
			}
		public void checkOtherServers() throws Exception{
			 synchronized (lock) {
				 Vector<String> allClients = new Vector<String>();
				 for(int i = 0; i<allServers.size(); i++){
					 try{
						ServerInterface s = (ServerInterface)Naming.lookup(allServers.elementAt(i));
						Vector<ClientInterface> temp = s.getClients(); 
						for (int j=0; j<temp.size(); j++) 
							allClients.add(temp.elementAt(j).getName()); 
					 }catch (Exception e1) {  
						 System.out.println("CheckServerThread report: Server non più presente nel sistema"); }
				}
				 serverGui.setGlobalClientList(allClients); 
			 }
		}
	}
		
	public void addFilesFromClient(Vector<ResourceInterface> r) throws RemoteException{
		synchronized(files){
			files.addAll(r); 
		}
	}
	
	public void printServers() throws RemoteException{
		Iterator<String> it = allServers.iterator(); 
		while(it.hasNext())
			System.out.println(it.next()); 
	}
	public Vector<String> getAllServers() throws RemoteException{
		return allServers; 
	}
	
	 public static void main(String[] args) throws Exception {
	    try {
	    	for(int i = 0; i<args.length; i++){
	    		Server s = new Server(args[i]);
	    		String serverName = "rmi://" + HOST + "/Server/" + s.getName();
		 	    Naming.rebind(serverName,s);
		 	    Server.allServers.add(s.getName()); 
	    	}
	    } catch (RemoteException e) {
	        System.out.println("Errore di connessione.");
	        System.exit(1);
	    }
	 }	 
}
