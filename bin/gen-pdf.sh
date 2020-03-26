#!/bin/bash

sbt slides/mdoc
cp -r slides/docs docs/

for file in docs/*.html
do
 mkdir ${file%.html}
 decktape remark "$file" "${file%.html}.pdf" --chrome-arg=--allow-file-access-from-files \
  --screenshots --size 1200x900 --screenshots-directory ${file%.html}
done

mkdir -p docs/pdf
mv docs/*.pdf docs/pdf

tar -czf docs/pdf/foundation.tar.gz docs/pdf/*.pdf