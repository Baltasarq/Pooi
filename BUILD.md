# Building *Pooi*

**Pooi** was written using *Java* and *IntelliJ IDEA*. You will need the [JDK (Java Development Kit)](http://www.oracle.com/technetwork/java/javase/downloads/) in order to modify *Pooi*. The preferred method for building the system is to use the aforementioned IDE. The [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/) is suitable for this.

The following commands will download a copy of the repository and make *IntelliJ IDEA* open the project (provided that *IntelliJ IDEA* is installed in ~/bin):

    $ git clone https://github.com/Baltasarq/Pooi
    $ ~/bin/idea/bin/idea.sh Pooi/ &

After *IntelliJ IDEA* is open, the only further step needed to have the project compiled and executed is to click the *play* button.

If you prefer other development environments, like **NetBeans** or **Eclipse**, then use the *File >> Import from 
sources* (or similar) option, and follow the instructions of that environment in order to execute it.

## I'm a die-hard command-line user

Okay.
You can use **Maven**. Once **Maven** is installed, and **Pooi** downloaded and 
unzipped in $SRC:

	$ cd $SRC
	$ mvn compile package
	

Execute it with the following command:

	$ java -jar target/pooi-stable-SNAPSHOT.jar
