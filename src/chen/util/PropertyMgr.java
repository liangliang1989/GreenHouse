package chen.util;

import java.io.IOException;
import java.util.Properties;

public class PropertyMgr {

	private static Properties props=null;
	static{
		props=new Properties();
		try {
			props.load(PropertyMgr.class.getClassLoader().getResourceAsStream("config/server.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String getProperty(String key){
		return props.getProperty(key);
	}
}
