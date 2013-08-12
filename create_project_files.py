import sys

print 'Creating project files'

game_name = sys.argv[1]
os = sys.argv[2]
dest_dir = sys.argv[3]

classpfile = open (dest_dir+'/'+game_name+'_'+os+'/.classpath', 'w')

classpfile.write ('\
<?xml version="1.0" encoding="UTF-8"?> \n\
<classpath> \n\
	<classpathentry kind="src" path="src"/> \n\
	<classpathentry kind="lib" path="jar/2DGame_2.0.jar"/> \n\
	<classpathentry kind="lib" path="libs/gluegen-rt.jar"> \n\
		<attributes> \n\
			<attribute name="org.eclipse.jdt.launching.CLASSPATH_ATTR_LIBRARY_PATH_ENTRY" value="'+game_name+'/libs"/> \n\
		</attributes> \n\
	</classpathentry> \n\
	<classpathentry kind="lib" path="libs/gluegen-rt-natives-'+os+'.jar"> \n\
		<attributes> \n\
			<attribute name="org.eclipse.jdt.launching.CLASSPATH_ATTR_LIBRARY_PATH_ENTRY" value="'+game_name+'/libs"/> \n\
		</attributes> \n\
	</classpathentry> \n\
	<classpathentry kind="lib" path="libs/jogl-all.jar"> \n\
		<attributes> \n\
			<attribute name="org.eclipse.jdt.launching.CLASSPATH_ATTR_LIBRARY_PATH_ENTRY" value="'+game_name+'/libs"/> \n\
		</attributes> \n\
	</classpathentry> \n\
	<classpathentry kind="lib" path="libs/jogl-all-natives-'+os+'.jar"> \n\
		<attributes> \n\
			<attribute name="org.eclipse.jdt.launching.CLASSPATH_ATTR_LIBRARY_PATH_ENTRY" value="'+game_name+'/libs"/> \n\
		</attributes> \n\
	</classpathentry> \n\
	<classpathentry kind="lib" path="libs/joal.jar"> \n\
		<attributes> \n\
			<attribute name="org.eclipse.jdt.launching.CLASSPATH_ATTR_LIBRARY_PATH_ENTRY" value="'+game_name+'/libs"/> \n\
		</attributes> \n\
	</classpathentry> \n\
	<classpathentry kind="lib" path="libs/joal-natives-'+os+'.jar"> \n\
		<attributes> \n\
			<attribute name="org.eclipse.jdt.launching.CLASSPATH_ATTR_LIBRARY_PATH_ENTRY" value="'+game_name+'/libs"/> \n\
		</attributes> \n\
	</classpathentry> \n\
	<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER"/> \n\
	<classpathentry kind="output" path="bin"/> \n\
</classpath>')

classpfile.close()


profile = open (dest_dir+'/'+game_name+'_'+os+'/.project', 'w')

profile.write('\
<?xml version="1.0" encoding="UTF-8"?> \n\
<projectDescription> \n\
	<name>'+game_name+'</name> \n\
	<comment></comment> \n\
	<projects> \n\
	</projects> \n\
	<buildSpec> \n\
		<buildCommand> \n\
			<name>org.eclipse.jdt.core.javabuilder</name> \n\
			<arguments> \n\
			</arguments> \n\
		</buildCommand> \n\
	</buildSpec> \n\
	<natures> \n\
		<nature>org.eclipse.jdt.core.javanature</nature> \n\
	</natures> \n\
</projectDescription>')


profile.close()


