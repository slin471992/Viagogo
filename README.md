************************************************************************************************************************
The Solution is written in Java. 

Instruction to build & run:

Option 1: add this project to a Java IDE (i.e. Eclipse) and run.

Option 2: In command line (terminal for Mac), navigate to the "src" directory of this project,
		  type "javac Solution.java", then type "java Solution" to run.

************************************************************************************************************************
Assumptions: 
1. Coordinates are integers.

2. The world can have zero or more events.

3. Each event has less than 120,000 tickets. 
(Based on my result from google search, the biggest stadium in the world can hold less than 120,000 people).

4. Each ticket is at least $1.00 US dollar.
(Based on my real life experience, the cheapest ticket is at least $1.00 US dollar)

************************************************************************************************************************
Q & A:
1. Q: How might you change your program if you needed to support multiple events at the same location?
A: If there are more than one events at the same location, I would use a Location class to define each location. 
Events are recorded in a list of events in each location. I would use a Location[][] 2d array to record locations 
that have events. 

2. Q: How would you change your program if you were working with a much larger world size?
A: If the world size is much larger, we would still need to consider if the world is sparse or dense 
in terms of the number of events in the world. 
If the world is sparse (less events compare to world size), allocating a large size of Location[][] 2d array 
to represent the world and locations is a waste of memory, because there are many locations that do not have events at all.
Therefore, I would use a map which stores pairs of (Coordinates, List<Events>) to stores all coordinates that have events. 
The Coordinates class stores a pair of coordinates, i.e x and y. List<Events> stores a list of events that are at a specific location. 
If the world is tense (more events compare to world size), I would still use a Location[][] 2d array 
to record locations that have events. Location class stores a list of events. 
