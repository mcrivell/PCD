import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.*;
import java.util.Iterator;
import java.util.Vector;


@SuppressWarnings("serial")
public class Client extends UnicastRemoteObject implements ClientInterface{
	private String name; 
	private int download; 
	private CheckServer sc = null; 
	private Vector<ResourceInterface> files; 
	//private Vector<FileStruct> fileStruct; 
	private ServerInterface connected; //il server a cui il client è connesso
	private Vector<Client> activeClients = new Vector<Client>();  
	private static final String HOST="localhost";
	private ClientGUI clientGUI; 
	
	
	public Client(String n, String s, int c, Vector<ResourceInterface> r) throws RemoteException{
		name = n; 
		download = c; 
		files = r; 
		clientGUI = new ClientGUI(name,this);
		System.out.println("Creato client: "+ getName()); 
		connect(s); 
		System.out.println(files.size()); 
		printFiles(this); 
		}
	
	public void connect(String s) throws RemoteException{ 
		try {
			connected = (ServerInterface)Naming.lookup("rmi://" + HOST + "/Server/" + s);
			System.out.println("connesso al server: " + connected.getName()); 
			connected.addClient(this); //prova provvisoria 
			sc = new CheckServer(); sc.start();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public Vector<ResourceInterface> searchFile(String r) throws RemoteException{
		Vector<ResourceInterface> temp = new Vector<ResourceInterface>(); 
		synchronized(files){
			Iterator<ResourceInterface> it = files.iterator(); 
			while(it.hasNext()){
				ResourceInterface t = it.next(); 
				if(t.getName() == r)
					temp.add(t); 
			}
		}
		return temp; 
	}
	
	public Vector<ResourceInterface> getFilesFromServer() throws RemoteException{
		Vector<ResourceInterface> temp = connected.getFiles(); 
		return temp; 
	}
	
	public void printServers(Vector<Server> v) throws RemoteException{
		Iterator<Server> it = v.iterator(); 
		while(it.hasNext()){
			System.out.println(it.next().getName()); 
		} 
			
	}
	public void printFiles(Client c) throws RemoteException{
		
        Vector<ResourceInterface> temp = c.getFilesFromServer(); 
        Iterator<ResourceInterface> it = temp.iterator(); 
        while(it.hasNext()) {
        	System.out.println(it.next().getName()); 
        }
	}
	
	public Vector<ResourceInterface> getFiles() throws RemoteException{
		return files; 
	}
	
	public String getName() throws RemoteException{
		return name; 
	}
	public int getDownload() throws RemoteException{
		return download; 
	}
	
	public class CheckServer extends Thread{
		CheckServer(){ setDaemon(true); }
		public void run(){
			while(true){
				try{
					String[] temp = Naming.list("rmi://" + HOST + "/Server/");
					//for(int i = 0; i<temp.length; i++)
						//System.out.println(temp[i]); 
				}catch (Exception exc) {  System.out.println(
                "CheckServerThread report: Server non più presente nel sistema"); 
                return;
            }
				try {
					sleep(10);
				} catch (InterruptedException e) {
					System.out.println("ServerChecker Thread interrotto"); return; 
				} 
			}
		}
	}
	
	public static void main(String[] args) {
	     try {
	        Vector<ResourceInterface> aux = new Vector<ResourceInterface>();
	        for (int i=3; i<args.length; i+=2) {
	            aux.add(new Resource(args[i],Integer.parseInt(args[i+1])));
	        }
	        Client c = new Client(args[0],args[1],Integer.parseInt(args[2]),aux);	        
	        	
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	}
}
