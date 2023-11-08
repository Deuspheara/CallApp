package fr.deuspheara.callapp.core.event

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.core.event.MultipleEventCutter
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Multiple cutter event
 *
 */
internal interface MultipleEventsCutter {
    /**
     * Process the events with a limit of 500ms for each execution
     *
     * @param event the callback to the function to launch
     */
    fun processEvent(event: () -> Unit)

    companion object
}

/**
 * Implementation of [MultipleEventsCutter]
 */
object MultipleEventsCutterImpl : MultipleEventsCutter {
    private val now: Long
        get() = System.currentTimeMillis()

    private var lastEventTimeMs: Long = 0

    override fun processEvent(event: () -> Unit) {
        if (now - lastEventTimeMs >= 500L) {
            lastEventTimeMs = now
            event.invoke()
        }
    }
}