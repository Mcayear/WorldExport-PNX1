package cn.powernukkitx.worldexport.ws;

import cn.nukkit.Server;
import cn.nukkit.level.format.LevelProvider;
import cn.nukkit.plugin.PluginLogger;
import cn.powernukkitx.worldexport.WorldExportPlugin;
import cn.powernukkitx.worldexport.ws.message.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.websocket.OnOpen;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.UUID;

@ServerEndpoint("/")
public class WebSocketServerEndpoint {
    private static PluginLogger logger = WorldExportPlugin.INSTANCE.getLogger();
    // 记录连接的客户端
    private static Map<String, Session> clients = new ConcurrentHashMap<>();

    private String sid;

    private LevelProvider levelProvider;

    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.sid = UUID.randomUUID().toString();
        clients.put(this.sid, session);

        logger.info(this.sid + " 加入了连接！");

        WorldListResPacket packet = new WorldListResPacket();

        session.getBasicRemote().sendText(packet.toJsonString());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        logger.info("Received message: " + message);
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        switch (jsonObject.get("pid").getAsInt()) {
            case PacketID.WORLD_INFO_REQUEST_PACKET: {
                WorldInfoResPacket packet = new WorldInfoResPacket(null);
                String worldName = jsonObject.get("world_name").getAsString();
                if (Server.getInstance().loadLevel(worldName)) {
                    levelProvider = Server.getInstance().getLevelByName(worldName).getProvider();
                    packet = new WorldInfoResPacket(levelProvider);
                }
                session.getBasicRemote().sendText(packet.toJsonString());
                break;
            }
            case PacketID.REGION_REQUEST_PACKET: {
                HandleRegionRequest.start(session, levelProvider, jsonObject.get("region_x").getAsInt(), jsonObject.get("region_z").getAsInt());
                break;
            }
        }
    }

    @OnClose
    public void onClose() {
        logger.info(this.sid + "连接断开！");
        clients.remove(this.sid);
    }

    @OnError
    public void onError(Throwable t) {
        logger.error("WebSocket error: " + t.getMessage());
    }
}