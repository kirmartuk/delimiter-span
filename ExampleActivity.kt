
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import ru.yandex.spannabletest.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val delimiter = " • "

        val words = listOf(
            "1aaaaaaaaaaaaaaaaaaaaaaaaaaa1",
            "1bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb 1",
            "1cc1",
            "1dd1",
            "1ffffffffff1",
            "1eeeee1",
            "1qqqqqqqqqqq1",
            "1zzzzzzz1",
            "1xxxxxxx1",
            "1ddddd1",
            "1dddddd1",
            "1ddddd1",
            "1ddddddd1",
        )

        binding.title.doOnPreDraw {
            (it as TextView).text = setupSpans(words, delimiter, CanvasSize(it.width, it.height))
        }
    }


    private fun setupSpans(
        words: List<String>,
        delimiter: String,
        canvasSize: CanvasSize
    ): SpannableString {
        val str = createStringWithDelimiter(words, delimiter)
        val spannableString = SpannableString(str.first)
        val textHelper = TextMeasureHelper(canvasSize, words)
        for (indexOfDelimiter in str.second) {
            spannableString.setSpan(
                DelimiterReplacementSpan(textHelper),
                indexOfDelimiter,
                indexOfDelimiter + delimiter.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return spannableString
    }

    private fun createStringWithDelimiter(
        words: List<String>,
        delimiter: String
    ): Pair<String, List<Int>> {
        var string = ""
        val delimiterIndexes = mutableListOf<Int>()
        words.forEachIndexed { index, s ->
            if (index != 0) {
                delimiterIndexes.add(string.length)
                string += delimiter
            }
            string += s
        }
        return string to delimiterIndexes
    }
}

data class CanvasSize(
    val width: Int,
    val height: Int,
)
