package uk.cyruscastle.www.ui.system.window.windows.pdf

import uk.cyruscastle.www.model.CitationType
import uk.cyruscastle.www.model.PdfCitation
import uk.cyruscastle.www.ui.system.window.UniqueWindow

class ZZCVPdfWindow : UniqueWindow, PdfWindow(
    pdfTitle = "Cyrus CV.pdf",
    pdfFilePath = "cv.pdf",
    pdfCitation = PdfCitation(
        type = CitationType.MISC,
        author = "Castle, C.",
        year = "2025",
        title = "Curriculum Vitae"
    )
)