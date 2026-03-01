package uk.cyruscastle.www.ui.system.window.windows.html.helpers

object URLChecker {
    private val validDomains = arrayOf(
        "http://localhost:8080",
        "https://codecymru.uk",
        "https://emilyprust.com",
        "https://web.archive.org/"
    )

    fun validateURL(url: String): String {
        return if (validDomains.any { url.startsWith(it) }){
            url
        }else{
            "http://localhost:8080/composeResources/cyruswebsite.composeapp.generated.resources/files/404/"
        }
    }
}