package uk.cyruscastle.www.ui.system.window.windows.html.helpers

@OptIn(ExperimentalWasmJsInterop::class)
external interface HtmlController : JsAny {
    fun getScroller(): NewHtmlScroller
    fun getHorizontalScroller(): NewHtmlScroller
    fun getZoomer() : NewHtmlZoomer
    fun getCursorHandler(): NewHtmlCursorHandler
}

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("""
(id, callback) => {
    function findIframe() {
        const host = document.querySelector("body");
        if (!host) return null;

        const root = host.shadowRoot;
        if (!root) return null;

        return root.getElementById(id);
    }

    async function waitForZoomer(controller) {
        while (true) {
//            if (window.setZoom === undefined) {
//                await new Promise(requestAnimationFrame);
//                continue;
//            }
        
            try {
                const zoomer = controller.getZoomer?.();
                if (zoomer?.setZoom && zoomer?.getZoom !== undefined) {
                    return zoomer;
                }
            } catch {}
            await new Promise(requestAnimationFrame);
        }
    }

    let currentController = null;
    
    async function monitor() {
        while (true) {
            const iframe = findIframe();
        
            // Skip until we find a HtmlController
            if (!iframe?.contentWindow?.HtmlController){
                console.log("!no controller at all");
                await new Promise(requestAnimationFrame);
                continue;
            }
            
            // If we haven't got one yet, set our current controller
            if (!currentController) {
                currentController = iframe.contentWindow.HtmlController;
                callback(currentController);
                console.log("!first controller");
                await new Promise(requestAnimationFrame);
                continue;
            }
            
            // If we have got a controller, let's see if we have the right one
            const newController = iframe.contentWindow.HtmlController;
            if (newController === currentController){
                callback(currentController);
                console.log("!same controller as before");
                await new Promise(requestAnimationFrame);
                continue;
            }
            
            // We don't have the right one, so let's get some details
            const currentZoom = currentController.getZoomer().getZoom();
            
            // Let's send these details to the new controller in the future
//            iframe.contentWindow.addEventListener(
//                "pdf-ready",
//                () => {
//                    if (currentZoom !== undefined){
//                        currentController.getZoomer().setZoom(currentZoom);
//                    }
//                },
//                { once: true }
//            );
            
            // Finally let's set the new controller & callback
            currentController = newController;
            callback(currentController);
            
            (async () => {
                const zoomer = await waitForZoomer(currentController);

                if (currentZoom !== undefined) {
                    console.log("trying to set a zoom");
                    while (true) {
                        try {
                            console.log("- complete set zoom");
                            zoomer.setZoom(currentZoom);
                            break;
                        } catch {
                            console.log("- FAILED set zoom");
                        } finally {
                            await new Promise(requestAnimationFrame);
                        }
                    }
                    console.log("zoom loop complete");
                }
            })();
            
            await new Promise(requestAnimationFrame);
        }
    }
 
    monitor();
}
""")
external fun subscribeToHtmlController(id: String, callback: (HtmlController) -> Unit)