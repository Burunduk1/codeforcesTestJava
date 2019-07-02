# https://codeforces.com/blog/entry/43696

function echoerr { echo "$@" 1>&2; }
function echoColor { echo -ne "\e[38;5;$1m$2\e[0m"; }

#/usr/lib/jvm/java-8-oracle/jre/bin/java
#/usr/lib/jvm/jdk-11.0.2/bin/java
#/usr/lib/jvm/jdk-11.0.3+7/bin/java
#/usr/lib/jvm/jdk-12.0.1/bin/java
#/usr/lib/jvm/jdk8u212-b04/bin/java
#/usr/lib/jvm/openjdk-12.0.1/bin/java

function compileJavaJar {
	echoerr compileJava: \"$1.java to $1.jar\"
	echo "Main-Class: $1" > manifest.txt
	javac $1.java || exit 1
	jar -cvfm $1.jar manifest.txt *.class || exit 1
	rm *.class manifest.txt
}

defines="-Duser.language=en -Duser.region=US -Duser.variant=US"
testfile=17

function runJavaJar2 {
	flags="-XX:NewRatio=5 -XX:+AggressiveOpts"
	#flags="-XX:NewRatio=5"
	#flags="-XX:NewRatio=5 -XX:+UseSerialGC -XX:TieredStopAtLevel=1"
	timeout ${TL}s java -jar $flags $memory $* < $testfile
}

function testJava {
	compileJavaJar CF1 2>&1 >/dev/null
	compileJavaJar MemoryTest 2>&1 >/dev/null
	
	TL=100; memory="-Xms8M -Xss64M -Xmx128M"
	
	echo -n "[CF1:17]"

	runJavaJar2 CF1.jar >/dev/null 2>/dev/null && echoColor 2 "OK."
	if [ $? -ne 0 ]; then
		echoColor 1 "ML." ; false ; return		
	fi

	runJavaJar2 CF1.jar >/dev/null 2>/dev/null && echoColor 2 "OK."
	if [ $? -ne 0 ]; then
		echoColor 1 "TL." ; false ; return		
	fi

	TL=100; memory="-Xms8M -Xss64M -Xmx256M"

	echo -n "[MemoryTest]"

	runJavaJar2 MemoryTest.jar 1 200 false >/dev/null 2>/dev/null && echoColor 2 "OK."
	if [ $? -ne 0 ]; then
		echoColor 1 "ML !!!" ; false ; return		
	fi
	
	runJavaJar2 MemoryTest.jar 10 200 false >/dev/null 2>/dev/null && echoColor 2 "OK."
	if [ $? -ne 0 ]; then
		echoColor 1 "ML." ; false ; return		
	fi

	echo -n "FINISH."
	true
}

sudo update-alternatives --list java
n=`sudo update-alternatives --list java | wc -l `
echo "check $n versions"
for i in `seq 1 $n` ; do
	echo $i | sudo update-alternatives --config java > /dev/null
	echo $i | sudo update-alternatives --config javac > /dev/null
	javaVersion=`java -version 2>&1 | head -n 1`
	echo -e "\e[38;5;4m Test $javaVersion\e[0m"
	javaVersion=`echo $javaVersion | awk '{print $3}'`
	#echo $javaVersion
	testJava
	if [[ $? -eq 0 ]] ; then
		echoColor 10 " OK $javaVersion\n"
	else
		echoColor 1 " FAILED $javaVersion\n"
	fi
done

exit

# compileJavaJar CF1
# memory="-Xms8M -Xss64M -Xmx128M"
# time runJavaJar0 CF1.jar
# time runJavaJar1 CF1.jar
# time runJavaJar2 CF1.jar

# compileJavaJar MemoryTest
# memory="-Xms8M -Xss64M -Xmx256M"
# runJavaJar0 MemoryTest.jar 1 200 false
# runJavaJar1 MemoryTest.jar 1 200 false
# runJavaJar2 MemoryTest.jar 1 200 false
# runJavaJar0 MemoryTest.jar 10 200 false
# runJavaJar1 MemoryTest.jar 10 200 false
# runJavaJar2 MemoryTest.jar 10 200 false

