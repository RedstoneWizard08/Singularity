package fr.catcore.server.translations.api.resource.language;

import java.util.Collection;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.google.common.base.Supplier;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public final class TranslationStore {
    private final Map<String, TranslationMap> translations = new Object2ObjectOpenHashMap<>();
    private final Multimap<String, Supplier<TranslationMap>> translationSuppliers = HashMultimap.create();

    public void clear() {
        this.translationSuppliers.clear();
        this.translations.clear();
    }

    public void add(String code, Supplier<TranslationMap> supplier) {
        TranslationMap translations = this.translations.get(code);
        
        if (translations != null) {
            TranslationMap map = supplier.get();

            if (map != null) {
                translations.putAll(map);
            }
        } else {
            this.translationSuppliers.put(code, supplier);
        }
    }

    @NotNull
    public TranslationMap get(String code) {
        TranslationMap translations = this.translations.get(code);

        if (translations == null) {
            translations = this.tryLoad(code);
        }

        return translations;
    }

    @NotNull
    private TranslationMap tryLoad(String code) {
        Collection<Supplier<TranslationMap>> suppliers = this.translationSuppliers.removeAll(code);
        
        if (suppliers.isEmpty()) {
            return new TranslationMap();
        }

        TranslationMap translations = new TranslationMap();

        this.translations.put(code, translations);

        for (Supplier<TranslationMap> supplier : suppliers) {
            TranslationMap map = supplier.get();

            if (map != null) {
                translations.putAll(map);
            }
        }

        return translations;
    }
}
