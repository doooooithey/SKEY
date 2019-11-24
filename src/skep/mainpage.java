package skep;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.*;
import java.rmi.server.ServerCloneException;
import java.util.*;
import javax.swing.*;



public class mainpage extends JFrame{
	
	JPanel jpanel1=new JPanel();
	JPanel jpanel2=new JPanel();
	JPanel jpanel3=new JPanel();
	JTextField j1=new JTextField(25);
	JTextField j2=new JTextField(25);
	JButton b1=new JButton("注册");
	JButton b2=new JButton("登录");
	JButton b3=new JButton("获取动态口令");
	JLabel l1=new JLabel("请输入英文字符完成初次注册");
	static String name=null;
	public String key=null;
	Socket socket=null;
	mainpage self=this;
	
	public mainpage() throws Exception{
		this.setTitle("SKEY");
		this.setSize(400,150);
		this.setLocation(200, 200);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.add(jpanel1,BorderLayout.NORTH);
		jpanel1.add(j1,BorderLayout.NORTH);
		jpanel1.add(b1,BorderLayout.WEST);
		this.add(jpanel3, BorderLayout.CENTER);
		jpanel3.add(l1,BorderLayout.SOUTH);
		jpanel3.add(b3,BorderLayout.WEST);
		this.add(jpanel2,BorderLayout.SOUTH);
		jpanel2.add(j2,BorderLayout.CENTER);
		jpanel2.add(b2,BorderLayout.WEST);
		
		b1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent m1) {
				name=j1.getText();
				
				try {
					socket=new Socket("127.0.0.1", 19901);
					BufferedReader bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					PrintStream pStream = new PrintStream(socket.getOutputStream());
					
					pStream.println("REGISTER");
					pStream.println(name);
					String msg=bReader.readLine();
					if(!msg.equals("EXSIST")) {
						msg = bReader.readLine();
						int num = Integer.valueOf(msg).intValue();
						ArrayList<String> strings = new ArrayList<>();
						for (int i = 0; i < num; i++) {
							msg = bReader.readLine();
							strings.add(msg);
						}
						File file = new File(name + ".txt");
						BufferedWriter fWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
						for (int i = num - 1; i >= 0; i--) {
							fWriter.write(strings.get(i));
							fWriter.newLine();
						}
						fWriter.close();
						JOptionPane.showMessageDialog(self, "注册成功", "注册成功", JOptionPane.PLAIN_MESSAGE);
					}else {
						JOptionPane.showMessageDialog(self, "注册失败", "用户名重复", JOptionPane.ERROR_MESSAGE);
					}
					socket.close();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});
		
		b2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent m2) {
			    name=j1.getText();
			    key=j2.getText();
			    try {
					String name=j1.getText();
					String word=j2.getText();
					socket=new Socket("127.0.0.1", 19901);
					PrintStream pStream = new PrintStream(socket.getOutputStream());
					BufferedReader bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					pStream.println("LOGIN");
					pStream.println(name);
					pStream.println(word);
					String msg=bReader.readLine();
					if(msg.equals("SUCCESS")) {
						File file = new File(name+".txt");
						BufferedReader fReader= new BufferedReader(new FileReader(file.getAbsolutePath()));
						ArrayList<String> list = new ArrayList<>();
						String string=null;
						while((string=fReader.readLine())!=null) {
							list.add(string);
						}
						BufferedWriter fWriter = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
						for(int i=1;i<list.size();i++) {
							fWriter.write(list.get(i));
							fWriter.newLine();
						}
						fReader.close();
						fWriter.close();
						JOptionPane.showMessageDialog(self, "登录成功", "登录成功", JOptionPane.PLAIN_MESSAGE);
					}else {
						JOptionPane.showMessageDialog(self, "登录失败", "登录失败", JOptionPane.ERROR_MESSAGE);
					}
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		b3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					String name = j1.getText();
				File file=new File(name+".txt");
				if(!file.exists()) {
					JOptionPane.showMessageDialog(self, "当前用户未注册", "错误", JOptionPane.ERROR_MESSAGE);
				}else{
					BufferedReader fReader= new BufferedReader(new InputStreamReader(new FileInputStream(file)));
					String word=fReader.readLine();
					if(word==null) {
						JOptionPane.showMessageDialog(self, "用户已过期，请重新注册", "错误", JOptionPane.ERROR_MESSAGE);
					}else {
						j2.setText(word);
					}
					fReader.close();
				}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			
		});
	}
	
	
	public static void main(String[] args) throws Exception {
		new mainpage();
	}
}



