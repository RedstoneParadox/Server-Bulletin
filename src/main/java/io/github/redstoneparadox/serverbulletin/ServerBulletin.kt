package io.github.redstoneparadox.serverbulletin

import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier

object ServerBulletin: ModInitializer {
    val MODID = "serverbulletin"
    // Packet IDs
    val REQUEST_BULLETINS_PACKET = Identifier(MODID, "request_bulletin_messages")
    val RECEIVE_BULLETINS_PACKET = Identifier(MODID, "receive_bulletin_messages")

    override fun onInitialize() {
        println("Hello world!")
    }
}