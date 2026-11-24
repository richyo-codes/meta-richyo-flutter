# SPDX-License-Identifier: MIT
SUMMARY = "Programmer's expression calculator for flutter-pi"
HOMEPAGE = "https://github.com/richyo-codes/pcalc-express"
LICENSE = "AGPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f9f8da7826b4eb457cf1f11429652f97"
SRC_URI = "git://github.com/richyo-codes/pcalc-express.git;protocol=https;branch=main file://0001-pin-sdk-test-api.patch"
SRCREV = "8c8940fc2ddf492358e94884930f3067b232b0b5"
PV = "1.0.0+git"

inherit flutter-app-native features_check
PUBSPEC_APPNAME = "pcalc_express"
FLUTTER_APPLICATION_PATH = ""
FLUTTER_APP_RUNTIME_MODES = "release"
REQUIRED_DISTRO_FEATURES = "opengl"
COMPATIBLE_HOST = "aarch64.*-linux"
DEPENDS += "clang llvm"
RDEPENDS:${PN} += "flutter-pi liberation-fonts"

do_install:append() {
    install -d ${D}${bindir}
    cat > ${D}${bindir}/pcalc-express <<END
#!/bin/sh
exec ${bindir}/flutter-pi --release ${FLUTTER_INSTALL_DIR}/${FLUTTER_SDK_VERSION}/release "\$@"
END
    chmod 0755 ${D}${bindir}/pcalc-express
}
