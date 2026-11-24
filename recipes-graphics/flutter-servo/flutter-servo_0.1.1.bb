# SPDX-License-Identifier: MIT
SUMMARY = "Flutter Servo GTK3 plugin, Rust bridge and matching Flutter engine"
require flutter-servo-release.inc

DEPENDS += "patchelf-native"
PRIVATE_LIBS = "libflutter_servo_plugin.so libflutter_linux_gtk.so librust_lib_flutter_servo.so"

do_install() {
    install -d ${D}${bundle_dir}/lib
    for lib in libflutter_servo_plugin libflutter_linux_gtk librust_lib_flutter_servo; do
        install -m 0755 ${S}/lib/$lib.so ${D}${bundle_dir}/lib/
    done
    patchelf --set-rpath '$ORIGIN' ${D}${bundle_dir}/lib/libflutter_servo_plugin.so
}
FILES:${PN} = "${bundle_dir}/lib/*.so"
