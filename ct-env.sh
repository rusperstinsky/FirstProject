
if [ "$CT_HOME" == "" ]
then 
	export IDEA_HOME=/usr/local/lib/idea/11.1.3/idea-soi/idea-IC-129.354
	export JAVA_HOME=/usr/local/java/jdk1.6.0_39
	export GROOVY_HOME="/home/sucursal/herramientas/groovy"
	export GRADLE_HOME="/home/sucursal/herramientas/gradle"
	export GRADLE_OPTS="-Xmx256m -Xms256m -XX:PermSize=128m -XX:MaxPermSize=128m"
	export CT_HOME=/ct
	export PATH=$JAVA_HOME/bin:$PATH:$IDEA_HOME/bin:$GROOVY_HOME/bin:$GRADLE_HOME/bin:$SOI_HOME/bin:./bin
	

fi
