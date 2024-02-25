package cn.powernukkitx.worldexport;

import cn.nukkit.scheduler.PluginTask;

/**
 * author: MagicDroidX
 * ExamplePlugin Project
 */
public class BroadcastPluginTask extends PluginTask<WorldExportPlugin> {

    public BroadcastPluginTask(WorldExportPlugin owner) {
        super(owner);
    }

    @Override
    public void onRun(int currentTick) {
        this.getOwner().getLogger().info("I've run on tick " + currentTick);
    }
}
