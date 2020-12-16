package io.github.redstoneparadox.serverbulletin.client

import io.github.redstoneparadox.serverbulletin.ServerBulletin
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs

object ClientNetworking {
    fun initReceivers() {

    }

    fun requestBulletins() {
        ClientPlayNetworking.send(ServerBulletin.REQUEST_BULLETINS_PACKET, PacketByteBufs.empty())
    }
}