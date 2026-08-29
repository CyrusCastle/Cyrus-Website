// ================================
// Cursor Control Interop
// ================================
window.draggable = false;
function updateCursor() {
//    const container = document.getElementById('page-container');
//    if (!container) return;
//    container.style.cursor = window.draggable ? 'grab' : 'default';
}
Object.defineProperty(window, 'draggable', {
    set(value) {
//        this._draggable = value;
//        updateCursor();
    },
    get() {
//        return this._draggable;
        return false;
    }
});
window._draggable = false;
updateCursor();

// ================================
// HtmlScroller Interop
// ================================
window.NewHtmlScroller = (function () {
    const container = document.getElementById("page-container");
    const listeners = new Set();

    function maxScroll() {
        return container ? Math.max(0, container.scrollHeight - container.clientHeight) : 0;
    }

    function notify() {
            listeners.forEach((callback) => callback());
    }

    if (container) {
        container.addEventListener("scroll", notify);
        new ResizeObserver(notify).observe(container);
    }

    window.addEventListener("resize", notify);

    return {
        getCurrentScroll() { return container ? container.scrollTop : 0; },
        getMaxScroll() { return maxScroll(); },
        scrollBy(amount) {
            if (!container) return;
            const top = Math.min(maxScroll(), Math.max(0, container.scrollTop + amount));
            container.scrollTo({ top, behavior: "auto" });
        },
        addScrollListener(callback) { listeners.add(callback); },
        removeScrollListener(callback) { listeners.delete(callback); },
    };
})();

window.NewHtmlScrollerHorizontal = (function () {
    const container = document.getElementById("page-container");
    const listeners = new Set();

    function maxScroll() {
        return container ? Math.max(0, container.scrollWidth - container.clientWidth) : 0;
    }

    function notify() {
        listeners.forEach((callback) => callback());
    }

    if (container) {
        container.addEventListener("scroll", notify);
        new ResizeObserver(notify).observe(container);
    }

    window.addEventListener("resize", notify);

    return {
        getCurrentScroll() { return container ? container.scrollLeft : 0; },
        getMaxScroll() { return maxScroll(); },
        scrollBy(amount) {
            if (!container) return;
            const left = Math.min(maxScroll(), Math.max(0, container.scrollLeft + amount));
            container.scrollTo({ left, behavior: "auto" });
        },
        addScrollListener(callback) { listeners.add(callback); },
        removeScrollListener(callback) { listeners.delete(callback); },
    };
})();

// ================================
// Zoom Interop (CSS zoom - there's no canvas here to re-render at scale)
// ================================
window.currentScale ??= 1;

function applyZoom() {
    const container = document.getElementById("page-container");
    if (container) container.style.zoom = window.currentScale;
}

window.NewHtmlZoomer = (function () {
    return {
        zoomIn() {
            window.currentScale = Math.min(2, window.currentScale + 0.1);
            applyZoom();
        },
        zoomOut() {
            window.currentScale = Math.max(0.5, window.currentScale - 0.1);
            applyZoom();
        },
        setZoom(amount) {
            window.currentScale = amount;
            applyZoom();
        },
        getZoom() {
            return window.currentScale;
        },
    };
})();

// ================================
// Cursor Handler Interop
// ================================
window.NewHtmlCursorHandler = (function () {
    return {
        getDraggable() {
            window.draggable ??= true;
            return window.draggable;
        },
        toggleDraggable() {
            window.draggable = !window.draggable;
        }
    };
})();

// ================================
// HtmlController
// ================================
window.HtmlController = (function () {
    return {
        getScroller() { return NewHtmlScroller; },
        getHorizontalScroller() { return NewHtmlScrollerHorizontal; },
        getZoomer() { return NewHtmlZoomer; },
        getCursorHandler() { return NewHtmlCursorHandler; },
    };
})();

// ================================
// DRAG TO SCROLL
// ================================
(function () {
    const container = document.getElementById("page-container");
    if (!container) return;

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