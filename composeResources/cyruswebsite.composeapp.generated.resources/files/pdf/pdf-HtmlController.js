// ================================
// Cursor Control Interop
// ================================
window.draggable = true;
function updateCursor() {
    const container = document.getElementById('pdf-container');
    if (!container) return;

    if (window.draggable) {
        container.style.cursor = 'grab';
    } else {
        container.style.cursor = 'default';
    }
}
Object.defineProperty(window, 'draggable', {
    set(value) {
        this._draggable = value;
        updateCursor();
    },
    get() {
        return this._draggable;
    }
});

// Initialize
window._draggable = true;
updateCursor();

// ================================
// HtmlScroller Interop
// ================================

window.NewHtmlScroller = (function () {
    const container = document.getElementById("pdf-container");

    function maxScroll() {
        return container
            ? Math.max(0, container.scrollHeight - container.clientHeight)
            : 0;
    }

    return {
        // a1. Current scroll (px)
        getCurrentScroll() {
            return container ? container.scrollTop : 0;
        },

        // a2. Max scroll (px)
        getMaxScroll() {
            return maxScroll();
        },

        // a3. Scroll by (px)
        scrollBy(amount) {
            if (!container) return;

            const top = Math.min(
                maxScroll(),
                Math.max(0, container.scrollTop + amount)
            );

            container.scrollTo({
                top,
                behavior: "auto"
            });
        },

        // a4. create listener
        addScrollListener(callback) {
            if (!container) return;

            container.addEventListener("scroll", callback);
        },

        removeScrollListener(callback) {
            if (!container) return;

            container.removeEventListener("scroll", callback);
        },
};
})();

window.NewHtmlScrollerHorizontal = (function () {
    const container = document.getElementById("pdf-container");

    function maxScroll() {
        return container
            ? Math.max(0, container.scrollWidth - container.clientWidth)
            : 0;
    }

    return {
        // h1. Current scroll (px)
        getCurrentScroll() {
            return container ? container.scrollLeft : 0;
        },

        // h2. Max scroll (px)
        getMaxScroll() {
            return maxScroll();
        },

        // h3. Scroll by (px)
        scrollBy(amount) {
            if (!container) return;

            const left = Math.min(
                maxScroll(),
                Math.max(0, container.scrollLeft + amount)
            );

            container.scrollTo({
                left,
                behavior: "auto"
            });
        },

        // h4. create listener
        addScrollListener(callback) {
            if (!container) return;

            container.addEventListener("scroll", callback);
        },

        removeScrollListener(callback) {
            if (!container) return;

            container.removeEventListener("scroll", callback);
        },
    };
})();

window.NewHtmlZoomer = (function () {
    return {
        // b1. Zoom in
        zoomIn() {
            window.zoomIn()
        },

        // b2. Zoom out
        zoomOut() {
            window.zoomOut()
        },

        // b3. Set zoom
        setZoom(amount){
            if (typeof window.setZoom === "function") {
              window.setZoom(x);
            } else {
              window.currentScale = amount;
            }
        },

        // b4. Get zoom
        getZoom() {
            return window.currentScale
        },

        getMaxZoom() {
            return window.currentScale
        },

        getMinZoom() {
            return window.currentScale
        },
    };
})();

window.NewHtmlCursorHandler = (function () {
    return {
        // c1. Get draggable
        getDraggable() {
            window.draggable ??= true

            return window.draggable;
        },

        // c2. Toggle draggable
        toggleDraggable() {
            window.draggable = !window.draggable;
        }
    };
})();

window.HtmlController = (function () {
    return {
        // x1. Scrollers
        getScroller() {
            return NewHtmlScroller;
        },
        getHorizontalScroller(){
            return NewHtmlScrollerHorizontal
        },

        // x2. Zoomer
        getZoomer() {
            return NewHtmlZoomer;
        },

        // x3. CursorHandler
        getCursorHandler() {
            return NewHtmlCursorHandler;
        },
    };
})();

// ================================
// DRAG TO SCROLL
// ================================
(function() {
    const container = document.getElementById("pdf-container");

    let isDown = false;
    let startY;
    let scrollTop;

    container.addEventListener("mousedown", (e) => {
        if (!window.draggable) return;

        isDown = true;
        container.classList.add("dragging");
        startY = e.pageY - container.offsetTop;
        scrollTop = container.scrollTop;
    });

    container.addEventListener("mouseleave", () => {
        isDown = false;
        container.classList.remove("dragging");
    });

    container.addEventListener("mouseup", () => {
        isDown = false;
        container.classList.remove("dragging");
    });

    container.addEventListener("mousemove", (e) => {
        if (!isDown) return;
        e.preventDefault();
        const y = e.pageY - container.offsetTop;
        const walk = (y - startY) * 1.5;
        container.scrollTop = scrollTop - walk;
    });
})();