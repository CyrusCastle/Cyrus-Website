## <img width="32" height="32" alt="favicon" src="https://github.com/user-attachments/assets/b20b3945-de59-49a5-81db-ca0420e4c39f" /> Cyrus-Website
Hi, I'm Cyrus! Welcome to the source code for my personal (and professional?) website.

<img width="959.5" height="539.5" alt="CyrusWebsiteScreenshot" src="https://github.com/user-attachments/assets/64ec1836-a746-4bac-b58f-f07563d5ac3a" />

<img width="236.25" height="512" alt="Nokia1" src="https://github.com/user-attachments/assets/90d4527e-a566-4415-a931-3ed573bd633a" />
<img width="236.25" height="512" alt="Nokia2" src="https://github.com/user-attachments/assets/a52adf1b-f1fb-4e0f-adfe-2886ff4bd730" />



## What is it?
The main site has been designed to reflect the aesthetics of older Windows operating systems, particularly Windows 95 and Windows 98. It has been made in Kotlin with Compose Multiplatform, targeting Wasm. A secondary target replicates the aesthetics of an old Nokia phone, and acts as the mobile adaptation of my site.

Each part of the app is broken down into a "program" or an "app", which each aim to say something about me.

---

## Libraries
Libraries used in this project:
- All standard [Kotlin](https://kotlinlang.org/) / [Compose Multiplatform](https://kotlinlang.org/compose-multiplatform/) libraries
- [KotlinX datetime](https://github.com/Kotlin/kotlinx-datetime) (for our clock at the bottom)
- [Coil 3](https://github.com/coil-kt/coil) (for image loading)
- [DrawBox-Enhanced](https://github.com/CyrusCastle/DrawBox-Enhanced), my rework of Mark Yavorskyi's [DrawBox](https://github.com/MarkYav/DrawBox) (for our current version of PaintWindow)
- Mohamed Rejeb's [compose-rich-editor](https://github.com/MohamedRejeb/compose-rich-editor) (for our WordPadWindow)
- Vincent Guillebaud's [FileKit](https://github.com/vinceglb/FileKit) (for uploading/downloading files from the site)
- NucleusFramework's [CompsePdfReader](https://github.com/NucleusFramework/ComposePdfReader) (PDF renderer)

(Also, a special thanks to Alex Meub for providing the [Windows 98 icons](https://win98icons.alexmeub.com/) and x1unix for the [S60 icons](https://github.com/x1unix/s60-icon-pack) used in the mobile verison of the site)

## Building & Running
To build and run a development version, run the following through Gradle:
```wasmJsBrowserDevelopmentRun -t --quiet --stacktrace```

For a dev version of the desktop site, target the ```webApp``` project; for the mobile site target the ```mobileWebApp``` project.
