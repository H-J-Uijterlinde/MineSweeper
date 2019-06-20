"# MineSweeper"

First real solo project.
The goal is to apply everything I have learned so far.
Trying to create a Minesweeper game from scratch.
Ultimately packaging it for distribution.

After cloning the source code run the following command from the applications root directory (for windows):
mvn clean package

this produces an executable jar file which can be run from the applications target folder by running the following command: 
java -jar MineSweeper-1.0-SNAPSHOT.jar

you can move the jar file from to applications target directory to anywhere you like, and run it just bij doulbe clicking on it.


# 19-06-2019
The first 'sprint' so to speak is finished resulting in an MVP.
The product can still use some enhancements, but most of them would involve reusing techniques I have already used before.
I do not consider this a priority at this time, because my goal is to master new techniques. 
I will keep collecting stories for the backlog untill they consitute enough work for a new sprint.
In the mean time I will work on new projects.

# 20-06-2019
Decided to implement one additional change, considering my primary goal was to produce an executable .jar file, which can be also be distributed as a single file. This required some changes, primarily in the way that the application loads it resources, like images.
Information on how this was achieved can be found in the comments inside the code.
