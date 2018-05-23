## Building POOI

**Pooi** was written using *IntelliJ IDEA*. The preferred method for building
the system is therefore using the IDE.

If you prefer other enviornments, like NetBeans or Eclipse, then use the *File >> Import from 
sources* (or similar), option.

# I'm a die-hard command-line user

Okay.
You can use **Maven**. Once **Maven** is installed, and **Pooi** downloaded and 
unzipped in $SRC:

	$ cd $SRC
	$ mvn compile package
	

Execute it with the following command:
	$ java -jar target/pooi-2.1-SNAPSHOT.jar
