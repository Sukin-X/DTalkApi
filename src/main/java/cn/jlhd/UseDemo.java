package cn.jlhd;
import cn.jlhd.dtalk.login.AuthHelper;
import cn.jlhd.util.RandomStringUtil;

public class UseDemo {
	//请填入相应信息
	static String AGENT_ID = "agent_id";
	static String CORP_ID = "corp_id";
	static String CORP_SECRET = "corp_secret";
	public static void main(String[] args) {
		//1.获取AccessToken
		String accessToken = AuthHelper.getAccessToken(CORP_ID, CORP_SECRET);
		//2.获取jsapi的鉴权ticket
		String ticket = AuthHelper.getJsapiTicket(accessToken);
		//3.获取前段JS的config初始化时所需要的sign签名
		String nonceStr = RandomStringUtil.getCode(10, 6);
		long timeStamp = System.currentTimeMillis();
		String url = "需要初始化dd.config的前端页面url";
		String sign = AuthHelper.sign(ticket, nonceStr, timeStamp, url);
		//4.获取用户信息
		String code = "你的js页所传来的临时授权code";
		AuthHelper.getUserInfo(code, accessToken);
		//5.获取用户详细信息
		String userid = "用户的USERID";
		AuthHelper.getUser(userid, accessToken);
	}
}
