package uk.cyruscastle.www.view.screen.folder

import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.phoneFolderTwo
import uk.cyruscastle.www.controller.DEFAULT_MESSAGE_SHORT
import uk.cyruscastle.www.controller.HelpMessage
import uk.cyruscastle.www.view.screen.dynamic.pdf.PdfViewerApp

class XXMiscDocsFolder : FolderViewer(
    name = "Misc",
    helpMessage = HelpMessage(
        "Misc Files",
        "A collection of random .pdfs and other documents that may, but probably won't, be of interest."
    ),
    apps = listOf(ZZCVPdfWindow(), ZZWelshForSoldiersWindow()),
    icon = Res.drawable.phoneFolderTwo
)

class ZZWelshForSoldiersWindow : PdfViewerApp(
    pdfTitle = "welsh for the discerning soldier.pdf",
    pdfFilePath = "wfds.pdf"
)

class ZZCVPdfWindow : PdfViewerApp(
    pdfTitle = "Cyrus CV.pdf",
    pdfFilePath = "cv.pdf"
)