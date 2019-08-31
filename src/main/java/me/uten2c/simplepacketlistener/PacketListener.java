package me.uten2c.simplepacketlistener;

import io.netty.channel.Channel;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class PacketListener implements Listener {

    private Plugin plugin;

    static Map<PacketHandler, Player> channelMap = new HashMap<>();

    public PacketListener(Plugin plugin) {
        this.plugin = plugin;
    }

    public void listen(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        Channel channel = entityPlayer.playerConnection.networkManager.channel;
        PacketHandler handler = new PacketHandler();
        if (channel.pipeline().get(PacketHandler.class) == null) {
            channel.pipeline().addBefore("packet_handler", plugin.getName(), handler);
            channelMap.put(handler, player);
        }
    }

    public void remove(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        Channel channel = entityPlayer.playerConnection.networkManager.channel;
        if (channel.pipeline().get(PacketHandler.class) != null) {
            channel.pipeline().remove(PacketHandler.class);
            for (Map.Entry<PacketHandler, Player> entry : channelMap.entrySet()) {
                if (entry.getValue().equals(player)) {
                    channelMap.remove(entry.getKey());
                }
            }
        }
    }

    @EventHandler
    public void onEnable(PluginEnableEvent e) {
        if (e.getPlugin() == plugin) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                listen(player);
            }
        }
    }

    @EventHandler
    public void onDisable(PluginDisableEvent e) {
        if (e.getPlugin() == plugin) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                remove(player);
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        listen(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        remove(e.getPlayer());
    }
}
