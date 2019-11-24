package skep;

import java.net.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.rmi.server.ServerCloneException;
import java.util.*;
import javax.swing.*;
import java.security.*;

public class server extends JFrame implements Runnable {
	private ServerSocket serverSocket;
	HashMap<String, String> cMap=new HashMap<>();
	JTextArea jTextArea = new JTextArea();
	Socket socket = null;
	BufferedReader bReader = null;
	PrintStream pStream = null;
	String cname = null;
	
	server self = this;
	String feed = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	int num = 20;
	int len = 5;


	public server() {
		// TODO Auto-generated constructor stub
		this.setTitle("Server");
		this.setVisible(true);
		this.setSize(300, 300);
		this.setLocation(50, 50);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.add(jTextArea);
		try {
			serverSocket = new ServerSocket(19901);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    new Thread(this).start();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
		        socket = serverSocket.accept();
				bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				pStream = new PrintStream(socket.getOutputStream());
				String msg = bReader.readLine();
				if(msg.equals("REGISTER")) {
					cname=bReader.readLine();
					if(self.cMap.containsKey(cname)) {
						pStream.println("EXSIST");
					}else {
						pStream.println("Register Succeed");
						pStream.println(String.valueOf(num));
						Random random = new Random();
						StringBuffer sBuffer = new StringBuffer();
						for (int i = 0; i < len; i++) {
							int number = random.nextInt(62);
							sBuffer.append(feed.charAt(number));
						}
						String first = sBuffer.toString();
						for (int i = 0; i < num; i++) {
							first = Hash(first);
							pStream.println(first);
						}
						first = Hash(first);
						self.cMap.put(cname, first);
					}
					
				}else if(msg.equals("LOGIN")){
					String cname = bReader.readLine();
					String key = bReader.readLine();
					String first = self.cMap.get(cname);
					if (first.equals(Hash(key))) {
						pStream.println("SUCCESS");
						self.cMap.put(cname, key);
					} else {
						pStream.println("FAILED");
					}
				}
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}
	
	public static String Hash(String string) {
		String resString = "";
		try {
			MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
			mDigest.update(string.getBytes("UTF-8"));
			resString = byte2Hex(mDigest.digest());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resString;
	}

	private static String byte2Hex(byte[] bytes) {
		StringBuffer sBuffer = new StringBuffer();
		String temp = null;
		for (int i = 0; i < bytes.length; i++) {
			temp = Integer.toHexString(bytes[i] & 0xFF);
			if (temp.length() == 1) {
				sBuffer.append("0");
			} else {
				sBuffer.append(temp);
			}
		}
		return sBuffer.toString();
	}

	public static void main(String[] args) {
		new server();
	}

}
