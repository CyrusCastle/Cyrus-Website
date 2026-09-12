package uk.cyruscastle.www.helpers.html

import kotlinx.browser.window

object URLChecker {
    private val validDomains = arrayOf(
        "http://localhost:8080",
        "https://codecymru.uk",
        "https://cyruscastle.com",
        "https://emilyprust.com",
        "https://web.archive.org/",
        getHost()
    )

    fun validateURL(url: String): String {
        return if (validDomains.any { url.startsWith(it) }){
            url
        }else{
            "${getHost()}/composeResources/cyruswebsite.shared.generated.resources/files/404/"
//            "https://cyruscastle.com/composeResources/cyruswebsite.shared.generated.resources/files/404/"
//            "http://localhost:8080/composeResources/cyruswebsite.shared.generated.resources/files/404/"
        }
    }
}

fun getHost(): String = window.location.origin