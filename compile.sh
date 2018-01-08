#!/bin/bash

set -e # Exit with nonzero exit code if anything fails

mkdir -p out

echo "Compiling JS"
lein package
cp -R public/* out/
rm -rf out/js/release
