package net.hadences;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.hadences.particles.RotationalParticle;
import net.hadences.particles.behaviors.SpecterParticleBehaviorRegistry;
import net.hadences.particles.behaviors.presets.MoveParticleBehavior;
import net.hadences.particles.behaviors.presets.NoneParticleBehavior;
import net.hadences.particles.types.SpecterParticleTypes;
import net.hadences.particles.util.SpecterParticleUtils;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Specter implements ModInitializer {
	public static final String MOD_ID = "specter";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final String NONE_BEHAVIOR = "none";
	public static final String MOVE_BEHAVIOR = "move_behavior";

	@Override
	public void onInitialize() {
		SpecterParticleTypes.init();
		registerCommands();
		registerParticleBehaviors();
	}

	private void registerCommands(){
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CommandManager.literal("test").executes(this::testCommand));
		});
	}

	/**
	 * registers all particle behaviors into registry.
	 */
	private void registerParticleBehaviors(){
		SpecterParticleBehaviorRegistry.register(MOVE_BEHAVIOR, new MoveParticleBehavior());
		SpecterParticleBehaviorRegistry.register(NONE_BEHAVIOR, new NoneParticleBehavior());
	}

	private int testCommand(CommandContext<ServerCommandSource> context) {
		ServerPlayerEntity player = context.getSource().getPlayer();
		if(player == null) return 0;
		player.sendMessage(Text.literal("spawned particle"));

		ServerWorld world = player.getServerWorld();
		Vec3d pos = player.getEyePos().add(player.getRotationVector());
		SpecterParticleUtils.spawnPlaneParticle(
				world,
				pos.x,
				pos.y,
				pos.z,
				1,
				0.0,
				0.0,
				0.0f,
				0.0f,
				-player.getYaw(),
				player.getPitch(),
				0.0f,
				1.0f,
				1000,
				true,
				0.0f,
				0xffffff,
				0xffffff,
				true,
				RotationalParticle.RenderType.BILLBOARD,
				Specter.MOVE_BEHAVIOR,
				player.getId()
		);


		return 1;
	}
}
