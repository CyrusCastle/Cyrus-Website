package uk.cyruscastle.www.ui.system.window.windows.html.helpers

import uk.cyruscastle.www.ui.system.window.windows.html.edge.getHost

object URLDisguiser {
    private val disguises = mapOf(
        "${getHost()}/composeResources/cyruswebsite.shared.generated.resources/files/404" to "Page: Error",
        "${getHost()}/composeResources/cyruswebsite.shared.generated.resources/files/browser" to "https://engine.com",
        "${getHost()}/composeResources/cyruswebsite.shared.generated.resources/files/cawl" to "https://cawlfyth.ol"
    )

    fun disguiseURL(url: String): String {
        val (realPrefix, disguise) = disguises.entries.firstOrNull { (prefix, _) -> url.startsWith(prefix) } ?: return url

        return disguise + url.removePrefix(realPrefix)
    }

    fun undisguiseURL(url: String): String {
        val (realPrefix, disguise) = disguises.entries.firstOrNull { (_, disguise) -> url.startsWith(disguise) } ?: return url

        return realPrefix + url.removePrefix(disguise)
    }
}