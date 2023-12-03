package fr.deuspheara.callapp.core.model.channel

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.core.model.channel.ChannelVideo
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Video channel model
 *
 */
data class VideoChannel(
    val channelName: String,
    val creator: String,
    val numberOfParticipants: Int,
) {
    constructor() : this("", "", 0)
}