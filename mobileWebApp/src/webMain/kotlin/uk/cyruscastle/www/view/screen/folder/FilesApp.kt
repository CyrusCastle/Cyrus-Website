package uk.cyruscastle.www.view.screen.folder

import uk.cyruscastle.www.controller.DEFAULT_MESSAGE_SHORT
import uk.cyruscastle.www.controller.HelpMessage

class FilesApp : FolderViewer(
    name = "Files",
    helpMessage = HelpMessage(
        "Files",
        "A folder containing folders of shortcuts or documents of interest."
    ),
    apps = listOf(/*XXFriendFolder(),*/ XXGithubFolder(), XXPublicationsFolder(), XXMiscDocsFolder())
)