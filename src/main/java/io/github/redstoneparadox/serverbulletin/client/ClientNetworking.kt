package io.github.redstoneparadox.serverbulletin.client

import io.github.redstoneparadox.serverbulletin.BulletinMessage
import io.github.redstoneparadox.serverbulletin.ServerBulletin
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag

object ClientNetworking {
    private var bulletinReceiver: (List<BulletinMessage>) -> Unit = {}

    fun initReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(ServerBulletin.RECEIVE_BULLETINS_PACKET) { client, handler, buf, sender ->
            val lastIndex = buf.readByte()
            val bulletinTags = mutableListOf<CompoundTag>()

            for (i in 0..lastIndex) {
                bulletinTags.add(buf.readCompoundTag()!!)
            }

            onReceiveBulletins(bulletinTags)
        }
    }

    fun requestBulletins(receiver: (List<BulletinMessage>) -> Unit) {
        bulletinReceiver = receiver

        ClientPlayNetworking.send(ServerBulletin.REQUEST_BULLETINS_PACKET, PacketByteBufs.empty())
    }

    private fun onReceiveBulletins(bulletinTags: List<CompoundTag>) {
        bulletinTags.map { BulletinMessage(it) }
        bulletinReceiver = {}
    }
}