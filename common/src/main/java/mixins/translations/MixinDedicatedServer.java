package mixins.translations;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import dev.architectury.platform.Platform;
import net.minecraft.network.chat.Component;
import net.minecraft.server.dedicated.DedicatedServer;

@Mixin(DedicatedServer.class)
public class MixinDedicatedServer {
    @ModifyArg(method = "initServer", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;info(Ljava/lang/String;Ljava/lang/Object;)V", ordinal = 0, remap = false), index = 0)
    private String translated_startingServer(String string) {
        return Component.translatable("text.translated_server.starting_version",
                string.replace("Starting minecraft server version ", "")).getString();
    }

    @ModifyArg(method = "initServer", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;info(Ljava/lang/String;)V", ordinal = 0, remap = false), index = 0)
    private String translated_loadingProperties(String string) {
        return Component.translatable("text.translated_server.loading.properties").getString();
    }

    @ModifyArg(method = "initServer", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;info(Ljava/lang/String;Ljava/lang/Object;)V", ordinal = 1, remap = false), index = 0)
    private String translated_gamemode(String string, Object p0) {
        return Component.translatable("text.translated_server.loading.gamemode", p0).getString();
    }

    @ModifyArg(method = "initServer", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;info(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", ordinal = 0, remap = false), index = 0)
    private String translated_ip(String string, Object p0, Object p1) {
        return Component.translatable("text.translated_server.loading.ip", p0, p1).getString();
    }

    @ModifyArg(method = "initServer", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;info(Ljava/lang/String;Ljava/lang/Object;)V", ordinal = 2, remap = false), index = 0)
    private String translated_preparingLevel(String string, Object p0) {
        return Component.translatable("text.translated_server.preparing.level", p0).getString();
    }

    @ModifyArg(method = "initServer", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;info(Ljava/lang/String;Ljava/lang/Object;)V", ordinal = 3, remap = false), index = 0)
    private String translated_done(String string, Object p0) {
        if (!Platform.isDevelopmentEnvironment()) {
            return Component.translatable("text.translated_server.done", p0).getString();
        } else
            return string;
    }
}
