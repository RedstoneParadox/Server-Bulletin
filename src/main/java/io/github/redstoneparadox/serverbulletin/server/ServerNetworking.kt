package io.github.redstoneparadox.serverbulletin.server

import io.github.redstoneparadox.serverbulletin.BulletinMessage
import io.github.redstoneparadox.serverbulletin.ServerBulletin
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.world.PersistentStateManager

object ServerNetworking {
    fun initReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(ServerBulletin.REQUEST_BULLETINS_PACKET) { server, player, handler, buf, sender ->
            val world = player.world

            if (world !is ServerWorld) throw Exception("Something has seriously gone wrong.")

            onRequestBulletins(player, world)
        }

        ServerPlayNetworking.registerGlobalReceiver(ServerBulletin.ADD_BULLETIN_PACKET) { server, player, handler, buf, sender ->
            onAddBulletin(player, (player.world as ServerWorld).persistentStateManager, buf.readText(), buf.readText())
        }

        ServerPlayNetworking.registerGlobalReceiver(ServerBulletin.UPDATE_BULLETIN_PACKET) { server, player, handler, buf, sender ->
            onUpdateBulletin(player, (player.world as ServerWorld).persistentStateManager, buf.readInt(), buf.readText(), buf.readText())
        }

        ServerPlayNetworking.registerGlobalReceiver(ServerBulletin.DELETE_BULLETIN_PACKET) { server, player, handler, buf, sender ->
            onDeleteBulletin(player, (player.world as ServerWorld).persistentStateManager, buf.readInt())
        }
    }

    private fun onRequestBulletins(player: ServerPlayerEntity, world: ServerWorld) {
        val state = ServerBulletinServer.getOrCreateBulletinState(world.persistentStateManager)
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

    private fun onAddBulletin(player: ServerPlayerEntity, manager: PersistentStateManager, title: Text, message: Text) {
        val state = ServerBulletinServer.getOrCreateBulletinState(manager)

        if (player.hasPermissionLevel(4)) {
            state.bulletins.add(BulletinMessage(title, message))
            state.markDirty()
        }
    }

    private fun onUpdateBulletin(player: ServerPlayerEntity, manager: PersistentStateManager, index: Int, title: Text, message: Text) {
        val state = ServerBulletinServer.getOrCreateBulletinState(manager)

        if (player.hasPermissionLevel(4)) {
            state.bulletins[index] = BulletinMessage(title, message)
            state.markDirty()
        }
    }

    private fun onDeleteBulletin(player: ServerPlayerEntity, manager: PersistentStateManager, index: Int) {
        val state = ServerBulletinServer.getOrCreateBulletinState(manager)

        if (player.hasPermissionLevel(4)) {
            state.bulletins.removeAt(index)
            state.markDirty()
        }
    }
}