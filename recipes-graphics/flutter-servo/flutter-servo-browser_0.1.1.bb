# SPDX-License-Identifier: MIT
SUMMARY = "Flutter Servo desktop browser with the GTK3 runner"
require flutter-servo-release.inc

RDEPENDS:${PN} += "flutter-servo (= ${EXTENDPKGV})"
# These DT_NEEDED entries are satisfied by the matching private bundle.
PRIVATE_LIBS = "libflutter_servo_plugin.so libflutter_linux_gtk.so"

do_install() {
    install -d ${D}${bindir} ${D}${bundle_dir}/lib
    install -m 0755 ${S}/flutter_servo_example ${D}${bundle_dir}/flutter-servo-browser
    install -m 0644 ${S}/lib/libapp.so ${D}${bundle_dir}/lib/
    cp -R --no-preserve=ownership ${S}/data ${D}${bundle_dir}/
    ln -s ${bundle_dir}/flutter-servo-browser ${D}${bindir}/flutter-servo-browser
}
FILES:${PN} += "${bundle_dir}"
