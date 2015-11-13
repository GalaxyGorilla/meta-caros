inherit cross
CCACHE = ""

DESCRIPTION = "Go Programming Language Cross Compiler, version 1.5.1"
PROVIDES = "virtual/${TARGET_PREFIX}golang"
DEPENDS = "libgcc"
SRC_URI = "https://storage.googleapis.com/golang/go1.5.1.src.tar.gz"
SRC_URI += "https://storage.googleapis.com/golang/go1.4.2.linux-amd64.tar.gz;name=go1.4;subdir=go1.4"
LICENSE = "CLOSED"
S="${WORKDIR}/go"

SRC_URI[md5sum] = "4adfbdfca523cc1c229be8a321f3602f"
SRC_URI[sha256sum] = "a889873e98d9a72ae396a9b7dd597c29dcd709cafa9097d9c4ba04cff0ec436b"
SRC_URI[go1.4.md5sum] = "cdc1ff96c6c99b918402693a5cf0e1f3"
SRC_URI[go1.4.sha256sum] = "141b8345932641483c2437bdbd65488a269282ac85f91170805c273f03dd223b"

PR = "r0.1"

do_compile () {
    export CGO_ENABLED=1
    export CC=gcc

    export GOROOT=${WORKDIR}/go1.4/go
    export PATH=$PATH:$GOROOT/bin
    export GOROOT_BOOTSTRAP=${WORKDIR}/go1.4/go

    export GOOS="linux"
    export GOARCH="${TARGET_ARCH}"
    if [ "${TARGET_ARCH}" = "x86_64" ]; then
        export GOARCH="amd64"
    fi  

    rm -rf "${WORKDIR}/go-linux-${GOARCH}-bootstrap"

    cd ${S}/src
    GOOS=${GOOS} GOARCH=${GOARCH} ./bootstrap.bash
}

do_install () {
    bootstrapped_go_dir="${WORKDIR}/go-linux-${TARGET_ARCH}-bootstrap"
    if [ "${TARGET_ARCH}" = "x86_64" ]; then
        bootstrapped_go_dir="${WORKDIR}/go-linux-amd64-bootstrap"
    fi

    mkdir -p ${D}${bindir}
    cp -a ${bootstrapped_go_dir}/bin/go ${D}${bindir}/
    mkdir -p ${D}${libdir}/go
    cp -a ${bootstrapped_go_dir}/pkg ${D}${libdir}/go/
    cp -a ${bootstrapped_go_dir}/api ${D}${libdir}/go/
    cp -a ${bootstrapped_go_dir}/src ${D}${libdir}/go/
}
