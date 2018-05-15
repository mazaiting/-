package com.mazaiting;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * 趣味聊天的服务端程序
 * 
 * @author mazaiting
 */
// 声明WebSocket服务器的地址
@ServerEndpoint("/chatServer")
public class ChatServer {
	/** 是否是第一次进入 */
	private boolean isFirstFlag = true;
	private Session session;
	/** 用户名 */
	private String userName;
	/**
	 * 记录此次聊天室的服务端有多少个连接 key代表此次客户端的Session ID，value代表此次连接对象
	 */
	private static final HashMap<String, Object> connectMap = new HashMap<>();
	/**
	 * 保存所有用户的昵称信息 key是Session ID，value才是用户名
	 */
	private static final HashMap<String, String> userMap = new HashMap<>();

	/**
	 * 服务端收到客户端连接请求，连接成功后会执行此方法
	 * 
	 * @param session
	 */
	@OnOpen
	public void start(Session session) {
		this.session = session;
		connectMap.put(session.toString(), this);
	}

	/**
	 * 发送消息
	 * 
	 * @param clientMessage
	 *            消息/用户名
	 * @param session
	 *            会话
	 */
	@OnMessage
	public void chat(String clientMessage, Session session) {
		// 消息
		String message;
		// 判断是否为第一次发送消息
		if (isFirstFlag) {
			// 用户名赋值
			this.userName = clientMessage;
			// 将新进来的用户保存到用户map
			userMap.put(session.getId(), userName);
			// 构造发送给客户端的提示信息
			message = htmlMessage("系统消息", userName + "进入聊天室");
			// 输入昵称后，代表isFirstFlag=false
			isFirstFlag = false;
		} else {
			// 构造发送给客户端的提示信息
			message = htmlMessage(userMap.get(session.getId()), clientMessage);
		}
		sendMessageForAll(message);
	}

	/**
	 * ws.close()方法调用后，会触发后台的标注OnClose方法
	 * 
	 * @param session
	 */
	@OnClose
	public void close(Session session) {
		// 当某个用户退出时，对其他用户进行广播
		String message = htmlMessage("系统消息", userMap.get(session.getId()) + "退出了聊天室");
		// 用户map移除
		userMap.remove(session.getId());
		// 链接map移除
		connectMap.remove(session.getId());
		sendMessageForAll(message);
	}

	/**
	 * 格式化消息
	 * @param userName 用戶名
	 * @param message 消息
	 * @return 消息
	 */
	private String htmlMessage(String userName, String message) {
		StringBuffer messageBuffer = new StringBuffer();
		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		messageBuffer.append("<div class='record_item'>");
		messageBuffer.append("<p class='record_item_time'>");
		messageBuffer.append("<span>" + sFormat.format(new Date()) + "</span>");
		messageBuffer.append("</p>");
		messageBuffer.append("<div class='record_item_txt'>");
		messageBuffer.append("<span class='avatar'>" + userName + "</span>");
		messageBuffer.append("<p>");
		messageBuffer.append("<span class='txt'>" + message + "</span>");
		messageBuffer.append("</p>");
		messageBuffer.append("</div>");
		messageBuffer.append("</div>");
		return messageBuffer.toString();
	}

	/**
	 * 为所有用户发送消息
	 * 
	 * @param message
	 *            消息
	 */
	private void sendMessageForAll(String message) {
		// 当前对象
		ChatServer client = null;
		// 将消息广播给所有人
		for (String connectKey : connectMap.keySet()) {
			// 获取客户端
			client = (ChatServer) connectMap.get(connectKey);
			// 给对应的web端发送一个文本信息
			try {
				client.session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
			;
		}
	}
	
}
