# Building POOI

**Pooi** was written using *IntelliJ IDEA*. The preferred method for building
the system is therefore using the IDE.

The following commands will download a copy of the repository and make IntelliJ IDEA open the project (provided that IntelliJ IDEA is installed in ~/bin):

    $ git clone https://github.com/Baltasarq/Pooi
    $ ~/bin/idea/bin/idea.sh Pooi/ &

After IntelliJ IDEA is open, the only further step needed is to click the *play* button.

If you prefer other enviornments, like **NetBeans** or **Eclipse**, then use the *File >> Import from 
sources* (or similar) option, and follow the instructions of that environment in order to execute it.

## I'm a die-hard command-line user

Okay.
You can use **Maven**. Once **Maven** is installed, and **Pooi** downloaded and 
unzipped in $SRC:

	$ cd $SRC
	$ mvn compile package
	

Execute it with the following command:

	$ java -jar target/pooi-stable-SNAPSHOT.jar
