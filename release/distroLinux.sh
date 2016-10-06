#! /bin/bash
# Pack a release for distribution

if [ "$1/A" == "/A" ]; then
    echo usage: distro.sh RELEASE-ID
    exit 1
fi

RELEASE="/soi/release/$1"

if [ -d $RELEASE ]; then
    cd $RELEASE
    tar -czvf soi.tar.gz bin/ etc/ lib/ sql/ local/
    cd $RELEASE/soi
    tar -czvf ../soi.$1.tar.gz *
else
    echo "Error could not locate [$RELEASE]"
    exit 1
fi

