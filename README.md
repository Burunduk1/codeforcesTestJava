# Install java, which you want to test

* https://adoptopenjdk.net/releases.html?variant=openjdk12&jvmVariant=hotspot#x32_win
* https://jdk.java.net/archive/
* https://jdk.java.net/12/
* https://jdk.java.net/13/

I prefer java8, java11, java12 to test.

Note, under windows you may choose x32, x64, under linux you have only x64.

# About tests

Read about tests: https://codeforces.com/blog/entry/43696

**Download and unzip test 17**: https://assets.codeforces.com/files/633E/17.zip

# Run script

The script is not very safe... so read it before running ;)

* For linux use **bash go.sh**
* For windows use **bash go_win.sh**

For windows check file **win_dirs**, it should contain all directories to your java versions.

# Choose java flags

Just change part of the script

```
function runJavaJar2 {
	#flags="-XX:NewRatio=5 -XX:+AggressiveOpts"
	#flags="-XX:NewRatio=5"
	flags="-XX:NewRatio=5 -XX:+UseSerialGC -XX:TieredStopAtLevel=1"
```
