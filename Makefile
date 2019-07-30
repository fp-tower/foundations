SRCS=$(notdir $(wildcard slides/tut/*.html))
HTMLS=$(addprefix docs/, $(SRCS))
PDFS=$(HTMLS:.html=.pdf)

pdf: ${PDFS}

docs/img:
	cp -r slides/docs docs/

docs/%.pdf: docs/%.html
	decktape $< $@

docs/%.html: docs/img slides/tut/%.html
	sbt tut

depends:
	npm install -g decktape

clean:
	rm -rf docs
