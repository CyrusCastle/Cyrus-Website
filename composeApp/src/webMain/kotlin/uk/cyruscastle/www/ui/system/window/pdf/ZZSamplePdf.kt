package uk.cyruscastle.www.ui.system.window.pdf

import uk.cyruscastle.www.model.CitationType
import uk.cyruscastle.www.model.PdfCitation

class ZZSamplePdf : PdfWindow(
    "PDF Viewer",
    "SamplePdf.pdf",
    pdfCitation = PdfCitation(
        type = CitationType.MISC,
        author = "Castle, C.",
        year = "2025",
        title = "Sample PDF"
    )
)