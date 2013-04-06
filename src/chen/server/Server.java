package chen.server;

import java.io.*;
import java.net.*;
import java.util.*;

import chen.util.PropertyMgr;

public class Server {
	private Boolean started =null;
	private ServerSocket ss = null;
	private List<Client> clients = null;
	private ServerFrame sf=null;
    private static final String UPDATE;
    private static final String GETDATA;
    private static final String CONTROL;
    private static final String TRUE;
    private static final String FAlSE;
    static{
    	UPDATE=PropertyMgr.getProperty("command_update");
    	GETDATA=PropertyMgr.getProperty("command_getdata");
    	CONTROL=PropertyMgr.getProperty("command_control");
    	TRUE=PropertyMgr.getProperty("isTrue");
    	FAlSE=PropertyMgr.getProperty("isFalse");
    	
    }
	public Server(ServerFrame sf) {
		this.sf = sf;
		this.started=false;
		this.clients = new ArrayList<Client>();
	}

	// 启动服务器
	public void start() {
		try {
			ss = new ServerSocket(8888);
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
				Client c = new Client(s);
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

	// 对每个客户端进行处理
	class Client implements Runnable {
		private Socket s;
		private DataInputStream dis = null;
		private DataOutputStream dos = null;
		private boolean bConnected = false;
		public Client(Socket s) {
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

		public void run() {
			
			try {
				while (bConnected) {
					// 解析数据包中命令
					String data=dis.readUTF();
					String datas[]=data.split("/");
					String cmd=datas[0];
					// 判断命令类型
					//updata命令：客户端更新数据到服务器端，服务器取出数据
					if (cmd.equals(UPDATE)) {
						sf.showText("receive a comman：--->"+data+"    from:--->"+s);
						sf.showText("before-->"+sf.info.toString());
						List<String> updata=new ArrayList<String>();
                       for(int i=1;i<=datas.length-1;i++){
                    	   updata.add(datas[i]);
                       }
                       if(updata.size()<6){
                    	  send("erro,too short!");
                       }else{
                    	   
                    	   sf.info.setData(updata);
                       }
                       sf.showText("after----->"+sf.info.toString());
					}
					//control命令：客户端设备控制命令：接收数据
					else if (cmd.equals(CONTROL)) {
						sf.showText("receive a comman：--->"+data+"    from:--->"+s);
						sf.showText("before-->"+sf.device.toString());
						List<Boolean>control=new ArrayList<Boolean>();
						Boolean b=null;
						for(int i=1;i<datas.length-1;i++){
							if(datas[i].equals(TRUE)){
								b=true;
							}else if(datas[i].equals(FAlSE)){
								b=false;
							}
							control.add(b);
						}
						if(control.size()<4){
							send("erro,too short!");
						}else{
							sf.device.setDevice(control);
						}
						sf.showText("after----->"+sf.device.toString());
					}
					//getdata命名：客户端请求更新数据，从服务器发送数据给客户端
					else if (cmd.equals(GETDATA)) {
						sf.showText("receive a comman：--->"+data+"    from:--->"+s);
						send(sf.info.getData());
					}
				}
			} catch (IOException e) {
				sf.showText("client close!--->"+s);
			}finally{
				bConnected=false;
				close();
			}
		}

		// 关闭资源
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
