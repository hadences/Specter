package net.hadences.particles.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.hadences.Specter;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;

import java.util.Locale;

public class PlaneParticleEffect implements ParticleEffect {

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

            return new PlaneParticleEffect(yaw, pitch, roll, scale, isStatic, gravityStrength, maxAge, color, targetColor, repeat, renderTypeOrdinal, behaviorIdentifier, targetEntityIdentifier);
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
    ).apply(instance, PlaneParticleEffect::new));

    // Fields for the particle effect
    private final float yaw;
    private final float pitch;
    private final float roll;
    private final float scale;
    private final boolean isStatic;
    private final float gravityStrength;
    private final int maxAge;
    private final int color;
    private final int targetColor;
    private final boolean repeat;
    private final int renderTypeOrdinal;
    private final String behaviorIdentifier;
    private final int targetEntityIdentifier;

    // Constructor
    public PlaneParticleEffect(float yaw, float pitch, float roll,
                               float scale, boolean isStatic, float gravityStrength,
                               int maxAge, int color, int targetColor,
                               boolean repeat, int renderTypeOrdinal, String behaviorIdentifier, int targetEntityIdentifier) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
        this.scale = scale;
        this.isStatic = isStatic;
        this.gravityStrength = gravityStrength;
        this.maxAge = maxAge;
        this.color = color;
        this.targetColor = targetColor;
        this.repeat = repeat;
        this.renderTypeOrdinal = renderTypeOrdinal;
        this.behaviorIdentifier = behaviorIdentifier;
        this.targetEntityIdentifier = targetEntityIdentifier;
    }

    // Serialization methods
    public void write(PacketByteBuf buf) {
        buf.writeFloat(this.yaw);
        buf.writeFloat(this.pitch);
        buf.writeFloat(this.roll);
        buf.writeFloat(this.scale);
        buf.writeBoolean(this.isStatic);
        buf.writeFloat(this.gravityStrength);
        buf.writeVarInt(this.maxAge);
        buf.writeVarInt(this.color);
        buf.writeVarInt(this.targetColor);
        buf.writeBoolean(this.repeat);
        buf.writeVarInt(this.renderTypeOrdinal);

        buf.writeString(behaviorIdentifier);
        buf.writeVarInt(targetEntityIdentifier);

        Specter.LOGGER.info("Server packet size: " + buf.writerIndex());
    }

    public String asString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %b %.2f %d %d %d %b %d %s %d",
                Registries.PARTICLE_TYPE.getId(this.getType()),
                this.yaw, this.pitch, this.roll, this.scale, this.isStatic,
                this.gravityStrength, this.maxAge, this.color, this.targetColor,
                this.repeat,
                this.renderTypeOrdinal,
                this.behaviorIdentifier,
                this.targetEntityIdentifier
        );
    }

    public ParticleType<PlaneParticleEffect> getType() {
        return SpecterParticleTypes.PLANE;
    }

    // Getter methods for each field
    public float getYaw() { return yaw; }
    public float getPitch() { return pitch; }
    public float getRoll() { return roll; }
    public float getScale() { return scale; }
    public boolean isStatic() { return isStatic; }
    public float getGravityStrength() { return gravityStrength; }
    public int getMaxAge() { return maxAge; }
    public int getColor() { return color; }
    public int getTargetColor() { return targetColor; }
    public boolean isRepeat() { return repeat; }
    public int getRenderTypeOrdinal() { return renderTypeOrdinal; }
    public String getBehaviorIdentifier() { return behaviorIdentifier; }
    public int getTargetEntityIdentifier() { return targetEntityIdentifier; }

}
