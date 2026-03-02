package uk.cyruscastle.www.ui.system.window.windows.picture

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.decodeToImageBitmap
import coil3.compose.AsyncImage
import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.buttonNew
import cyruswebsite.composeapp.generated.resources.buttonOpen
import cyruswebsite.composeapp.generated.resources.buttonPrint
import cyruswebsite.composeapp.generated.resources.buttonSave
import cyruswebsite.composeapp.generated.resources.externalViewer
import cyruswebsite.composeapp.generated.resources.paint
import cyruswebsite.composeapp.generated.resources.picture
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.download
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.w3c.dom.url.URL
import uk.cyruscastle.www.controller.WindowController
import uk.cyruscastle.www.ui.system.window.FacsimileWindow
import uk.cyruscastle.www.ui.system.window.topbar.TopBarEntry
import uk.cyruscastle.www.ui.system.window.topbar.WindowTopBarButtons
import uk.cyruscastle.www.ui.system.window.windows.shortcuts.openShortcut

open class ImageWindow(
    title: String,
    path: String,
    private var _imageTitle: MutableStateFlow<String> = MutableStateFlow(title),
    private var _imageURI: MutableStateFlow<String?> = MutableStateFlow(Res.getUri(path)),
    private var _imageFile: MutableStateFlow<PlatformFile?> = MutableStateFlow(null)
) : FacsimileWindow(
    programTitle = "Imaging",
    icon = Res.drawable.picture,
    initiallyVisible = true,
    topBarContent = listOf(
//        { WindowTopBarMenus() },
        { WindowTopBarButtons (
            { TopBarEntry(Res.drawable.buttonNew, false) {
                _imageURI.value = null
                _imageFile.value = null
            } },
            { TopBarEntry(Res.drawable.buttonOpen, false) {
                CoroutineScope(Dispatchers.Default).launch {
                    val file = FileKit.openFilePicker(FileKitType.Image)

                    _imageURI.value = null
                    _imageFile.value = file
                    _imageTitle.value = file?.name ?: ""
                }
            } },
            { TopBarEntry(Res.drawable.buttonSave, false) {
                CoroutineScope(Dispatchers.Default).launch {
                    if (_imageURI.value != null){
                        FileKit.download(bytes = Res.readBytes(path), fileName = _imageTitle.value)
                    }

                    _imageFile.value?.readBytes()?.let { bytes ->
                        FileKit.download(bytes = bytes, fileName = _imageTitle.value)
                    }
                }
            } },

            { TopBarEntry(null, false) { } },

            { TopBarEntry(Res.drawable.buttonPrint, false) {
                if (_imageURI.value != null){
                    printImage(_imageURI.value!!)
                }

                if (_imageFile.value != null){
                    printImage(_imageFile.value!!.asURL())
                }
            } },

            { TopBarEntry(null, false) { } },

            { TopBarEntry(Res.drawable.paint, false) {
                CoroutineScope(Dispatchers.Default).launch {
                    val bytes =
                        if (_imageURI.value != null) Res.readBytes(path)
                        else if (_imageFile.value != null) _imageFile.value!!.readBytes()
                        else null

                    if (bytes == null) return@launch

                    val bitmap = bytes.decodeToImageBitmap()

                    WindowController.addWindow(
                        window = object : PaintWindow(
                            title = _imageTitle.value,
                            startingBitmap = bitmap,
                            resolution = Size(bitmap.width.toFloat(), bitmap.height.toFloat())
                        ) {}
                    )
                }
            } },

            { TopBarEntry(Res.drawable.externalViewer, false) {
                if (_imageURI.value != null){
                    openShortcut(_imageURI.value!!)
                }

                if (_imageFile.value != null){
                    openShortcut(_imageFile.value!!.asURL())
                }
            } },

            // If I wanted to waste the time, i could add zoom in/out and scrolling to this image window... see https://saket.github.io/telephoto/zoomableimage/#sharing-hoisted-state
//            { TopBarEntry(null) { } },
//
//            { TopBarEntry(Res.drawable.zoomIn, false) {
//
//            } },
//
//            { TopBarEntry(Res.drawable.zoomOut, false) {
//
//            } },
        )}
    ),
    content = content@{
        val uri by _imageURI.collectAsState()
        val file by _imageFile.collectAsState()

        if (uri != null){
            AsyncImage(
                model = uri,
                contentDescription = title,
                modifier = Modifier.fillMaxSize()
            )
        }else if (file != null){
            var bytes by remember(file) { mutableStateOf<ByteArray?>(null) }

            LaunchedEffect(file){
                bytes = file!!.readBytes()
            }

            AsyncImage(
                model = bytes,
                contentDescription = title,
                modifier = Modifier.fillMaxSize()
            )
        }
    },
    bottomBarContent = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val imageTitle by _imageTitle.collectAsState()

            Text(text = imageTitle)
        }
    }
)

fun PlatformFile.asURL() = URL.createObjectURL(file)

@OptIn(ExperimentalWasmJsInterop::class)
fun printImage(url: String){
js("""
const iframe = document.createElement('iframe');
iframe.style.position = 'fixed';
iframe.style.right = '0';
iframe.style.bottom = '0';
iframe.style.width = '0';
iframe.style.height = '0';
iframe.style.border = '0';

document.body.appendChild(iframe);

iframe.onload = function () {
    iframe.contentWindow.focus();
    iframe.contentWindow.print();

    // Cleanup after printing
    setTimeout(() => {
        document.body.removeChild(iframe);
    }, 1000);
};

iframe.src = url;
""")
}