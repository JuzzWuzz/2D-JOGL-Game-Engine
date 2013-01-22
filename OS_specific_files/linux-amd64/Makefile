PLATFORM=amd64
GAMEENGINE=2DGame_2.0.jar
CLASSPATH="`pwd`/jar/$(GAMEENGINE)":"`pwd`/libs/jogl-all.jar":"`pwd`/libs/jogl-all-natives-linux-$(PLATFORM).jar":"`pwd`/libs/gluegen-rt.jar":"`pwd`/libs/gluegen-rt-natives-linux-$(PLATFORM).jar":"`pwd`/libs/joal.jar":"`pwd`/libs/joal-natives-linux-$(PLATFORM).jar"
ENV=LD_LIBRARY_PATH="`pwd`/libs/" CLASSPATH=$(CLASSPATH)

DIRBIN=bin
DIRSRC=src
JAVACMD=javac
JAVARUN=java

bin/*.class: src/*.java
	mkdir $(DIRBIN) ; $(ENV) $(JAVACMD) $(DIRSRC)/*.java -d $(DIRBIN)
	
clean:
	rm $(DIRBIN)/*.class $(DIRSRC)/*~

run:
	$(JAVARUN) -classpath $(DIRBIN):$(CLASSPATH) Runner