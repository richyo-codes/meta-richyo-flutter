# SPDX-License-Identifier: MIT
SUMMARY = "GTK3 Flutter LAN client for Bambu Lab printers"
HOMEPAGE = "https://github.com/richyo-codes/boom-print"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8e48d6f05c4a61a002ee11a9badf933d"
SRC_URI = "https://github.com/richyo-codes/boom-print/releases/download/v${PV}/boomprint-linux-x64-${PV}.tar.gz"
SRC_URI[sha256sum] = "58cd83690c6381c34af559660e4d1a64364d9ef0bf313f2702051e9ca7e55cb2"
S = "${UNPACKDIR}/bundle"

COMPATIBLE_HOST = "x86_64.*-linux$"
COMPATIBLE_HOST:libc-musl = "^$"
inherit features_check
ANY_OF_DISTRO_FEATURES = "wayland x11"
REQUIRED_DISTRO_FEATURES = "opengl"

DEPENDS = "gtk+3 mpv libsecret libepoxy fontconfig zlib patchelf-native"
# The ftpconnect Dart native asset loads libcurl dynamically.
RDEPENDS:${PN} += "libcurl ca-certificates"
PRIVATE_LIBS = "libflutter_linux_gtk.so libfile_saver_plugin.so libflutter_secure_storage_linux_plugin.so libmedia_kit_libs_linux_plugin.so libmedia_kit_video_plugin.so liburl_launcher_linux_plugin.so"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INSANE_SKIP:${PN} += "ldflags"

do_install() {
    dest=${D}${libdir}/boomprint
    install -d "$dest/lib" ${D}${bindir}
    install -m 0755 ${S}/boomprint "$dest/"
    cp -R --no-preserve=ownership ${S}/data "$dest/"
    # The Linux runner uses GTK and libmpv. The archive also carries an
    # unused JNI bridge linked against the build host's Temurin JDK.
    for lib in libapp.so libflutter_linux_gtk.so; do
        install -m 0644 ${S}/lib/$lib "$dest/lib/"
    done
    for plugin in ${S}/lib/*_plugin.so; do
        install -m 0755 "$plugin" "$dest/lib/"
        patchelf --set-rpath '$ORIGIN' "$dest/lib/$(basename "$plugin")"
    done
    ln -s ${libdir}/boomprint/boomprint ${D}${bindir}/boomprint
    install -d ${D}${datadir}/applications
    cat > ${D}${datadir}/applications/boomprint.desktop <<END
[Desktop Entry]
Type=Application
Name=BoomPrint
Exec=boomprint
Categories=Utility;
Terminal=false
END
}

FILES:${PN} += "${libdir}/boomprint ${datadir}/applications"
