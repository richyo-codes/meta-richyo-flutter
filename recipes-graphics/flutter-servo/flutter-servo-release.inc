# SPDX-License-Identifier: MIT
HOMEPAGE = "https://github.com/richyo-codes/flutter_servo"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=114c00b17c9c8dc1960debabaad63bca"

SRC_URI = "https://github.com/richyo-codes/flutter_servo/releases/download/v${PV}/flutter-servo-browser-linux-x86_64-gtk3-${PV}.tar.gz;subdir=bundle"
SRC_URI[sha256sum] = "ecf559b14f3b9b54b737c263d632404f1d2cd2f0327c6a4cff1c39e662073769"
S = "${UNPACKDIR}/bundle"

COMPATIBLE_HOST = "x86_64.*-linux$"
COMPATIBLE_HOST:libc-musl = "^$"
inherit features_check
REQUIRED_DISTRO_FEATURES = "opengl"
ANY_OF_DISTRO_FEATURES = "wayland x11"

bundle_dir = "${libdir}/flutter-servo"
DEPENDS += "gtk+3 libepoxy virtual/libgbm wayland fontconfig freetype zlib"
# The upstream release ships stripped engine, Rust and Dart AOT objects.
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INSANE_SKIP:${PN} += "ldflags"
