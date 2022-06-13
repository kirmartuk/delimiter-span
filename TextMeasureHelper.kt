/**
 * Helper for DelimiterReplacementSpan
 * store canvas size, delimiterOrder, current position on canvas and delimiter widths where the key is the starting index in thе whole SpannableString
 */
class TextMeasureHelper(
    val canvasSize: CanvasSize,
    val allWords: List<CharSequence>
) {
    var delimiterOrder = 0
    var isInit = true
    var currentX = 0 // position X in canvas
    var delimiterWidths = mutableMapOf<Int, Int>()

    fun initIfNeed(widthOfFirstWord: Int) {
        if (isInit) {
            currentX = ((widthOfFirstWord) % canvasSize.width)
            isInit = false
        }

    }

    fun isDelimiterPlacedInStartOrEndOfCanvas(delimiterWidth: Int): Boolean {
        return (currentX == 0
                || currentX + delimiterWidth > canvasSize.width
                )
    }
}
