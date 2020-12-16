package io.github.redstoneparadox.serverbulletin

import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier

object ServerBulletin: ModInitializer {
    val MODID = "serverbulletin"
    // Packet IDs
    val REQUEST_BULLETINS_PACKET = id("request_bulletin_messages")
    val RECEIVE_BULLETINS_PACKET = id("receive_bulletin_messages")
    val ADD_BULLETIN_PACKET = id("add_bulletin_message")

    override fun onInitialize() {
        println("Hello world!")
    }

    fun id(path: String): Identifier {
        return Identifier(MODID, path)
    }
}