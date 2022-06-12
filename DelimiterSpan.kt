
import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.ReplacementSpan
import kotlin.math.ceil

/**
 * DelimiterReplacementSpan - with "smart" drawing of the separator between words.
 * The delimiter will be drawn only between words,
 * if the delimiter is at the end or start of the line, then it will not be drawn
 */
class DelimiterReplacementSpan(
    private val textMeasureHelper: TextMeasureHelper
) : ReplacementSpan() {

    private var delimiterWidth = 0

    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fontMetrics: Paint.FontMetricsInt?
    ): Int {
        /**
         * Measure width of word with delimiter width
         */
        fun measureWordWidth(word: CharSequence): Int {
            return (paint.measureText(word.toString())
                .toInt() + delimiterWidth) % textMeasureHelper.canvasSize.width
        }

        /**
         * Check if (current position (x) + delimiter width + width of word) bigger than width of canvas
         */
        fun isWordBiggerThanCanvas(
            word: CharSequence,
            delimiterWidth: Int,
            paint: Paint
        ): Boolean {
            return textMeasureHelper.currentX + delimiterWidth + paint.measureText(word.toString())
                .toInt() > textMeasureHelper.canvasSize.width
        }

        delimiterWidth = ceil(paint.measureText(text, start, end)).toInt()

        if (fontMetrics != null) {
            paint.getFontMetricsInt(fontMetrics)
            textMeasureHelper.initIfNeed(paint)
            val word = textMeasureHelper.allWords[textMeasureHelper.delimiterOrder + 1]

            // if delimiter placed at start or end of canvas then we shouldn't draw it
            val isDelimiterAtStartOrEnd =
                textMeasureHelper.isDelimiterPlacedInStartOrEndOfCanvas(delimiterWidth)
            if (isDelimiterAtStartOrEnd) {
                delimiterWidth = 0
                textMeasureHelper.currentX =
                    measureWordWidth(word)
            } else {
                if (isWordBiggerThanCanvas(word, delimiterWidth, paint)) {
                    delimiterWidth = 0
                    textMeasureHelper.currentX = measureWordWidth(word)
                } else {
                    textMeasureHelper.currentX += measureWordWidth(word)
                }
            }
            textMeasureHelper.delimiterOrder++
            textMeasureHelper.delimiterWidths[start] = delimiterWidth
            if (isDelimiterAtStartOrEnd) {
                return 0
            }
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
        if ((textMeasureHelper.delimiterWidths[start] ?: 0) > 0) {
            canvas.drawText(text, start, end, x, y.toFloat(), paint)
        }
    }
}
