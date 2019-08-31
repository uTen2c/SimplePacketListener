package me.uten2c.simplepacketlistener;

import org.bukkit.plugin.java.JavaPlugin;

public final class SimplePacketListener extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PacketListener(this), this);
    }
}
