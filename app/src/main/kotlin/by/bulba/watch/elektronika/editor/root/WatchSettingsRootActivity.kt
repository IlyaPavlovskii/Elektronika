package by.bulba.watch.elektronika.editor.root

import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.FragmentActivity
import by.bulba.watch.elektronika.R

internal class WatchSettingsRootActivity : FragmentActivity(R.layout.activity_watch_settings_root) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WatchSettingsRootHolder.init(this)
    }
}
