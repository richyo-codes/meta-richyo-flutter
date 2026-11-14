# MooshRevolt operator configuration

This opt-in integration backports startup configuration to the MooshRevolt
revision pinned by `meta-embedded-images`, without changing the shared source
recipe. Enable it only in a new or idle build:

```bash
bitbake-layers add-layer /path/to/meta-richyo-flutter
bitbake-config-build enable-fragment richyoflutter/moosh-revolt-operator
bitbake embedded-moosh-revolt-image
```

Alternatively, require the fragment by absolute path in `conf/local.conf`.
It adds a recipe-scoped source patch; it does not change the Flutter engine or
enable a startup service. The patch targets app commit
`e37be6621f02fd6303f9ae67006a7e57f826f532`. Review/remove it when adopting the
app-side implementation upstream. The two app trees were coordinated with the
existing Herdr Mooshimeter agent; the backport omits unrelated localization.

On Linux the app reads optional `/etc/moosh-revolt/config.json`. An explicit
`MOOSHREVOLT_CONFIG` path must exist and be readable. The
[persistent SD profile](../docs/appliance-state.md) sets it to
`/data/config/moosh-revolt/config.json` for the app's systemd services.
For interactive launches, export that variable yourself.

The app validates the complete file before connecting. Invalid JSON, unknown
keys, unsupported values or unreadable explicit files abort startup rather than
connecting to a saved fallback meter. Restart the app to reload changes.
This file overrides saved UI settings at each launch; it is never rewritten by
the app. Omitted fields inherit saved preferences, then built-in defaults.

Example (replace the address and then set global `autoConnect` to true):

```json
{
  "schemaVersion": 1,
  "autoConnect": false,
  "sampleRateHz": 125,
  "graphMaxPoints": 300,
  "darkMode": true,
  "showFloatingReadings": false,
  "rememberedDevices": [
    {
      "address": "00:11:22:33:44:55",
      "name": "Bench meter",
      "autoConnect": true
    }
  ]
}
```

All fields are optional. `sampleRateHz` accepts 125, 250, 500, 1000, 2000, 4000,
or 8000. `graphMaxPoints` is an integer from 2 to 100000. The boolean
`skipUpgrade` exposes the existing preference, not new firmware-update logic.
Device addresses must be colon-separated Bluetooth MACs; names are labels,
not discovery selectors. Explicit null values and duplicate addresses fail
validation. A supplied `rememberedDevices` list replaces the saved list and
stays session-only, including automatic remember-on-connect and device-list UI
edits. Other explicit UI changes retain their individual-setting persistence.

Startup selects only exact enabled addresses, regardless of advertised name.
Its discovery window is bounded and its listener is released on completion,
timeout or error. This implements startup autoconnect, not a continuous
reconnection service after a later disconnection. Device calibration, channel
mode/range, per-channel units and backend/display selection are not config keys
yet. Physical BLE behavior still needs a board-and-meter test.

Validation: app backport `flutter analyze` and all 42 tests passed using Flutter
3.44.9. These are host app tests, not evidence that an updated Yocto image has
been built or flashed. Regression evidence and reproductions accompany the
patch in `docs/startup-autoconnect.md` in the patched application sources.
