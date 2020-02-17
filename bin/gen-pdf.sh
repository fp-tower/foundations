#!/bin/bash

sbt slides/mdoc
cp -r slides/docs docs/

for file in docs/*.html
do
 decktape "$file" "${file%.html}.pdf" --chrome-arg=--allow-file-access-from-files
done

mkdir -p docs/pdf
mv docs/*.pdf docs/pdf

tar -czf docs/pdf/foundation.tar.gz docs/pdf/*.pdf