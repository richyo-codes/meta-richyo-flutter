# SPDX-License-Identifier: MIT
SUMMARY = "Pinned pcalc-express sources for desktop runner integration"
HOMEPAGE = "https://github.com/richyo-codes/pcalc-express"
LICENSE = "AGPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f9f8da7826b4eb457cf1f11429652f97"
SRC_URI = "git://github.com/richyo-codes/pcalc-express.git;protocol=https;branch=main"
SRCREV = "8c8940fc2ddf492358e94884930f3067b232b0b5"
PV = "1.0.0+git"

# Source acquisition only. This deliberately does not provide pcalc-express
# as a runtime package until the GTK runner and native dependencies are built.
inherit nopackages
INHIBIT_DEFAULT_DEPS = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install[noexec] = "1"
