package net.hadences.particles.behaviors;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class SpecterParticleBehaviorRegistry {
    private static final HashMap<Identifier, SpecterParticleBehavior> behaviors = new HashMap<>();

    public static void register(Identifier identifier, SpecterParticleBehavior behavior){
        behaviors.put(identifier, behavior);
    }

    @Nullable
    public static SpecterParticleBehavior getBehavior(Identifier identifier){
        return behaviors.get(identifier);
    }
}
