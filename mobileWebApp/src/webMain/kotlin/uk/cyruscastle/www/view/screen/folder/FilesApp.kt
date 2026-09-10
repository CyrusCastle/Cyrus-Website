package uk.cyruscastle.www.view.screen.folder

class FilesApp : FolderViewer(
    name = "Files",
    apps = listOf(XXFriendFolder(), XXGithubFolder(), XXPublicationsFolder(), XXMiscDocsFolder())
)