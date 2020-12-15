package io.github.redstoneparadox.serverbulletin

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.world.PersistentState

class BulletinState(key: String) : PersistentState(key) {
    val bulletins: MutableList<BulletinMessage> = mutableListOf()
    private var initialized: Boolean = false

    fun setInitialized() {
        initialized = true
    }

    fun isInitialized(): Boolean {
        return initialized
    }

    override fun fromTag(tag: CompoundTag) {
        if (tag.contains("initialized")) {
            initialized = tag.getBoolean("initialized")
        }
        if (tag.contains("bulletins")) {
            val bulletinsTag = tag["bulletins"]

            if (bulletinsTag is ListTag) {
                for (bulletinTag in bulletinsTag) {
                    if (bulletinTag is CompoundTag) {
                        val bulletin = BulletinMessage()
                        bulletin.fromTag(bulletinTag)
                        bulletins.add(bulletin)
                    }
                }
            }
        }
    }

    override fun toTag(tag: CompoundTag): CompoundTag {
        val bulletinsTag = ListTag()
        bulletins.map { it.toTag(CompoundTag()) }.forEach { bulletinsTag.add(it) }
        tag.putBoolean("initialized", initialized)
        tag.put("bulletins", bulletinsTag)
        return tag
    }
}