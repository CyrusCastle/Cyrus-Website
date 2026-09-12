package uk.cyruscastle.www.view.screen.folder

import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.phoneFolderTwo
import uk.cyruscastle.www.view.screen.dynamic.pdf.PdfViewerApp

class XXMiscDocsFolder : FolderViewer(
    name = "Misc",
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