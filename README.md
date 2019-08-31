# SimplePacketListener
らくにBukkitのパケットに割り込むやつ
改変も配布も自由にしてくれていいです。

## 使い方 / How to use
返り値を`true`にするとパケットをキャンセルします。
```java
  //クライアントが受け取るパケット
  PacketHandler.addSendListener(it -> {
    return false;
  });
  
  //サーバーが受け取るパケット
  PacketHandler.addReceiveListener(it -> {
    return false;
  });
```

## サンプル / Example
「entity.player.attack.nodamage」と「entity.player.attack.knockback」の音を再生するパケットをキャンセルしています
```java
import me.uten2c.simplepacketlistener.PacketHandler;
import net.minecraft.server.v1_14_R1.MinecraftKey;
import net.minecraft.server.v1_14_R1.PacketPlayOutNamedSoundEffect;
import net.minecraft.server.v1_14_R1.SoundEffect;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public final class SimplePacketListenerTest extends JavaPlugin {

    @Override
    public void onEnable() {
        PacketHandler.addSendListener(it -> {
            Object packet = it.getPacket();
            if (packet instanceof PacketPlayOutNamedSoundEffect) {
                PacketPlayOutNamedSoundEffect soundPacket = (PacketPlayOutNamedSoundEffect)packet;
                try {
                    switch (getSoundName(soundPacket)) {
                        case "entity.player.attack.nodamage":
                        case "entity.player.attack.knockback":
                            return true;
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return false;
        });
    }

    private SoundEffect getSoundEffect(PacketPlayOutNamedSoundEffect packet) throws NoSuchFieldException, IllegalAccessException {
        Field a = packet.getClass().getDeclaredField("a");
        a.setAccessible(true);
        SoundEffect soundEffect = (SoundEffect) a.get(packet);
        a.setAccessible(false);
        return soundEffect;
    }

    private MinecraftKey getMinecraftKey(SoundEffect soundEffect) throws NoSuchFieldException, IllegalAccessException {
        Field a = soundEffect.getClass().getDeclaredField("a");
        a.setAccessible(true);
        MinecraftKey mcKey = (MinecraftKey) a.get(soundEffect);
        a.setAccessible(false);
        return mcKey;
    }

    private String getSoundName(PacketPlayOutNamedSoundEffect packet) throws NoSuchFieldException, IllegalAccessException {
        return getMinecraftKey(getSoundEffect(packet)).toString().replace("minecraft:", "");
    }
}
```
