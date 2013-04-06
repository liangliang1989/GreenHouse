package chen.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.text.html.HTMLEditorKit.Parser;
import javax.xml.bind.ParseConversionEvent;

import chen.server.ServerFrame;

public class DataRandom implements Runnable{

	private Random random;
	private int init;
	private int result;
	private ServerFrame sf;
	private int preNum;
	private int interval;
	private int type;
	private List randoms;
	private static long TIME;
	private boolean waitFlag; //false 为线程暂停，true为线程执行。
	
	public DataRandom(int r,ServerFrame sf) {
		this.sf=sf;
		this.randoms=new ArrayList();
		random=new Random();
		this.TIME=Long.parseLong(PropertyMgr.getProperty("time"));
		this.init=random.nextInt(r)>30?random.nextInt(r):30;
		this.result=this.init;
		this.waitFlag=false;
	}
	public void setRandom(){
		this.preNum=this.result;
		this.interval=random.nextInt(5);

		this.type=random.nextInt(20)>8?1:-1;

		this.result+=(interval*type);
		if(this.result<=5||this.result>60){
			this.result=this.preNum;
		}

		this.randoms.add(this.result);
		if(this.randoms.size()==7){
			sf.info.setAirTemperature(randoms.get(0).toString());
			sf.info.setAirHumidity(randoms.get(1).toString());
			sf.info.setSoilTemperature(randoms.get(2).toString());
			sf.info.setSoilHumidity(randoms.get(3).toString());
			sf.info.setCo2(randoms.get(4).toString());
			sf.info.setLight(randoms.get(5).toString());
			this.randoms.removeAll(randoms);
		}	
		
	}
	@Override
	public void run() {
		while(true){
			if(waitFlag){
			  this.setRandom();
			}else{
				pause();
			}
			try {
				Thread.sleep(TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public boolean isWaitFlag() {
		return waitFlag;
	}
	public void setWaitFlag(boolean waitFlag) {
		this.waitFlag = waitFlag;
	}
	//线程暂停
	public synchronized void pause(){
		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//线程唤醒
	public synchronized void awake(){
		notifyAll();
	}

}
