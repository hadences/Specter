package net.hadences.particles.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.particle.ParticleType;

public class TestParticleEffect extends PlaneParticleEffect{

    public static PacketCodec<RegistryByteBuf, PlaneParticleEffect> getPacketCodec() { return PACKET_CODEC; }
    public static MapCodec<PlaneParticleEffect> getCodec() {
        return MapCodec.assumeMapUnsafe(CODEC);
    }

    private static final PacketCodec<RegistryByteBuf, PlaneParticleEffect> PACKET_CODEC = new PacketCodec<>() {
        @Override
        public void encode(RegistryByteBuf buf, PlaneParticleEffect value) {
            value.write(buf);
        }

        @Override
        public PlaneParticleEffect decode(RegistryByteBuf buf) {
            float yaw = buf.readFloat();
            float pitch = buf.readFloat();
            float roll = buf.readFloat();
            float scale = buf.readFloat();
            boolean isStatic = buf.readBoolean();
            float gravityStrength = buf.readFloat();
            int maxAge = buf.readVarInt();
            int color = buf.readVarInt();
            int targetColor = buf.readVarInt();
            boolean repeat = buf.readBoolean();
            int renderTypeOrdinal = buf.readVarInt();
            String behaviorIdentifier = buf.readString();
            int targetEntityIdentifier = buf.readVarInt();

            return new TestParticleEffect(yaw, pitch, roll, scale, isStatic, gravityStrength, maxAge, color, targetColor, repeat, renderTypeOrdinal, behaviorIdentifier, targetEntityIdentifier);
        }
    };
    private static final Codec<PlaneParticleEffect> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.FLOAT.fieldOf("yaw").forGetter(PlaneParticleEffect::getYaw),
            Codec.FLOAT.fieldOf("pitch").forGetter(PlaneParticleEffect::getPitch),
            Codec.FLOAT.fieldOf("roll").forGetter(PlaneParticleEffect::getRoll),
            Codec.FLOAT.fieldOf("scale").forGetter(PlaneParticleEffect::getScale),
            Codec.BOOL.fieldOf("isStatic").forGetter(PlaneParticleEffect::isStatic),
            Codec.FLOAT.fieldOf("gravityStrength").forGetter(PlaneParticleEffect::getGravityStrength),
            Codec.INT.fieldOf("maxAge").forGetter(PlaneParticleEffect::getMaxAge),
            Codec.INT.fieldOf("color").forGetter(PlaneParticleEffect::getColor),
            Codec.INT.fieldOf("targetColor").forGetter(PlaneParticleEffect::getTargetColor),
            Codec.BOOL.fieldOf("repeat").forGetter(PlaneParticleEffect::isRepeat),
            Codec.INT.fieldOf("renderType").forGetter(PlaneParticleEffect::getRenderTypeOrdinal),
            Codec.STRING.fieldOf("behaviorIdentifier").forGetter(PlaneParticleEffect::getBehaviorIdentifier),
            Codec.INT.fieldOf("targetEntityIdentifier").forGetter(PlaneParticleEffect::getTargetEntityIdentifier)
    ).apply(instance, TestParticleEffect::new));

    public TestParticleEffect(float yaw, float pitch, float roll, float scale, boolean isStatic, float gravityStrength, int maxAge, int color, int targetColor, boolean repeat, int renderTypeOrdinal, String behaviorIdentifier, int targetEntityIdentifier) {
        super(yaw, pitch, roll, scale, isStatic, gravityStrength, maxAge, color, targetColor, repeat, renderTypeOrdinal, behaviorIdentifier, targetEntityIdentifier);
    }

    @Override
    public ParticleType<? extends PlaneParticleEffect> getType() {
        return SpecterParticleTypes.SPECTER_TEST;
    }
}
