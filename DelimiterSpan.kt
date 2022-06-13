
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
        delimiterWidth = ceil(paint.measureText(text, start, end)).toInt()
        if (fontMetrics != null) {
            textMeasureHelper.executeCount++
            paint.getFontMetricsInt(fontMetrics)

            textMeasureHelper.initIfNeed(
                paint.measureText(
                    textMeasureHelper.allWords.first().toString()
                ).toInt()
            )

            val word =
                textMeasureHelper.allWords.getOrNull(textMeasureHelper.delimiterOrder + 1)
                    ?: return delimiterWidth

            val isDelimiterAtStartOrEnd =
                textMeasureHelper.isDelimiterPlacedInStartOrEndOfCanvas(delimiterWidth)

            if (isDelimiterAtStartOrEnd) {
                delimiterWidth = 0
                textMeasureHelper.currentX =
                    paint.measureTexWithDelimiterWidth(word, delimiterWidth)
            } else {
                if (paint.isTextBiggerThanCanvas(word, delimiterWidth)) {
                    delimiterWidth = 0
                    textMeasureHelper.currentX = paint.measureBigText(word)
                } else {
                    textMeasureHelper.currentX += paint.measureTexWithDelimiterWidth(
                        word,
                        delimiterWidth
                    )
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
        if (textMeasureHelper.delimiterWidths[start] != 0) {
            canvas.drawText(text, start, end, x, y.toFloat(), paint)
        }
    }

    private fun Paint.measureTexWithDelimiterWidth(text: CharSequence, delimiterWidth: Int): Int {
        return (this.measureText(
            text.toString()
        ).toInt() + delimiterWidth) % textMeasureHelper.canvasSize.width
    }

    private fun Paint.measureBigText(word: CharSequence): Int {
        val canvasWidth = textMeasureHelper.canvasSize.width
        if (this.measureText(word.toString()) <= canvasWidth) {
            return this.measureText(word.toString()).toInt()
        }
        var tmpWidth = 0
        for (char in word) {
            val measuredWidthOfChar = this.measureText(
                char.toString()
            ).toInt()
            if (tmpWidth + measuredWidthOfChar > canvasWidth) {
                tmpWidth = measuredWidthOfChar
            } else {
                tmpWidth += measuredWidthOfChar
            }
        }
        return tmpWidth
    }

    /**
     * Check if (current position (x) + delimiter width + width of word) bigger than width of canvas
     */
    private fun Paint.isTextBiggerThanCanvas(
        text: CharSequence,
        delimiterWidth: Int,
    ): Boolean {
        return textMeasureHelper.currentX + delimiterWidth + this.measureText(
            text.toString(),
        ) > textMeasureHelper.canvasSize.width
    }
}
