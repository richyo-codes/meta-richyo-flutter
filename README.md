# Personal Flutter projects

`meta-richyo-flutter` tracks Rich Young's Flutter applications and custom
engine work. It is a separate Yocto layer in this repository, ready to extract
into its own repository later. It currently depends only on OE-Core, with
Wrynose compatibility. It defines no board, distro, or default engine policy.
Layer metadata is MIT licensed; fetched projects retain their own licenses.

Add this layer to a **new or idle** build environment:

```bash
bitbake-layers add-layer /path/to/meta-richyo-flutter
bitbake personal-flutter-projects
bitbake pcalc-express-source -c unpack
bitbake flutter-gtk4-source -c unpack
```

`personal-flutter-projects` installs the versioned registry at
`/usr/share/richyo-flutter/projects.json`. Optionally include it in an image:

```conf
IMAGE_INSTALL:append = " personal-flutter-projects"
```

The two `-source` recipes fetch pinned Git revisions; they produce no runtime
packages and do not build an app, SDK, or engine. Unpacking pcalc does not fetch
its pub dependencies. Those remain tracked in the upstream lockfile, with the
custom Git dependencies recorded in the registry for integration planning.

## Applications and engine work

- [pcalc-express](https://github.com/richyo-codes/pcalc-express) has GTK3/GTK4
  Linux runners, a path dependency on its expression engine, custom Git plugins,
  and native expression backends. Its source license is AGPL-3.0-only.
- [Flutter GTK4 PR #193405](https://github.com/flutter/flutter/pull/193405)
  is tracked by exact fork revision. It was an open draft at the recorded
  review date. GTK4 is opt-in through `--linux-gtk=gtk4`.
- MooshRevolt remains in `meta-embedded-images` during active board builds.
  This registry references its existing recipe rather than duplicating it.

The PR head and pcalc's documented CI framework pin are **different revisions**.
The registry preserves both; neither implies an ABI-compatible SDK/engine pair.
The prebuilt engine ID is informational, not a checksum or a fetchable Yocto
artifact declaration. Upstream documents an artifact being republished under
the same ID, so future prebuilt recipes must pin archive SHA-256 checksums.

## Desktop integration still required

The existing meta-flutter `flutter-app` class builds an application bundle for
an external embedder. It does not build pcalc's GTK desktop runner. The pinned
meta-flutter desktop engine configuration uses GTK3. Enabling its desktop
option alone does not enable this GTK4 fork.

The next implementation needs a matched fork SDK, target engine, host
`gen_snapshot`, GTK4 headers/libraries, desktop runner and plugin compilation,
and the native expression backends. Start with x86-64 QEMU and a desktop or
Wayland compositor; verify the binary loads GTK4 without GTK3 and that native
plugins and expression evaluation work before adding ARM64 board images.
No GTK4 image or pcalc executable is supplied by this initial tracking layer.

[Remote UI options](remote-ui.md) compares web/Wasm with native WayVNC access
and identifies the additional runtime/backend integration each requires.

[MooshRevolt operator configuration](operator-config.md) enables a tested,
opt-in startup-config backport for the existing pinned app recipe.

Keep application recipes here, image composition in `meta-embedded-images`,
and board support in the existing BSP layers. Keep independently upstreamable
meta-flutter or engine fixes separate from personal application integration.

## Flutter Servo browser

With this layer enabled, run `bitbake flutter-servo-browser` in an x86-64
glibc desktop build with OpenGL and GTK Wayland or X11 support. Add
`flutter-servo-browser` to `IMAGE_INSTALL` and launch `flutter-servo-browser`
inside the graphical session. Its `flutter-servo` dependency installs the
matching GTK3 plugin, Flutter engine and native Rust library.

The recipes package the pinned upstream v0.1.1 Linux GTK3 release. An ARM64
Orange Pi build requires native ARM64 artifacts or a source cross-build.

To build the dedicated Weston kiosk VM from the workspace root:

```sh
builds="$(realpath ../yocto-builds)"
bitbake/bin/bitbake-setup --setting default top-dir-prefix "$(dirname "$builds")" --setting default top-dir-name "$(basename "$builds")" init ./flutter-servo-kiosk.conf.json qemux86-64 --non-interactive --no-init-vscode
. "$builds/flutter-servo-kiosk-qemux86-64/build/init-build-env"
bitbake flutter-servo-kiosk-image
runqemu qemux86-64 flutter-servo-kiosk-image ext4 slirp gtk
```
