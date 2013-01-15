ENV=LD_LIBRARY_PATH="`pwd`/libs/" CLASSPATH="`pwd`/jar/2DGame_1.2.jar":"`pwd`/libs/jogl-all.jar":"`pwd`/libs/jogl-all-natives-linux-amd64.jar":"`pwd`/libs/gluegen-rt.jar":"`pwd`/libs/gluegen-rt-natives-linux-amd64.jar:"`pwd`/libs/joal.jar":"`pwd`/libs/joal-natives-linux-amd64.jar"
JAVACMD=javac

bin/*.class: src/*.java
	mkdir bin ; $(ENV) $(JAVACMD) src/*.java -d bin
	
clean:
	rm bin/*.class src/*~
