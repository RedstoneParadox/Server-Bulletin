package io.github.redstoneparadox.serverbulletin

import net.minecraft.nbt.CompoundTag
import net.minecraft.text.LiteralText
import net.minecraft.text.Text

class BulletinMessage(var title: Text = LiteralText.EMPTY, var message: Text = LiteralText.EMPTY) {
    constructor(tag: CompoundTag): this() {
        fromTag(tag)
    }

    fun fromTag(tag: CompoundTag) {
        title = Text.Serializer.fromJson(tag.getString("title"))!!
        message = Text.Serializer.fromJson(tag.getString("message"))!!
    }

    fun toTag(tag: CompoundTag): CompoundTag {
        tag.putString("title", Text.Serializer.toJson(title))
        tag.putString("message", Text.Serializer.toJson(message))

        return tag
    }
}