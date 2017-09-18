
import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.util.Vector;
import java.awt.event.*;
import java.awt.Font;
import javax.swing.plaf.basic.BasicBorders;
import java.rmi.*;
import java.net.MalformedURLException;

/**
 *
 * @author luca
 */
public class ClientGUI extends JFrame {
    private String title;
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int width;
    private int height;
    private static final int hgap = 5;
    private static final int wgap = 5;
    private JPanel contentPane;
    private JPanel topPanel;
    private JPanel centerWestPanel;
    private JPanel centerEastPanel;
    private JPanel centerPanel;
    private JPanel bottomPanel;
    private JTextArea log;
    private JTextField searchField;
    private JButton searchButton;
    private JList serverList;
    private JList downloadQueue;
    private Vector<String> downloadList;
    private JList resourceList;
    private Client clientReference;
    
    class WindowEventHandler extends WindowAdapter {
        public void windowClosing(WindowEvent evt) {
            System.out.println("Window closed");
            //clientReference.disconnect();
        }
    }

    private void setMainPanel() {
        contentPane = new JPanel();
        contentPane.setOpaque(true);
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(5, 5));
    }
    
    private void setTopPanel() {
        topPanel = new JPanel();
        topPanel.setOpaque(true);
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(
            BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK),
                "Cerca file",TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial",Font.PLAIN,10)));
        
        searchField = new JTextField(10);
        searchButton = new JButton("Cerca");
        searchButton.setFont(new Font("Arial",Font.BOLD,10));
        /*searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String s = searchField.getText();
                String [] a = s.split("\\s+"); // separo le parole
                if (a.length < 2) {
                    popError("Stringa di ricerca errata!");
                    return;
                }
                if (!clientReference.isDownloading()) {
                    clientReference.sendRequest(a[0],a[1]);
                } else {
                    popError("Il client sta già scaricando una risorsa!");
                    return;
                }
            }
        });*/
        topPanel.add(searchField,BorderLayout.WEST);
        topPanel.add(searchButton,BorderLayout.EAST);
        
        
    }
    
    private void setCenterPanel() {
        centerWestPanel = new JPanel();
        centerWestPanel.setOpaque(true);
        centerWestPanel.setBackground(Color.WHITE);
        centerWestPanel.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK),
                "Server disponibili",TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial",Font.PLAIN,10)));
        serverList = new JList();
        serverList.setFont(new Font("Arial",Font.BOLD,8));

        centerWestPanel.setLayout(new BoxLayout(centerWestPanel,BoxLayout.PAGE_AXIS));
        centerWestPanel.add(serverList);

        centerPanel = new JPanel();
        centerPanel.setOpaque(true);
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK),
                "Coda download",TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial",Font.PLAIN,10)));
        downloadQueue = new JList();
        downloadQueue.setFont(new Font("Arial",Font.BOLD,10));
        downloadList = new Vector<String>();
        centerPanel.add(downloadQueue,BorderLayout.NORTH);
        /*****/
        DefaultListModel model = new DefaultListModel();
        downloadQueue.setModel(model);
        /****/
        centerEastPanel = new JPanel();
        centerEastPanel.setOpaque(true);
        centerEastPanel.setBackground(Color.WHITE);
        centerEastPanel.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK),
                "Risorse disponibili",TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial",Font.PLAIN,10)));
        //centerEastPanel.setPreferredSize(new Dimension((int) width/2 -25, (int) height/2));
        resourceList = new JList();
        resourceList.setFont(new Font("Arial",Font.BOLD,10));
        centerEastPanel.add(resourceList);
    }
    
    private void setBottomPanel() {
        bottomPanel = new JPanel();
        bottomPanel.setOpaque(true);
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK),
                "Log",TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial",Font.PLAIN,10)));
        bottomPanel.setLayout(new BorderLayout());
        log = new JTextArea();
        log.setFont(new Font("Arial",Font.PLAIN,12));
        log.setRows(10);
        log.setEditable(false);
        log.setBackground(Color.BLACK);
        log.setForeground(Color.GREEN);
        
        bottomPanel.add(log,BorderLayout.CENTER);
    }
    
    public ClientGUI(String n, Client r) {
        super(n);
        clientReference = r;
        title = n;        
        width = (int) screenSize.getWidth()/3;
        height = (int) screenSize.getHeight() - 100;
        
        setMainPanel();
        setTopPanel();
        setCenterPanel();
        setBottomPanel();
        contentPane.add(topPanel,BorderLayout.PAGE_START);
        contentPane.add(centerWestPanel,BorderLayout.WEST);
        contentPane.add(centerEastPanel,BorderLayout.EAST);
        contentPane.add(centerPanel,BorderLayout.CENTER);
        contentPane.add(bottomPanel,BorderLayout.PAGE_END);
        setContentPane(contentPane);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setVisible(true);
    }

    public void setServerList(String[] l) {
        DefaultListModel model = new DefaultListModel();
        for (int i=0; i<l.length; i++) { model.addElement(l[i]); }
        serverList.setModel(model);
    }

    public void setResourceList(Vector<String> l) {
        DefaultListModel model = new DefaultListModel();
        for (int i=0; i<l.size(); i++) { model.addElement(l.elementAt(i)); }
        resourceList.setModel(model);
    }

    public void setDownloadQueue() {
        DefaultListModel model = new DefaultListModel();
        for (int i=0; i<downloadList.size(); i++) { model.addElement(downloadList.elementAt(i)); }
        downloadQueue.setModel(model);
    }

    public void appendLog(String s) {
        log.append(s + "\n");
    }

    public void popError(String message) {
        JOptionPane.showMessageDialog(this,message);
    }

    public void addDownloadList(String l) {
        downloadList.add(l);
        setDownloadQueue();
    }

    public void popDownloadList(int n) {
        for (int i=downloadList.size()-1; i>= downloadList.size()-n; i++) {
            downloadList.remove(i);
        }
    }

    public int getDownloadListSize() {
        return downloadList.size();
    }

    public void eraseDownloadList() {
        downloadList.clear();
        setDownloadQueue();
    }

    public void modifyDownloadList(int index, String s) {
        downloadList.set(index,s);
        setDownloadQueue();
    }
}