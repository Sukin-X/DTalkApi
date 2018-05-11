package cn.jlhd.util;

public class ReturnUtil {
	
	public static String result(String ret,String msg) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"ret\":\"").append(ret).append("\",");
		sb.append("\"msg\":\"").append(msg).append("\"");
		sb.append("}");
		return sb.toString();
	}
}
