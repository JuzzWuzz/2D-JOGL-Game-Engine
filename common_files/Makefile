PLATFORM=i586
GAMEENGINE=2DGame_2.0.jar
LIBCLASSFILES="`pwd`/../OS_specific_files/linux-$(PLATFORM)/libs/jogl-all.jar":"`pwd`/../OS_specific_files/linux-$(PLATFORM)/libs/jogl-all-natives-linux-$(PLATFORM).jar":"`pwd`/../OS_specific_files/linux-$(PLATFORM)/libs/gluegen-rt.jar":"`pwd`/../OS_specific_files/linux-$(PLATFORM)/libs/gluegen-rt-natives-linux-$(PLATFORM).jar":"`pwd`/../OS_specific_files/linux-$(PLATFORM)/libs/joal.jar":"`pwd`/../OS_specific_files/linux-$(PLATFORM)/libs/joal-natives-linux-$(PLATFORM).jar"
ENV=LD_LIBRARY_PATH="`pwd`/../OS_specific_files/linux-$(PLATFORM)/libs/" CLASSPATH="`pwd`/jar/$(GAMEENGINE)":$(LIBCLASSFILES)
JAVACMD=javac

all: jar docs

jar: bytecode
	cd src && jar cf $(GAMEENGINE) GameEngine/*.class && cd .. || cd ..
	mkdir jar ; mv src/$(GAMEENGINE) jar/$(GAMEENGINE)
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