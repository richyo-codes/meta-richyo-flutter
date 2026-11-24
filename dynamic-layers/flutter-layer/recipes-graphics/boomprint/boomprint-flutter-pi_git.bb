# SPDX-License-Identifier: MIT
SUMMARY = "Experimental BoomPrint ARM64 application bundle for flutter-pi"
HOMEPAGE = "https://github.com/richyo-codes/boom-print"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8e48d6f05c4a61a002ee11a9badf933d"
SRC_URI = "git://github.com/richyo-codes/boom-print.git;protocol=https;branch=main file://0001-match-flutter-sdk-test-api.patch"
SRCREV = "acb5d5acf6154597b14da0f59616338f4290f633"
PV = "1.0.0+git"

inherit flutter-app features_check

PUBSPEC_APPNAME = "boomprint"
FLUTTER_APPLICATION_PATH = ""
FLUTTER_APP_RUNTIME_MODES = "release"
PUBSPEC_IGNORE_LOCKFILE = "0"
REQUIRED_DISTRO_FEATURES = "opengl"
COMPATIBLE_HOST = "aarch64.*-linux"
RDEPENDS:${PN} += "flutter-pi libcurl mpv ca-certificates liberation-fonts"

do_install:append() {
    install -d ${D}${bindir}
    cat > ${D}${bindir}/boomprint-flutter-pi <<EOF_LAUNCH
#!/bin/sh
exec ${bindir}/flutter-pi --release ${FLUTTER_INSTALL_DIR}/${FLUTTER_SDK_VERSION}/release "\$@"
EOF_LAUNCH
    chmod 0755 ${D}${bindir}/boomprint-flutter-pi
}
