package io.github.redstoneparadox.serverbulletin.server

import io.github.redstoneparadox.serverbulletin.ServerBulletin
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld

object ServerNetworking {
    fun initReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(ServerBulletin.REQUEST_BULLETINS_PACKET) { server, player, handler, buf, sender ->
            val world = player.world

            if (world !is ServerWorld) throw Exception("Something has seriously gone wrong.")

            onRequestBulletins(player, world)
        }
    }

    private fun onRequestBulletins(player: ServerPlayerEntity, world: ServerWorld) {
        val state = ServerBulletinServer.createBulletinState(world.persistentStateManager)
        val bulletinTags = state.bulletins.map { it.toTag(CompoundTag()) }

        if (bulletinTags.size > 256) {
            throw Exception()
        }

        val buf = PacketByteBufs.create()
        buf.writeByte(bulletinTags.size - 1)

        bulletinTags.forEach {
            buf.writeCompoundTag(it)
        }

        ServerPlayNetworking.send(player, ServerBulletin.RECEIVE_BULLETINS_PACKET, buf)
    }
}