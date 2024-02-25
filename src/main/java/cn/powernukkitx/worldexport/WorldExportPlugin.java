package cn.powernukkitx.worldexport;

import cn.nukkit.Server;
import cn.nukkit.lang.PluginI18n;
import cn.nukkit.lang.PluginI18nManager;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import cn.powernukkitx.worldexport.config.MainConfig;
import cn.powernukkitx.worldexport.ws.WebSocketServerEndpoint;
import jakarta.websocket.DeploymentException;

/**
 * author: Mcayear
 */
public class WorldExportPlugin extends PluginBase {
    public static WorldExportPlugin INSTANCE;
    public static PluginI18n I18N;

    public static org.glassfish.tyrus.server.Server server;


    @Override
    public void onLoad() {
        //save Plugin Instance
        INSTANCE = this;
        //register the plugin i18n
        I18N = PluginI18nManager.register(this);
        //register the command of plugin
        //this.getServer().getCommandMap().register("exampleplugin", new WorldExportCommand());

        this.getLogger().info(TextFormat.WHITE + "I've been loaded!");
    }

    @Override
    public void onEnable() {
        //Use the plugin's i18n output
        this.getLogger().info(I18N.tr(Server.getInstance().getLanguageCode(), "exampleplugin.helloworld", "世界"));

        //PluginTask
        //this.getServer().getScheduler().scheduleDelayedRepeatingTask(new BroadcastPluginTask(this), 500, 200);

        //Save resources
        this.saveResource("config.yml");
        MainConfig.init(new Config(this.getDataFolder()+"/config.yml", Config.YAML));

        if (MainConfig.server) {
            server = new org.glassfish.tyrus.server.Server(MainConfig.ip, MainConfig.port, "/worldexport", null, WebSocketServerEndpoint.class);

            try {
                server.start();
                this.getLogger().info("WebSocket Server started on port " + MainConfig.port + ".");
                // Keep the server running
                // Thread.currentThread().join();
            } catch (DeploymentException e) {
                this.getLogger().error("Failed to start WebSocket Server: " + e.getMessage());
            }
        }
    }

    @Override
    public void onDisable() {
        if (server != null) {
            server.stop();
        }
        this.getLogger().info(TextFormat.DARK_RED + "I've been disabled!");
    }
}
