package net.hadences.particles.types;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;

import java.util.Locale;

public abstract class PlaneParticleEffect implements ParticleEffect {

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
    private final int delayTicks;

    // Constructor
    public PlaneParticleEffect(float yaw, float pitch, float roll,
                               float scale, boolean isStatic, float gravityStrength,
                               int maxAge, int color, int targetColor,
                               boolean repeat, int renderTypeOrdinal, int delayTicks, String behaviorIdentifier, int targetEntityIdentifier) {
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
        this.delayTicks = delayTicks;
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
        buf.writeVarInt(delayTicks);
        buf.writeString(behaviorIdentifier);
        buf.writeVarInt(targetEntityIdentifier);
    }

    public String asString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %b %.2f %d %d %d %b %d %s %d %d",
                Registries.PARTICLE_TYPE.getId(this.getType()),
                this.yaw, this.pitch, this.roll, this.scale, this.isStatic,
                this.gravityStrength, this.maxAge, this.color, this.targetColor,
                this.repeat,
                this.renderTypeOrdinal,
                this.behaviorIdentifier,
                this.targetEntityIdentifier,
                this.delayTicks
        );
    }

    public abstract ParticleType<? extends PlaneParticleEffect> getType();

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
    public int getDelayTicks() { return delayTicks; }

}
