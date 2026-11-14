# SPDX-License-Identifier: MIT
SUMMARY = "Pinned sources of Rich Young's Flutter GTK4 pull request"
HOMEPAGE = "https://github.com/flutter/flutter/pull/193405"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1d84cf16c48e571923f837136633a265"
SRC_URI = "git://github.com/richyo-codes/flutter.git;protocol=https;branch=3.48.0-gtk4.issue94804.20260925"
SRCREV = "5cdfc3d358c9638ffd135ad1b32a41086096b267"
PV = "3.48.0+git"

# Tracks framework/engine source together; not an SDK or compiled engine.
# Do not substitute this for meta-flutter's matched engine/SDK recipes.
inherit nopackages
INHIBIT_DEFAULT_DEPS = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install[noexec] = "1"
