# Remote Flutter UI options

Two useful paths have different ownership of the meter and display. Neither
is enabled in the current Yocto images.

| Path | UI executes on | Meter connection | What must be added |
| --- | --- | --- | --- |
| Flutter web/Wasm | Client browser | Browser Bluetooth, or a device API | Web-compatible plugins; static web serving; preferably shared device backend |
| GTK app + WayVNC + noVNC | Orange Pi | Existing device-side BlueZ | GTK runner/engine; wlroots compositor; WayVNC; WebSocket/browser bridge |

## Web/Wasm

Flutter supports `flutter build web --wasm`. Wasm compatibility must be checked
for every dependency. The Mooshimeter agent successfully built the app as Wasm
with Flutter 3.44.9, but found native filesystem exports, unguarded `dart:io`
audio checks and browser Bluetooth chooser behavior still needing runtime work.
Compilation alone is not a browser acceptance test. Flutter paints its UI using canvas-based
web renderers; this is not a conversion of the widgets into ordinary HTML
controls. See [Flutter web deployment](https://docs.flutter.dev/deployment/web)
and [Wasm support](https://docs.flutter.dev/platform-integration/web/wasm).

A web UI served by the Orange Pi executes in the visitor's browser. Its BLE
plugin would access that browser's Bluetooth environment, not the Pi's BlueZ
connection. Serving the app alone therefore does not provide remote access to
the Pi-attached meter. A device-side meter service with an HTTP/WebSocket API
would let native and web clients share readings, commands and config. That API
and its access policy still need design and implementation.

The device's `/data/config` file is server-side state; do not publish it as a
static web asset or assume a browser can open it. A future config API should
validate changes, write atomically, and distinguish live changes from restart
requirements. Client-side display preferences can remain browser-local.

## Remote native display

[WayVNC](https://github.com/any1/wayvnc) captures a compatible wlroots Wayland
session, including headless sessions. It does not support Weston, GNOME or KDE.
Our current flutter-pi process owns DRM/KMS directly; adding WayVNC to that image
cannot capture it. The GTK runner must instead run under a supported Wayland
compositor, such as Sway, with a working desktop engine build.

WayVNC serves VNC. A browser path additionally needs noVNC and a compatible
WebSocket transport/bridge. This shares the native app session and device-side
connection, rather than creating another BLE owner. Start with loopback access
and an SSH tunnel during development; exposing it needs an explicit access
configuration. No VNC listener is enabled by these documents.

For a remote copy of the existing screen, remote native display avoids an
immediate meter-backend rewrite but requires a new compositor/runner profile.
For independent local and browser dashboards, a shared meter service is the
more flexible architecture. Keep these as separate opt-in profiles instead of
coupling meter startup settings to one rendering backend.
