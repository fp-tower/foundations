SRCS=$(notdir $(wildcard slides/tut/*.html))
HTMLS=$(addprefix slides/docs/, $(SRCS))
PDFS=$(HTMLS:.html=.pdf)

pdf: ${PDFS}

docs/img:
	mkdir -p docs

docs/%.pdf: docs/%.html
	decktape $< $@

docs/%.html: docs/img slides/tut/%.html
	sbt tut

depends:
	npm install -g decktape

clean:
	rm -rf docs 
