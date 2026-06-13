package uk.cyruscastle.www.ui.system.window.windows.folders

import uk.cyruscastle.www.model.CitationType
import uk.cyruscastle.www.model.PdfCitation
import uk.cyruscastle.www.ui.system.window.windows.html.pdf.PdfWindow

class XXPapersFolder : FileExplorerWindow(
    title = "Publications",
    items = listOf(ZZwje766())
)

class ZZwje766 : PdfWindow(
    pdfTitle = "Policy for GenAI in Wales.pdf",
    pdfFilePath = "wje766.pdf",
    pdfCitation = PdfCitation(
        type = CitationType.JOURNAL,
        author = "Atherton, S., Karlinger, P. & Castle, C.",
        year = "2026",
        title = "Policy and Professional Development for Generative AI in Welsh Education",
        journal = "Wales Journal of Education",
        volume = "28",
        number = "1"
    )
)