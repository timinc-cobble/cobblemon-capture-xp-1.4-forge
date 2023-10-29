package us.timinc.mc.cobblemon.capturexp

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.pokemon.experience.SidemodExperienceSource
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon
import net.minecraftforge.event.server.ServerStartedEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import us.timinc.mc.cobblemon.capturexp.config.CaptureXPConfig

@Mod(CaptureXP.MOD_ID)
object CaptureXP {
    const val MOD_ID = "capture_xp"
    private var captureXPConfig: CaptureXPConfig= CaptureXPConfig.Builder.load()

    @EventBusSubscriber(bus = EventBusSubscriber.Bus.FORGE)
    object Registration {
        @SubscribeEvent
        fun onInit(e: ServerStartedEvent) {
            CobblemonEvents.POKEMON_CAPTURED.subscribe { event ->
                val playerParty = Cobblemon.storage.getParty(event.player)
                val source = SidemodExperienceSource(MOD_ID)
                val targetPokemon = playerParty.firstOrNull { it != event.pokemon } ?: return@subscribe
                val experience = Cobblemon.experienceCalculator.calculate(
                    BattlePokemon.safeCopyOf(targetPokemon),
                    BattlePokemon.safeCopyOf(event.pokemon),
                    captureXPConfig.multiplier
                )
                targetPokemon.addExperienceWithPlayer(event.player, source, experience)
            }
        }
    }
}