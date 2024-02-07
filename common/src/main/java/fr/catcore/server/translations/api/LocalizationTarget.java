package fr.catcore.server.translations.api;

import org.jetbrains.annotations.Nullable;

import fr.catcore.server.translations.api.resource.language.ServerLanguage;
import xyz.nucleoid.packettweaker.PacketContext;

public interface LocalizationTarget {
    @Nullable
    static LocalizationTarget forPacket() {
        PacketContext context = PacketContext.get();
        return (LocalizationTarget) context.getTarget();
    }

    @Nullable
    String getLanguageCode();

    default ServerLanguage getLanguage() {
        return ServerTranslations.INSTANCE.getLanguage(this);
    }
}
