package io.github.redstoneparadox.serverbulletin.server

import io.github.redstoneparadox.serverbulletin.BulletinMessage
import io.github.redstoneparadox.serverbulletin.BulletinState
import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents
import net.minecraft.text.LiteralText
import net.minecraft.world.PersistentStateManager

object ServerBulletinServer: DedicatedServerModInitializer {
    override fun onInitializeServer() {
        ServerNetworking.initReceivers()

        ServerWorldEvents.LOAD.register { server, world ->
            createBulletinState(world.persistentStateManager)
        }
    }

    fun createBulletinState(manager: PersistentStateManager): BulletinState {
        val bulletinId = "serverbulletin"
        val state = manager.getOrCreate({BulletinState(bulletinId)}, bulletinId)
        if (!state.isInitialized()) {
            val bulletin = BulletinMessage(LiteralText("Welcome to Server Bulletin!"), LiteralText("Hello world!"))
            state.bulletins.add(bulletin)
            state.setInitialized()
            state.markDirty()
        }

        return state
    }
}