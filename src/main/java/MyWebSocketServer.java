import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

@ServerEndpoint(value = "/websocket")
public class MyWebSocketServer {

    private Logger logger = Logger.getLogger(MyWebSocketServer.class);

    private Session session;
    /**
     * 连接建立后触发的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        logger.info("onOpen" + session.getId());
        System.out.println(this.getClass().getSimpleName() + "连接接入" + session.getId());

        WebSocketMapUtil.put(session.getId(), this);

        try {
            sendMessage(1, "连接成功", "", session);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 连接关闭后触发的方法
     */
    @OnClose
    public void onClose() {
//从map中删除
        WebSocketMapUtil.remove(session.getId());
        logger.info("====== onClose:" + session.getId() + " ======");
        System.out.println(this.getClass().getSimpleName() + "====== onClose:" + session.getId() + " ======");
        Set<Map.Entry<String, Session>> entries = People.getInstance().getSite().entrySet();
        Iterator<Map.Entry<String, Session>> iterator = entries.iterator();
        if (iterator.hasNext()) {
            Map.Entry<String, Session> next = iterator.next();
            if (next.getValue().getId().equals(session.getId())) {
                iterator.remove();
                return;
            }
        }
    }


    /**
     * 接收到客户端消息时触发的方法
     */
    @OnMessage
    public void onMessage(String params, Session session) throws Exception {
//获取服务端到客户端的通道

        upDateSession(params, session);

        if (params.contains("sender") && People.getInstance().getSite().containsKey("taker") && People.getInstance().getSite().get("taker") != null) {
            sendMessage(1, "content", params, People.getInstance().getSite().get("taker"));
            sendMessage(1, "发送命令成功", params, People.getInstance().getSite().get("sender"));
            System.out.println();
        }


//返回消息给Web Socket客户端（浏览器）
    }


    private boolean upDateSession(String params, Session session) {
        if (params != null && params.length() > 0) {
            if (params.contains("sender")) {
                if (!People.getInstance().getSite().containsKey("sender")) {
                    People.getInstance().getSite().put("sender", session);
                    try {
                        sendMessage(1, "注册成功sender", "", session);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Session send = People.getInstance().getSite().get("sender");
                    if (send.getId().equals(session.getId())) {

                    } else {
                        People.getInstance().getSite().put("sender", send);
                        try {
                            sendMessage(1, "sender更新", "", session);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                return true;
            }
            if (params.contains("taker")) {
                if (!People.getInstance().getSite().containsKey("taker")) {
                    People.getInstance().getSite().put("taker", session);
                    try {
                        sendMessage(1, "注册成功taker", "", session);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Session send = People.getInstance().getSite().get("taker");
                    if (send.equals(session.getId())) {

                    } else {
                        People.getInstance().getSite().put("taker", send);
                        try {
                            sendMessage(1, "taker更新", "", session);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return true;
            }

        }
        return false;
    }


    /**
     * 发生错误时触发的方法
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.info(session.getId() + "连接发生错误" + error.getMessage());
        error.printStackTrace();

    }

    public void sendMessage(int status, String message, Object datas, Session session) throws IOException {

        JSONObject result = new JSONObject();
        result.put("status", status);
        result.put("message", message);
        result.put("datas", datas);
        MyWebSocketServer myWebSocketServer = WebSocketMapUtil.get(session.getId());
        if (myWebSocketServer != null) {
            myWebSocketServer.session.getBasicRemote().sendText(result.toString());
            System.out.println(this.getClass().getSimpleName() + "下发消息" + result.toString());
        } else {
            System.out.println(this.getClass().getSimpleName() + "下发消息失败");

        }


    }

}