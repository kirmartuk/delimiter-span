class ExampleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        val delimiter = " • "
        val words =
            listOf(
                "Highfalutin",
                "black-and-white",
                "Speed",
                "Premium",
                "Price",
                "Tiny"
            )

        val spannableString = setupSpans(words, delimiter)

        binding.title.text = spannableString

        setContentView(view)
    }

    private fun setupSpans(words: List<String>, delimiter: String): SpannableString {
        val spannableString = SpannableString(words.joinToString(delimiter))
        for (indexOfDelimiter in spannableString.getAllIndexes(delimiter)) {
            spannableString.setSpan(
                DelimiterReplacementSpan(),
                indexOfDelimiter,
                indexOfDelimiter + delimiter.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return spannableString
    }

    private fun CharSequence.getAllIndexes(substring: String): List<Int> {
        var lastIndex = 0
        val result: MutableList<Int> = ArrayList()

        while (lastIndex != -1) {
            lastIndex = this.indexOf(substring, lastIndex)
            if (lastIndex != -1) {
                result.add(lastIndex)
                lastIndex += 1
            }
        }
        return result
    }
}
