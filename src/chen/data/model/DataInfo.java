/**
 * 温室环境中数据封装类
 */
package chen.data.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataInfo {

	private String airTemperature; // 空气温度
	private String airHumidity; // 空气湿度
	private String soilTemperature; // 土壤温度
	private String soilHumidity; // 土壤湿度
	private String co2; // co2浓度
	private String light; // 关照强度
	private String time; // 时间

	public DataInfo() {
		super();
	}

	public DataInfo(String airTemperature, String airHumidity,
			String soilTemperature, String soilHumidity, String co2,
			String light) {
		super();
		this.airTemperature = airTemperature;
		this.airHumidity = airHumidity;
		this.soilTemperature = soilTemperature;
		this.soilHumidity = soilHumidity;
		this.co2 = co2;
		this.light = light;
	}

	public String getAirTemperature() {
		return airTemperature;
	}

	public void setData(List<String> data) {

		this.airTemperature = data.get(0);
		this.airHumidity = data.get(1);
		this.soilTemperature = data.get(2);
		this.soilHumidity = data.get(3);
		this.co2 = data.get(4);
		this.light = data.get(5);

	}

	public String getData() {
		this.time = this.getTime();
		String str = this.time + "/" + this.airTemperature + "/"
				+ this.airHumidity + "/" + this.soilTemperature + "/"
				+ this.soilHumidity + "/" + this.co2 + "/" + this.light;
		return str;
	}

	@Override
	public String toString() {
		return "set data:---> [airTemperature=" + airTemperature + ", airHumidity="
				+ airHumidity + ", soilTemperature=" + soilTemperature
				+ ", soilHumidity=" + soilHumidity + ", co2=" + co2
				+ ", light=" + light + "]";
	}

	public void setAirTemperature(String airTemperature) {
		this.airTemperature = airTemperature;
	}

	public String getAirHumidity() {
		return airHumidity;
	}

	public void setAirHumidity(String airHumidity) {
		this.airHumidity = airHumidity;
	}

	public String getSoilTemperature() {
		return soilTemperature;
	}

	public void setSoilTemperature(String soilTemperature) {
		this.soilTemperature = soilTemperature;
	}

	public String getSoilHumidity() {
		return soilHumidity;
	}

	public void setSoilHumidity(String soilHumidity) {
		this.soilHumidity = soilHumidity;
	}

	public String getCo2() {
		return co2;
	}

	public void setCo2(String co2) {
		this.co2 = co2;
	}

	public String getLight() {
		return light;
	}

	public void setLight(String light) {
		this.light = light;
	}

	// 获取当前时间
	public static String getTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("",
				Locale.SIMPLIFIED_CHINESE);
		sdf.applyPattern("HH:mm:ss:");
		String timeStr = sdf.format(new Date());
		return timeStr;
	}
}
