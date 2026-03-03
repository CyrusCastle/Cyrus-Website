## Cyrus-Website
Hi, I'm Cyrus! Welcome to the source code for my personal (and professional?) website.

<img width="959.5" height="539.5" alt="CyrusWebsiteScreenshot" src="https://github.com/user-attachments/assets/64ec1836-a746-4bac-b58f-f07563d5ac3a" />


## What is it?
The site has been designed to reflect the aesthetics of older Windows operating systems, particularly Windows 95 and Windows 98. It has been made in Kotlin with Compose Multiplatform, targeting Wasm.

Each part of the app is broken down into a "program", with the following programs available:
- FileExplorerWindow and StartMenu (containers to access other programs)
- BrowserWindow (utilise the new WebElementView to show iframes, designed to view simple HTML web pages that will describe some project or other I've worked on)
- PdfWindow (built on the same work as BrowserWindow, but with different controls (i.e. zoom, rather than address bar) to reflect the differing context)
- GlobeWindow (a simple map of the world with markers around showing places I've travelled to (or wish to), with some added context/images if the user clicks them)
- ImageWindow (image viewer which will show any images I put on the site, made primarily so that users can see closer the images on GlobeWindow)
- PaintWindow (a prototype MS Paint, was originally used instead of ImageWindow but is quite incomplete)
- ShortcutWindow (not a real window, but anything that I can't use iframes for I will open in a new tab)
- NotepadWindow (designed for small or simple text documents that I might need to put on the site)
- WordpadWindow (designed for longer or more complicated (rich) text documents)

---

## Libraries
To avoid trying to re-invent the wheel, I am using a couple of libraries for this project:
- All standard [Kotlin](https://kotlinlang.org/) / [Compose Multiplatform](https://kotlinlang.org/compose-multiplatform/) libraries (obviously)
- [KotlinX datetime](https://github.com/Kotlin/kotlinx-datetime) (for our clock at the bottom)
- [Coil 3](https://github.com/coil-kt/coil) (for image loading)
- Mark Yavorskyi's [DrawBox](https://github.com/MarkYav/DrawBox) (for our current version of PaintWindow)
- Mohamed Rejeb's [compose-rich-editor](https://github.com/MohamedRejeb/compose-rich-editor) (for our WordPadWindow)
- Vincent Guillebaud's [FileKit](https://github.com/vinceglb/FileKit) (for uploading/downloading files from the site)

## Building & Running
To build and run a development version, run the following through Gradle:
```wasmJsBrowserDevelopmentRun -t --quiet --stacktrace```