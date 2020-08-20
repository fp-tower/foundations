#!/bin/bash

while [[ "$#" -gt 0 ]]; do
    case $1 in
        -b|--build) sbt slides/mdoc; shift ;;
        -c|--clean) rm -r docs; shift ;;
        *) echo "Unknown parameter passed: $1"; exit 1 ;;
    esac
    shift
done

cp -r slides/docs docs/

cd docs

mkdir pdf screenshots

for file in *.html
do
 decktape remark "$file" "${file%.html}.pdf" --chrome-arg=--allow-file-access-from-files \
  --screenshots --size 1920x1080 --screenshots-directory screenshots
done

mv *.pdf pdf
cd ..


tar -czf docs/pdf/foundation.tar.gz docs/pdf/*.pdf docs/screenshots