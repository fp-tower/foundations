SRCS=$(wildcard docs/*.html)
PDFS=$(SRCS:.html=.pdf)

pdf: ${PDFS}

docs/%.pdf: docs/%.html
	decktape $< $@

docs/%.html: slides/tut/%.html
	sbt tut

depends:
	npm install -g decktape

clean:
	rm -rf docs 
