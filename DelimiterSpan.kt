
import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.ReplacementSpan
import kotlin.math.ceil

/**
 * DelimiterReplacementSpan - with "smart" drawing of the separator between words.
 * The delimiter will be drawn only between words,
 * if the delimiter is at the end or start of the line, then it will not be drawn
 */
class DelimiterReplacementSpan : ReplacementSpan() {

    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fontMetrics: Paint.FontMetricsInt?
    ): Int {
        if (fontMetrics != null) {
            paint.getFontMetricsInt(fontMetrics)
        }
        return ceil(paint.measureText(text, start, end)).toInt()
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val delimiter = text.substring(start, end)
        val indexOfNextDelimiter = text.indexOf(delimiter, end)

        val nextWord = if (indexOfNextDelimiter == INDEX_OF_NONE_EXISTING_DELIMITER) {
            text.substring(end, text.length)
        } else {
            text.substring(end, indexOfNextDelimiter)
        }
        // if a word is larger than the canvas, then it will be drawn to the full width of the canvas
        // + wrap the part of the word to a new line
        if (paint.measureText(nextWord) > canvas.width) return
        // measure width of next word
        val widthOfNextWordOnCanvas = x + paint.measureText(nextWord) + paint.measureText(delimiter)

        // check can we draw the next word on the current line or next word will drawn at start of canvas
        if (widthOfNextWordOnCanvas <= canvas.width && x != START_OF_CANVAS &&
            (x + paint.measureText(delimiter)) != canvas.width.toFloat()
        ) {
            canvas.drawText(delimiter, 0, delimiter.length, x, y.toFloat(), paint)
        }
    }

    companion object {
        private const val INDEX_OF_NONE_EXISTING_DELIMITER = -1
        private const val START_OF_CANVAS = 0.0f
    }
}
