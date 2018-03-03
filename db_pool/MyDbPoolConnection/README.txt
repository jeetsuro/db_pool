It is the child project to build MySql DB jar. 
In future it should be used by any other project which needs DB connection reference.

///////////////////////////
TO skip JNUT test case 
///////////////////////////

1) 
	mvn package -Dmaven.test.skip=true
	
2) Add in pom.xml

<properties>
    <maven.test.skip>true</maven.test.skip>
</properties>