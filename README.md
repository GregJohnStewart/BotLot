BotLot (Bot Pilot)
===================


This library gives a very open-ended and versatile base to organize navigational data. The idea being that this library can be used to help a robot (or anything else that needs help getting around an area) learn the area, and determine how to get around.

BotLot does this by keeping track of nodes and edges given to it by the driving application. From there, it can calculate:

 - Where is left to explore
 - How to get from where it is to where it wants to be.

The underlying structure is an [Adjacency List](https://www.khanacademy.org/computing/computer-science/algorithms/graph-representation/a/representing-graphs) that allows for any kind of area, no matter how complicated.

The whole library is included in one jar (located in /dist/), and is easy to implement wherever you need.

There is a working example program called `testDriver` (/src/testDriver.java) that demonstrates one possible implementation in a virtual environment.

