# SPDX-License-Identifier: MIT
SUMMARY = "Pinned registry of personal Flutter applications and engine work"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
SRC_URI = "file://projects.json"

inherit allarch

do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install() {
    install -d ${D}${datadir}/richyo-flutter
    install -m 0644 ${UNPACKDIR}/projects.json ${D}${datadir}/richyo-flutter/projects.json
}
FILES:${PN} = "${datadir}/richyo-flutter"
