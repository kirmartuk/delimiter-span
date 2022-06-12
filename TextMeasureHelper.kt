import android.graphics.Paint

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
    var currentX = 0
    var delimiterWidths = mutableMapOf<Int, Int>()

    private fun getWidthOfWord(word: CharSequence, paint: Paint): Float {
        return paint.measureText(word.toString())
    }

    fun isDelimiterPlacedInStartOrEndOfCanvas(delimiterWidth: Int): Boolean {
        return (currentX == 0
                || currentX + delimiterWidth > canvasSize.width
                )
    }

    fun initIfNeed(paint: Paint) {
        if (isInit) {
            val first = allWords[delimiterOrder]
            val firstWidth = getWidthOfWord(first, paint)
            currentX =
                ((firstWidth) % canvasSize.width).toInt()
            isInit = false
        }
    }
}
