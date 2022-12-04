package by.bulba.watch.elektronika.editor.root

import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.wear.watchface.editor.EditorSession

internal object WatchSettingsRootHolder {
    private var editorSession: EditorSession? = null

    fun init(activity: ComponentActivity) {
        activity.lifecycleScope.launchWhenCreated {
            editorSession = EditorSession.createOnWatchEditorSession(activity = activity)
        }
    }

    fun getEditorSession(): EditorSession = requireNotNull(editorSession)
}
