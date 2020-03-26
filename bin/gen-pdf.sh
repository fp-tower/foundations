#!/bin/bash

sbt slides/mdoc
cp -r slides/docs docs/

cd docs

mkdir pdf img

for file in *.html
do
 mkdir "img/${file%.html}"
 decktape remark "$file" "pdf/${file%.html}.pdf" --chrome-arg=--allow-file-access-from-files \
  --screenshots --size 1920x1080 --screenshots-directory "img/${file%.html}"
done

cd ..

tar -czf docs/pdf/foundation.tar.gz docs/pdf/*.pdf docs/img