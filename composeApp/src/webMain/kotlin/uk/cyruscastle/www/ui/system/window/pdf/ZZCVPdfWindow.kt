package uk.cyruscastle.www.ui.system.window.pdf

import uk.cyruscastle.www.model.CitationType
import uk.cyruscastle.www.model.PdfCitation

class ZZCVPdfWindow : PdfWindow(
    pdfTitle = "Cyrus CV.pdf",
    pdfFilePath = "cv.pdf",
    pdfCitation = PdfCitation(
        type = CitationType.MISC,
        author = "Castle, C.",
        year = "2025",
        title = "Curriculum Vitae"
    )
)