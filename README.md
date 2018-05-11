DTalkApi 说明  
> 可以引入maven项目自行增加开发使用  
> 也可以打成jar包引用  
> 也可以拷贝类进项目用

### 一. login相关
#### 1.获取accessToken  
	 参数: `corpID`,`corpSecert`
	 使用: `AuthHelper.getAccessToken(CORP_ID, CORP_SECRET);`
	 结果: 相应的accessToken值

#### 2.获取jsapi中的ticket
	 参数: `accessToken` 
	 使用: `AuthHelper.getJsapiTicket(accessToken);`
	 结果: 相应的ticket值

#### 3.获取jsapi中初始化dd.config所需要的sign签名
	 参数: `ticket`[^1], `nonceStr`[^2], `timeStamp`[^3], `url`[^4]
	 使用: `AuthHelper.sign(ticket, nonceStr, timeStamp, url);`
	 结果: 一个签名后的字符串

---

#### 4.获取用户基本信息,如userid
	 参数: `code`[^5] , `accessToken`
	 使用: `AuthHelper.getUser(code, accessToken);`
	 结果: 一个json格式的字符串,里面包含了相关的用户基本信息

#### 5.获取用户详细信息(ISV版本)
	 参数: `userid` , `accessToken`
	 使用: `AuthHelper.getUser(userid, accessToken);`
	 结果: 一个json格式的字符串,里面包含了相关的用户基本信息




UseDemo.java
````
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

````
---


[^1]:  `ticket` 是上面所生成的tickect  
[^2]:  `nonceStr` 为随机生成的字符串  
[^3]:  `timeStamp` 时间戳  
[^4]:  **`url`** 这个url,是前端页面需要进行dd.config初始化权限的那个url  
[^5]:  这里的code是前端页面调用jssdk所传入后台的一个code