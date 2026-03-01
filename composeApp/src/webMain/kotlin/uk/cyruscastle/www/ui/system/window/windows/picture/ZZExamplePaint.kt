package uk.cyruscastle.www.ui.system.window.windows.picture

import androidx.compose.ui.geometry.Size
import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.paint

class ZZExamplePaint : PaintWindow(
    title = "example.png",
    startingResource = Res.drawable.paint,
    pictureIcon = true,
    resolution = Size(150f, 150f)
)