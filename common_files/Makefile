LIBCLASSFILES="`pwd`/../OS_specific_files/linux-i586/libs/jogl-all.jar":"`pwd`/../OS_specific_files/linux-i586/libs/jogl-all-natives-linux-i586.jar":"`pwd`/../OS_specific_files/linux-i586/libs/gluegen-rt.jar":"`pwd`/../OS_specific_files/linux-i586/libs/gluegen-rt-natives-linux-i586.jar":"`pwd`/../OS_specific_files/linux-i586/libs/joal.jar":"`pwd`/../OS_specific_files/linux-i586/libs/joal-natives-linux-i586.jar"
ENV=LD_LIBRARY_PATH="`pwd`/../OS_specific_files/linux-i586/libs/" CLASSPATH="`pwd`/jar/2DGame_1.2.jar":$(LIBCLASSFILES)
JAVACMD=javac

JAR_FILE=2DGame_1.2.jar

all: jar docs

jar: bytecode
	cd src && jar cf $(JAR_FILE) GameEngine/*.class && cd .. || cd ..
	mkdir jar ; mv src/$(JAR_FILE) jar/$(JAR_FILE)
	#$(ENV) $(JAVACMD) src/*.java
	
docs:
	javadoc -d docs src/GameEngine/*.java -classpath $(LIBCLASSFILES)
	
bytecode:
	$(ENV) $(JAVACMD) src/GameEngine/*.java
  
	
#src/*.class: src/*.java
#	$(ENV) $(JAVACMD) src/*.java
	
clean_all: clean_docs clean_jar clean_bytecode
	rm -r src/*/*~
	
clean_jar:
	rm -rf jar/
	
clean_bytecode:
	rm -r src/*/*.class
	
clean_docs:
	rm -rf docs/
