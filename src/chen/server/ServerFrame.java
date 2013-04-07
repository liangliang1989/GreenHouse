/**
 * 实现server的界面显示功能
 */
package chen.server;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.swing.*;

import chen.data.model.DataInfo;
import chen.data.model.Device;
import chen.util.DataRandom;
import chen.util.PropertyMgr;

public class ServerFrame extends JFrame {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ServerFrame sf = new ServerFrame();
		Server server = new Server(sf);
		server.start();
	}
    /**
     * 定义布局控件
     */
	private JTextField jtf1, jtf2, jtf3, jtf4, jtf5, jtf6;
	private JLabel jl1, jl2, jl3, jl4, jl5, jl6, jl7, jl8, jl9, jl10;
	private JButton jb1, jb2, jb3, jb4,jb5,jb6;
	private JPanel jp1, jp2, jp3, jp4;
	private JTextArea jta;
	private JScrollPane jsp;
	private DataRandom dr;
	public static DataInfo info;
	public static Device device;
	private static final int WIDTH;      //界面宽度
	private static final int HEIGHT;     //界面高度
	private static final int INIT_RANDOM;//初始化随机数范围
	private static final String TITLE;   //标题
	private Thread thread;               //产生随机数的线程
	static {
		info = new DataInfo();
		WIDTH = Integer.parseInt(PropertyMgr.getProperty("server_width"));
		HEIGHT = Integer.parseInt(PropertyMgr.getProperty("server_height"));
		INIT_RANDOM = Integer.parseInt(PropertyMgr.getProperty("initRandom"));
		TITLE = PropertyMgr.getProperty("title");
	}
	
	//构造函数，初始化参数
	public ServerFrame() {
		dr = new DataRandom(INIT_RANDOM, this);
		thread = new Thread(dr);
		thread.start();
		device = new Device(false, false, false, false);
		jp1 = new JPanel(new GridLayout(0, 2));
		jtf1 = new JTextField(5);
		jtf2 = new JTextField(5);
		jtf3 = new JTextField(5);
		jtf4 = new JTextField(5);
		jtf5 = new JTextField(5);
		jtf6 = new JTextField(5);
		jb1 = new JButton("setting");
		jb1.addActionListener(new ButtonActionListener());
		jb2 = new JButton("refresh");
		jb2.addActionListener(new ButtonActionListener());
		jb3 = new JButton("start");
		jb3.addActionListener(new ButtonActionListener());
		jb4 = new JButton("pause");
		jb4.addActionListener(new ButtonActionListener());
		jb5 = new JButton("clear");
		jb5.addActionListener(new ButtonActionListener());
		jb6 = new JButton("exit");
		jb6.addActionListener(new ButtonActionListener());
		jta = new JTextArea();
		jsp = new JScrollPane(jta);
		jl1 = new JLabel(" airTemperature:");
		jl2 = new JLabel("    airHumidity:");
		jl3 = new JLabel("soilTemperature:");
		jl4 = new JLabel("   soilHumidity:");
		jl5 = new JLabel("            co2:");
		jl6 = new JLabel("          light：");
		jl7 = new JLabel(device.getWindow());
		jl8 = new JLabel(device.getFan());
		jl9 = new JLabel(device.getHeater());
		jl10 = new JLabel(device.getHumidifier());
		jp1.add(jl1);
		jp1.add(jtf1);
		jp1.add(jl2);
		jp1.add(jtf2);
		jp1.add(jl3);
		jp1.add(jtf3);
		jp1.add(jl4);
		jp1.add(jtf4);
		jp1.add(jl5);
		jp1.add(jtf5);
		jp1.add(jl6);
		jp1.add(jtf6);
		jp1.add(jb1);
		jp1.add(jb2);
		jp1.add(jb3);
		jp1.add(jb4);
		jp1.add(jb5);
		jp1.add(jb6);
		jp1.add(jl7);
		jp1.add(jl8);
		jp1.add(jl9);
		jp1.add(jl10);
		this.add(jp1, BorderLayout.NORTH);
		this.add(jsp);
		this.setTitle(TITLE);
		this.setLocation(300, 400);
		this.setSize(WIDTH, HEIGHT);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	class ButtonActionListener implements ActionListener {

		//更新数据方法
		public void updataInfo() {
			info.setAirTemperature(jtf1.getText());
			info.setAirHumidity(jtf2.getText());
			info.setSoilTemperature(jtf3.getText());
			info.setSoilHumidity(jtf4.getText());
			info.setCo2(jtf5.getText());
			info.setLight(jtf6.getText());
			showText(info.toString());
		}

		// 设置按钮处理
		public void actionPerformed(ActionEvent e) {
            //设置按钮处理
			if (e.getSource() == jb1) {
				updataInfo();
			}
			// 刷新按钮处理
			else if (e.getSource() == jb2) {
				showText("before--->" + info.toString());
				jtf1.setText(info.getAirTemperature());
				jtf2.setText(info.getAirHumidity());
				jtf3.setText(info.getSoilTemperature());
				jtf4.setText(info.getSoilHumidity());
				jtf5.setText(info.getCo2());
				jtf6.setText(info.getLight());
				jl7.setText(device.getWindow());
				jl8.setText(device.getFan());
				jl9.setText(device.getHeater());
				jl10.setText(device.getHumidifier());
				showText("refresh-->" + info.toString());
				showText("refresh-->" + device.toString());
			}
			// 产生随机数
			else if (e.getSource() == jb3) {
				dr.setWaitFlag(true);
				dr.awake();
				showText("start random!----------------->");
			}
			// 暂停线程
			else if (e.getSource() == jb4) {	
				dr.setWaitFlag(false);
				showText("pause random!----------------->");
			}
			//清屏
			else if(e.getSource()==jb5){
			  jta.setText(null);
			  showText("clear done!");
			}
			//退出
			else if(e.getSource()==jb6){
				System.exit(0);
			}

		}
	}
	
	// 用于显示信息
	public void showText(String str) {
		String time = "[" + this.info.getTime() + "]";
		String showStr = this.jta.getText() + time + str + "\n";
		this.jta.setText(showStr);
	}
}
