JAVAC = javac
# JVM = 1.7

BIN = ./bin/
SRC = ./src/
MAIN = cookie/CookieFactory
JAR = mysql-connector-java-5.1.38-bin.jar
RUNFLAGS = -cp $(BIN):$(JAR)

JAVAFLAGS = -g -d $(BIN) -cp $(SRC) #-target $(JVM)

all:
	$(JAVAC) $(JAVAFLAGS) $(SRC)$(MAIN).java

run : 
	java $(RUNFLAGS) $(MAIN)

clean : 
	rm -rf $(BIN)*

