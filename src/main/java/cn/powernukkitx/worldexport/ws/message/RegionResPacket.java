package cn.powernukkitx.worldexport.ws.message;

import cn.nukkit.block.Block;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.level.format.ChunkSection;
import cn.nukkit.level.format.LevelProvider;
import cn.nukkit.level.format.anvil.Chunk;
import cn.nukkit.level.format.anvil.RegionLoader;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.utils.HumanStringComparator;
import cn.powernukkitx.worldexport.WorldExportPlugin;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

@Data
public class RegionResPacket extends AbstractPacket {
    private int pid = PacketID.REGION_RES_PACKET;
    private boolean state = true;
    private ArrayList<BlockData> chunkData = new ArrayList<>();
    private String error;

    public RegionResPacket(LevelProvider provider, int regionX, int regionZ) {
        super();
    }

    public static class BlockData {
        String name;
        int runtimeid;
        int x;
        int y;
        int z;
        Map<String, String> property = new TreeMap(HumanStringComparator.getInstance());
        public BlockData(Block block) {
            name = block.getPersistenceName();
            runtimeid = block.getRuntimeId();
            x = block.getFloorX();
            y = block.getFloorY()-64;
            z = block.getFloorZ();
            BlockProperties properties = block.getProperties();
            properties.getNames().forEach((name) -> {
                property.put(properties.getBlockProperty(name).getPersistenceName(), block.getPersistenceValue(name));
            });
        }
    }

    @Override
    public void processPackage() {
    }
}
