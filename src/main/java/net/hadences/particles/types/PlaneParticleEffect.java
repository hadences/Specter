package net.hadences.particles.types;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;

import java.util.Locale;

public class PlaneParticleEffect implements ParticleEffect {


    // Define the Codec for serialization and deserialization
    public static final Codec<PlaneParticleEffect> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
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

    @SuppressWarnings("deprecation")
    public static final ParticleEffect.Factory<PlaneParticleEffect> FACTORY = new ParticleEffect.Factory<>() {
        public PlaneParticleEffect read(ParticleType<PlaneParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            float yaw = stringReader.readFloat();
            float pitch = stringReader.readFloat();
            float roll = stringReader.readFloat();
            float scale = stringReader.readFloat();
            boolean isStatic = stringReader.readBoolean();
            float gravityStrength = stringReader.readFloat();
            int maxAge = stringReader.readInt();
            int color = stringReader.readInt();
            int targetColor = stringReader.readInt();
            boolean repeat = stringReader.readBoolean();
            int renderTypeOrdinal = stringReader.readInt();
            String behaviorIdentifier = stringReader.readString();
            int targetEntityIdentifier = stringReader.readInt();

            return new PlaneParticleEffect(yaw, pitch, roll, scale, isStatic, gravityStrength, maxAge, color, targetColor, repeat, renderTypeOrdinal, behaviorIdentifier, targetEntityIdentifier);
        }

        public PlaneParticleEffect read(ParticleType<PlaneParticleEffect> particleType, PacketByteBuf packetByteBuf) {
            float yaw = packetByteBuf.readFloat();
            float pitch = packetByteBuf.readFloat();
            float roll = packetByteBuf.readFloat();
            float scale = packetByteBuf.readFloat();
            boolean isStatic = packetByteBuf.readBoolean();
            float gravityStrength = packetByteBuf.readFloat();
            int maxAge = packetByteBuf.readVarInt();
            int color = packetByteBuf.readVarInt();
            int targetColor = packetByteBuf.readVarInt();
            boolean repeat = packetByteBuf.readBoolean();
            int renderTypeOrdinal = packetByteBuf.readVarInt();
            String behaviorIdentifier = packetByteBuf.readString();
            int targetEntityIdentifier = packetByteBuf.readVarInt();

            return new PlaneParticleEffect(yaw, pitch, roll, scale, isStatic, gravityStrength, maxAge, color, targetColor, repeat, renderTypeOrdinal, behaviorIdentifier, targetEntityIdentifier);
        }
    };

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
        buf.writeInt(targetEntityIdentifier);
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
