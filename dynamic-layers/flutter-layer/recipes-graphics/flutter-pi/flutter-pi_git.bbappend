# SPDX-License-Identifier: MIT
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# In the local experiment, add the directory containing flutter_pi/CMakeLists.txt
# to FILESEXTRAPATHS:prepend:pn-flutter-pi. The file fetcher tracks its contents.
PACKAGECONFIG[media-kit-video] = "-DMEDIA_KIT_FLUTTER_PI_DIR=${UNPACKDIR}/flutter_pi,,mpv"
SRC_URI:append = "${@bb.utils.contains('PACKAGECONFIG', 'media-kit-video', ' file://0001-include-media-kit-backend.patch file://flutter_pi', '', d)}"
