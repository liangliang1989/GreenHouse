/**
 * 这个类用来产生随机数；主要用来模拟温室环境中数据的变化
 */
package chen.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.text.html.HTMLEditorKit.Parser;
import javax.xml.bind.ParseConversionEvent;

import chen.server.ServerFrame;

public class DataRandom implements Runnable {

	private Random random;
	private int init; // 产生第一个随机数(初始值)
	private int result; // 最终产生的随机数（最终值）
	private ServerFrame sf;
	private int preNum; // 用来记录上一个数
	private int interval; // 每次参数变化的幅度，由随机数产生
	private int type; // 随机产生运算符，"+"或者"-"
	private List randoms;
	private static long TIME;
	private boolean waitFlag; // false 为线程暂停，true为线程执行。

	// 构造函数
	public DataRandom(int r, ServerFrame sf) {
		this.sf = sf;
		this.randoms = new ArrayList();
		random = new Random();
		this.TIME = Long.parseLong(PropertyMgr.getProperty("time"));
		// 产生初始值，如果随机数大于30，则初始值为产生的数，否则初始值为30；
		this.init = random.nextInt(r) > 30 ? random.nextInt(r) : 30;
		this.result = this.init;
		this.waitFlag = false;
	}

	public boolean isWaitFlag() {
		return waitFlag;
	}

	public void setWaitFlag(boolean waitFlag) {
		this.waitFlag = waitFlag;
	}

	/**
	 * 产生最终的随机数，产生最终值的原理：先随机生成初始值result,由preNum保存，
	 * 再随机产生运算符号(type),随机产生变化幅度值(interval)， result=result(type)interval;
	 */
	public void setRandom() {
		// 保存result
		this.preNum = this.result;
		// 产生参数变化幅度随机数
		this.interval = random.nextInt(5);
		// 产生运算符
		this.type = random.nextInt(20) > 8 ? 1 : -1;
		// 产生最终值，并设置其值在[5,80);
		this.result += (interval * type);
		if (this.result <= 5 || this.result > 80) {
			this.result = this.preNum;
		}
		// 设置server界面值
		this.randoms.add(this.result);
		if (this.randoms.size() == 7) {
			sf.info.setAirTemperature(randoms.get(0).toString());
			sf.info.setAirHumidity(randoms.get(1).toString());
			sf.info.setSoilTemperature(randoms.get(2).toString());
			sf.info.setSoilHumidity(randoms.get(3).toString());
			sf.info.setCo2(randoms.get(4).toString());
			sf.info.setLight(randoms.get(5).toString());
			this.randoms.removeAll(randoms);
		}

	}

	// 实现run方法，线程操作
	public void run() {
		while (true) {
			if (waitFlag) {
				this.setRandom();
			} else {
				pause();
			}
			try {
				Thread.sleep(TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	// 线程暂停
	public synchronized void pause() {
		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 线程唤醒
	public synchronized void awake() {
		notifyAll();
	}

}
