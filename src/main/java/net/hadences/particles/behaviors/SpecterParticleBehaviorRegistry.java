package net.hadences.particles.behaviors;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class SpecterParticleBehaviorRegistry {
    private static final HashMap<String, SpecterParticleBehavior> behaviors = new HashMap<>();

    public static void register(String identifier, SpecterParticleBehavior behavior){
        behaviors.put(identifier, behavior);
    }

    @Nullable
    public static SpecterParticleBehavior getBehavior(String identifier){
        return behaviors.get(identifier);
    }
}
