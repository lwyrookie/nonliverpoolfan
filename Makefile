ANTLR_LIB := lib/antlr.jar
ANTLR_GRAMMAR := Lex.g

all: group compiler
group:
	@echo "Member 1: Arun Thomas id: thoma442   Member2: Weiyi Lou  id:wlou"
compiler: 
	rm -rf classes
	mkdir classes
	rm -rf build
	mkdir build
	java -cp $(ANTLR_LIB) org.antlr.v4.Tool -visitor -o build $(ANTLR_GRAMMAR)
	javac -cp $(ANTLR_LIB) -d classes src/*.java build/*.java
executor: 
	rm -rf build
	mkdir build
	java -cp $(ANTLR_LIB) org.antlr.Tool -o build $(ANTLR_GRAMMAR)

clean: 
	rm -rf classes build

.PHONY: all group compiler executor clean
