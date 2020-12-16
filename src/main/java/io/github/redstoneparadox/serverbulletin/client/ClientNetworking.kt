package io.github.redstoneparadox.serverbulletin.client

import io.github.redstoneparadox.serverbulletin.BulletinMessage
import io.github.redstoneparadox.serverbulletin.ServerBulletin
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.nbt.CompoundTag
import net.minecraft.text.Text

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

    fun addBulletin(title: Text, message: Text) {
        val buf = PacketByteBufs.create()
        buf.writeText(title)
        buf.writeText(message)
        ClientPlayNetworking.send(ServerBulletin.ADD_BULLETIN_PACKET, buf)
    }

    fun updateBulletin(index: Int, title: Text, message: Text) {
        val buf = PacketByteBufs.create()
        buf.writeInt(index)
        buf.writeText(title)
        buf.writeText(message)
        ClientPlayNetworking.send(ServerBulletin.UPDATE_BULLETIN_PACKET, buf)
    }

    fun deleteBulletin(index: Int) {
        val buf = PacketByteBufs.create()
        buf.writeInt(index)
        ClientPlayNetworking.send(ServerBulletin.DELETE_BULLETIN_PACKET, buf)
    }

    private fun onReceiveBulletins(bulletinTags: List<CompoundTag>) {
        bulletinTags.map { BulletinMessage(it) }
        bulletinReceiver = {}
    }
}