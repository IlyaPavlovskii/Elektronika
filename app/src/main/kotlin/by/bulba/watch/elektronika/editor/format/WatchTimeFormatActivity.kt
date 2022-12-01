package by.bulba.watch.elektronika.editor.format

import android.os.Bundle
import androidx.activity.ComponentActivity
import by.bulba.watch.elektronika.R
import by.bulba.watch.elektronika.databinding.ActivityWatchTimeFormatBinding

internal class WatchTimeFormatActivity : ComponentActivity(R.layout.activity_watch_time_format) {

    private val binding: ActivityWatchTimeFormatBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityWatchTimeFormatBinding.inflate(this.layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}