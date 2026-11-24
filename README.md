# meta-richyo-flutter

Yocto Project Wrynose layer for Richard Young’s embedded Linux Flutter
applications, with flutter-pi recipes and desktop packages. The layer metadata is MIT licensed. Each project retains its own license.

## Add the layer

From an initialized BitBake build environment, run:

```sh
bitbake-layers add-layer /path/to/meta-richyo-flutter
```

OE-Core is the base dependency. Enable meta-flutter for the flutter-pi recipes
and meta-oe for the BoomPrint desktop package.

## Embedded Linux with flutter-pi

### MooshRevolt

The app repository carries its own Yocto recipe. Builds that still use the
older pinned recipe can enable this layer’s temporary operator-config backport.
The image’s service configuration selects the on-device JSON file used for
settings and startup autoconnect. [MooshRevolt](https://github.com/richyo-codes/moosh-revolt-multimeter) · [Backport instructions](operator-config.md)

### BoomPrint

On Orange Pi Zero 3, the experimental `boomprint-flutter-pi` target builds
from pinned Dart sources against the image’s Flutter engine. The build requires
mpv and FFmpeg. Review their Yocto `commercial` license flags before enabling them. Build with `bitbake boomprint-flutter-pi` and include
`boomprint-flutter-pi` in the test image. Its launcher uses the direct DRM
embedder. Test on a free display, with the existing kiosk app stopped.
Camera preview requires the media_kit_video platform channel and a working
embedder texture implementation. The GTK plugin in the desktop release
cannot register that channel with flutter-pi.

### pcalc-express

The experimental ARM64 `pcalc-express` recipe targets flutter-pi and requires
meta-flutter with `flutter-app-native`. Build it with `bitbake pcalc-express`.

## Desktop builds and engine work

### Flutter Servo browser

With this layer enabled, run `bitbake flutter-servo-browser` in an x86-64
glibc desktop build with OpenGL and GTK Wayland or X11 support. Add
`flutter-servo-browser` to `IMAGE_INSTALL` and launch `flutter-servo-browser`
inside the graphical session. Its `flutter-servo` dependency installs the
matching GTK3 plugin, Flutter engine and native Rust library.

The recipes package the pinned upstream v0.1.1 Linux GTK3 release. An ARM64
Orange Pi build requires native ARM64 artifacts or a source cross-build.

### BoomPrint GTK3

Add `boomprint` to `IMAGE_INSTALL` in an x86-64 glibc desktop build with
this layer and meta-oe. Run `boomprint` in the graphical session. The
recipe packages the v0.3.1 GTK3 Linux release. Printer video uses libmpv
and FTP uses libcurl. Provide a session Secret Service such as GNOME Keyring
for credential storage. Orange Pi requires an ARM64 build of the application.

The pinned meta-oe mpv recipe carries Yocto's `commercial` license flag.
Review and accept the applicable mpv and FFmpeg flags in the build configuration.

### Flutter GTK4 and pcalc

A GTK4 desktop build still requires a
matched Flutter SDK, engine, and host `gen_snapshot`, plus the GTK4 runner and
native plugins. The Flutter fork uses `--linux-gtk=gtk4`. [pcalc-express](https://github.com/richyo-codes/pcalc-express) · [Flutter GTK4 PR](https://github.com/flutter/flutter/pull/193405)

The `pcalc-express-source` and `flutter-gtk4-source` targets fetch pinned sources.
Run `bitbake <target> -c unpack` to inspect them.

## Project inventory

The optional `personal-flutter-projects` package installs a snapshot of project
metadata at `/usr/share/richyo-flutter/projects.json`. Use the recipes to check
the revisions BitBake builds. See the [remote UI notes](remote-ui.md) for WayVNC
and web/Wasm options.
