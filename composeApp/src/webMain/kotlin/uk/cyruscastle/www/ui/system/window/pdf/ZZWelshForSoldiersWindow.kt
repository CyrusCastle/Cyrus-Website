package uk.cyruscastle.www.ui.system.window.pdf

import uk.cyruscastle.www.model.CitationType
import uk.cyruscastle.www.model.PdfCitation

class ZZWelshForSoldiersWindow : PdfWindow(
    pdfTitle = "welsh for the discerning soldier.pdf",
    pdfFilePath = "wfds.pdf",
    pdfCitation = PdfCitation(
        type = CitationType.MISC,
        author = "Castle, C.",
        year = "2025",
        title = "Welsh for the Discerning Soldier"
    )
)