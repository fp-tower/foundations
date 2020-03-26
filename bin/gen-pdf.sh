#!/bin/bash

sbt slides/mdoc
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