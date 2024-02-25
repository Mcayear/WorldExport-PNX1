package cn.powernukkitx.worldexport.ws.message;

import cn.nukkit.Server;
import cn.nukkit.level.format.LevelProvider;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;

@Data
public class WorldInfoRequestPacket extends AbstractPacket {
    private int pid = PacketID.WORLD_INFO_REQUEST_PACKET;
    private boolean state = true;
    public String world_name;

    public WorldInfoRequestPacket(String worldName) {
        super();
        world_name = worldName;
    }

    @Override
    public void processPackage() {
    }
}
