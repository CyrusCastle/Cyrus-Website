// Imports
import * as pdfjsLib from "./pdf.mjs";
pdfjsLib.GlobalWorkerOptions.workerSrc ="./pdf.worker.mjs";

// Find the file we want
const params = new URLSearchParams(window.location.search);
const file = params.get("file") || "cv.pdf";
const pdfUrl = "http://localhost:8080/composeResources/cyruswebsite.composeapp.generated.resources/files/pdf/pdfs/" + file;

// Variables
let pdfDoc = null;
window.currentScale ??= 0.75;
const container = document.getElementById("pdf-container");
let loaded = false;

// Load the PDF once
async function loadPdf() {
  pdfDoc = await pdfjsLib.getDocument(pdfUrl).promise;
  await renderPdf();
}

// Render all pages
async function renderPdf() {
  console.log("starting a render");
  container.innerHTML = "";

  for (let pageNum = 1; pageNum <= pdfDoc.numPages; pageNum++) {
    const page = await pdfDoc.getPage(pageNum);
    const viewport = page.getViewport({ scale: window.currentScale });

    // Wrapper div
    const pageWrapper = document.createElement("div");
    pageWrapper.className = "pdf-page";

    // Canvas
    const canvas = document.createElement("canvas");
    const context = canvas.getContext("2d");

    canvas.width = viewport.width;
    canvas.height = viewport.height;

    pageWrapper.appendChild(canvas);
    container.appendChild(pageWrapper);

    await page.render({
      canvasContext: context,
      viewport: viewport,
    }).promise;
  }

  loaded = true;
}
//async function renderPdf() {
//  console.log("starting a render");
//  container.innerHTML = "";
//
//  for (let pageNum = 1; pageNum <= pdfDoc.numPages; pageNum++) {
//    const page = await pdfDoc.getPage(pageNum);
//    const viewport = page.getViewport({ scale: window.currentScale });
//
//    const canvas = document.createElement("canvas");
//    const context = canvas.getContext("2d");
//
//    canvas.width = viewport.width;
//    canvas.height = viewport.height;
//
//    container.appendChild(canvas);
//
//    await page.render({
//      canvasContext: context,
//      viewport: viewport,
//    }).promise;
//  }
//
//  loaded = true;
//}

window.renderPdf = function () {
    renderPdf();
};

// Public zoom functions
window.zoomIn = function () {
  currentScale += 0.25;
  renderPdf();
};

window.zoomOut = function () {
  currentScale = Math.max(0.25, currentScale - 0.25);
  renderPdf();
};

window.setZoom = function (amount) {
    currentScale = amount;
    if (loaded){
        renderPdf();
    }
};

// Start
loadPdf();