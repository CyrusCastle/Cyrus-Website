package uk.cyruscastle.www.ui.system.window.windows.picture

import androidx.compose.ui.geometry.Size
import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.paint
import uk.cyruscastle.www.ui.system.window.UniqueWindow

class ZZExamplePaint : UniqueWindow, PaintWindow(
    title = "example.png",
    startingResource = Res.drawable.paint,
    pictureIcon = true,
    resolution = Size(150f, 150f)
)