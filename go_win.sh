# https://codeforces.com/blog/entry/43696

function echoerr { echo "$@" 1>&2; }
function echoColor { echo -ne "\e[38;5;$1m$2\e[0m"; } 

function compileJavaJar {
	echoerr compileJava: \"$1.java to $1.jar\"
	echo "Main-Class: $1" > manifest.txt
	"$JAVADIR"/javac $1.java >/dev/null 2>/dev/null || exit 1
	"$JAVADIR"/jar -cvfm $1.jar manifest.txt *.class >/dev/null 2>/dev/null || exit 1
	rm *.class manifest.txt
}

LOG=musinTimes.log
rm $LOG

defines="-Duser.language=en -Duser.region=US -Duser.variant=US"
#flags="-XX:NewRatio=5"
#flags="-XX:NewRatio=5 -XX:+AggressiveOpts"
flags="-XX:NewRatio=5 -XX:+UseSerialGC -XX:TieredStopAtLevel=1"

function runJavaJar2 {
	"$JAVADIR"/java -jar $flags $memory $defines $* < $testfile >/dev/null 2>/dev/null
}
function getTime {
	# windows has no "timeout" command. 
	(time "$JAVADIR"/java -jar $flags $memory $defines $* < $testfile >/dev/null) 2>&1 | grep real | awk '{print $2;}'
}

COLOR_OK=32
COLOR_FINALOK=42
COLOR_FAIL=41
COLOR_HEADER=44

function testJava {
	compileJavaJar CF1 
	compileJavaJar MemoryTest 
	compileJavaJar MusinQFMain 
	
	testfile=36
	memory="-Xms8M -Xss64M -Xmx512M"
	
	echo $javaVersion `getTime MusinQFMain.jar` >> $LOG

	testfile=17
	memory="-Xms8M -Xss64M -Xmx128M"
	echo -n "[CF1:17]"

	runJavaJar2 CF1.jar >/dev/null 2>/dev/null && echoColor $COLOR_OK "OK."
	if [ $? -ne 0 ]; then
		echoColor $COLOR_FAIL "ML." ; false ; return		
	fi

	echo -n "[time=`getTime CF1.jar`]"

	memory="-Xms8M -Xss64M -Xmx256M"
	echo -n "[MemoryTest]"

	runJavaJar2 MemoryTest.jar 1 200 false >/dev/null 2>/dev/null && echoColor $COLOR_OK "OK."
	if [ $? -ne 0 ]; then
		echoColor $COLOR_FAIL "ML !!!" ; false ; return		
	fi
	
	runJavaJar2 MemoryTest.jar 10 200 false >/dev/null 2>/dev/null && echoColor $COLOR_OK "OK."
	if [ $? -ne 0 ]; then
		echoColor $COLOR_FAIL "ML." ; false ; return		
	fi

	echo -n "FINISH."
	true
}

function initAndTestJava {
	echo "test java: $1"

	JAVADIR="$1/bin"
	javaVersion=`"$JAVADIR"/java -version 2>&1 | head -n 1`
	#for c in `seq 1 100` ; do
	echoColor $COLOR_HEADER "Test $javaVersion\n"
	#done
	javaVersion=`echo $javaVersion | awk '{print $3}'`
	echo $javaVersion
	testJava
	if [[ $? -eq 0 ]] ; then
		echoColor $COLOR_FINALOK " OK $javaVersion\n"
	else
		echoColor $COLOR_FAIL " FAILED $javaVersion\n"
	fi
}

function readPaths {
	while read line ; do
		for javaDir in `ls -1 "$line"` ; do 
			initAndTestJava "$line/$javaDir"
		done
	done
}

cat win_dirs | readPaths
