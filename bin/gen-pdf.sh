#!/bin/bash

while [[ "$#" -gt 0 ]]; do
    case $1 in
        -b|--build) echo "Rebuilding slides"; sbt slides/mdoc ;;
        -f|--file) FILE_INPUT_REGEX="$2"; shift;;
        -c|--clean) echo "Remove docs folder"; rm -r docs ;;
        *) echo "Unknown parameter passed: $1"; exit 1 ;;
    esac
    shift
done

cp -r slides/docs docs/

cd docs

for section in value-functions generic-functions data-processing bonus
do
 mkdir -p "pdf/$section" "screenshots/$section"
done

for file in ${FILE_INPUT_REGEX:-*.html}
do
 decktape remark "$file" "${file%.html}.pdf" --chrome-arg=--allow-file-access-from-files \
  --screenshots --size 1920x1080 --screenshots-directory screenshots
done

mv *.pdf pdf
cd ..


tar -czf docs/pdf/foundation.tar.gz docs/pdf/*.pdf docs/screenshots