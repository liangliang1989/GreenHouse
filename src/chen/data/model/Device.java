package chen.data.model;

import java.util.List;

public class Device {

	private boolean window;
	private boolean fan;
	private boolean heater;
	private boolean humidifier;
	public Device() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void setDevice(List<Boolean>data){
	
		if(data.size()>=4){
			this.window=data.get(0);
			this.fan=data.get(1);
			this.heater=data.get(2);
			this.humidifier=data.get(3);
		}else {
			return;	
		}
		
	}
	
	public String toString() {
		return "device:---> [window=" + window + ", fan=" + fan + ", heater="
				+ heater + ", humidifier=" + humidifier + "]";
	}
	public String getWindow() {
		String s="Window:"+String.valueOf(this.window);
		return s;
	}
	public Device(boolean window, boolean fan, boolean heater,
			boolean humidifier) {
		super();
		this.window = window;
		this.fan = fan;
		this.heater = heater;
		this.humidifier = humidifier;
	}

	public void setWindow(boolean window) {
		this.window = window;
	}
	public String getFan() {
		String s="Fan:"+String.valueOf(this.fan);
		return s;
	}
	public void setFan(boolean fan) {
		this.fan = fan;
	}
	public String getHeater() {
		String s="Heater:"+String.valueOf(this.heater);
		return s;
	}
	public void setHeater(boolean heater) {
		this.heater = heater;
	}
	public String getHumidifier() {
		String s="Humidfier:"+String.valueOf(this.humidifier);
		return s;
	}
	public void setHumidifier(boolean humidifier) {
		this.humidifier = humidifier;
	}
}
