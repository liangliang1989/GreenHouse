/**
 * 主要功能：对服务器socket通信处理。
 */
package chen.server;

import java.io.*;
import java.net.*;
import java.util.*;

import chen.data.model.Device;
import chen.util.PropertyMgr;

public class Server {
	private Boolean started = null; // server是否启动标志，true：启动server,false：未启动
	private ServerSocket ss = null;
	private List<ClientHandle> clients = null; // 客户端处理集合
	private ServerFrame sf = null;
	private static final String UPDATE;   //update命令
	private static final String GETDATA;  //getdata命令
	private static final String CONTROL;  //control命令
	private static final String TRUE;     //1代表true
	private static final String FAlSE;    //0代表false
	private static final int PORT;        //服务器端口号
	static {
		UPDATE = PropertyMgr.getProperty("command_update");
		GETDATA = PropertyMgr.getProperty("command_getdata");
		CONTROL = PropertyMgr.getProperty("command_control");
		TRUE = PropertyMgr.getProperty("isTrue");
		FAlSE = PropertyMgr.getProperty("isFalse");
		PORT = Integer.parseInt(PropertyMgr.getProperty("port"));

	}

	public Server(ServerFrame sf) {
		this.sf = sf;
		this.started = false;
		this.clients = new ArrayList<ClientHandle>();
	}

	// 启动服务器
	public void start() {
		try {
			ss = new ServerSocket(PORT);
			started = true;
			sf.showText("server start successfully!");
		} catch (BindException e) {
			System.out.print(e.getMessage());
			sf.showText("Address already in use!please check it and restart!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.acceptClient();
	}

	// 服务器对客户端的监听
	public void acceptClient() {
		try {
			while (started) {
				Socket s = ss.accept();
				// 对于每次监听到的客户端，则交给ClientHandle类处理，新建线程处理
				ClientHandle c = new ClientHandle(s);
				sf.showText("a client connected!--->" + s);
				new Thread(c).start();
				clients.add(c);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 对客户端进行具体处理的类
	class ClientHandle implements Runnable {
		private Socket s;
		private DataInputStream dis = null;
		private DataOutputStream dos = null;
		private boolean bConnected = false; // 是否连接客户端

		public ClientHandle(Socket s) {
			this.s = s;
			try {
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				bConnected = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 发送消息给客户端
		public void send(String msg) {
			try {
				dos.writeUTF(msg);
				dos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 对Client接收数据，线程处理
		public void run() {
			try {
				while (bConnected) {
					// 解析数据包中命令
					String data = dis.readUTF();
					String datas[] = data.split("/");
					String cmd = datas[0];
					// 判断命令类型
					// updata命令：客户端更新数据到服务器端，服务器取出数据，并设置
					if (cmd.equals(UPDATE)) {
						sf.showText("receive a comman：--->" + data
								+ "    from:--->" + s);
						sf.showText("before-->" + sf.info.toString());
						List<String> update = new ArrayList<String>();
						for (int i = 1; i <= datas.length - 1; i++) {
							update.add(datas[i]);
						}
						// 判断发来的数据是否符合约定(数据条数)
						if (update.size() < 6) {
							send("erro,too short!");
						} else {

							sf.info.setData(update);
						}
						sf.showText("after----->" + sf.info.toString());
					}
					// control命令：客户端设备控制命令：接收数据并设置
					else if (cmd.equals(CONTROL)) {
						sf.showText("receive a comman：--->" + data
								+ "    from:--->" + s);
						sf.showText("before-->" + sf.device.toString());
						List<Boolean> control = new ArrayList<Boolean>();
						Boolean b = null;
						for (int i = 1; i <=datas.length - 1; i++) {
							if (datas[i].equals(TRUE)) {
								b = true;
							} else if (datas[i].equals(FAlSE)) {
								b = false;
							}
							control.add(b);
						}
						// 判断发来的数据是否符合约定(数据条数)
						if (control.size() < 4) {
							send("erro,too short!");
						} else {
							sf.device.setDevice(control);
						}
						sf.showText("after----->" + sf.device.toString());
					}
					// getdata命名：客户端请求更新数据，从服务器发送最新数据给客户端
					else if (cmd.equals(GETDATA)) {
						sf.showText("receive a comman：--->" + data
								+ "    from:--->" + s);
						send(sf.info.getData());
					}
				}
			} catch (IOException e) {
				sf.showText("client close!--->" + s);
			}
			// 断开连接处理
			finally {
				bConnected = false;
				close();
			}
		}

		// 关闭资源处理
		public void close() {

			try {
				if (dis != null)
					dis.close();
				if (dos != null)
					dos.close();
				if (s != null)
					s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
