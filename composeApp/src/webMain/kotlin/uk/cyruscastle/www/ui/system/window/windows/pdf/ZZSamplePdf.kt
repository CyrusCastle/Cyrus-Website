package uk.cyruscastle.www.ui.system.window.windows.pdf

import uk.cyruscastle.www.model.CitationType
import uk.cyruscastle.www.model.PdfCitation
import uk.cyruscastle.www.ui.system.window.UniqueWindow

class ZZSamplePdf : UniqueWindow, PdfWindow(
    "PDF Viewer",
    "SamplePdf.pdf",
    pdfCitation = PdfCitation(
        type = CitationType.MISC,
        author = "Castle, C.",
        year = "2025",
        title = "Sample PDF"
    )
)