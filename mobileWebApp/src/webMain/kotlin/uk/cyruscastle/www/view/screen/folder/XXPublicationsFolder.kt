package uk.cyruscastle.www.view.screen.folder

import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.phoneFolderTwo
import uk.cyruscastle.www.controller.DEFAULT_MESSAGE_SHORT
import uk.cyruscastle.www.controller.HelpMessage
import uk.cyruscastle.www.view.screen.dynamic.pdf.PdfViewerApp

class XXPublicationsFolder : FolderViewer(
    name = "Publications",
    helpMessage = HelpMessage(
        "Publications",
        "A collection of publications that, hopefully, will grow."
    ),
    apps = listOf(ZZwje766()),
    icon = Res.drawable.phoneFolderTwo
)

class ZZwje766 : PdfViewerApp(
    pdfTitle = "Policy for GenAI in Wales.pdf",
    pdfFilePath = "wje766.pdf"
)