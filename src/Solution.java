
import java.util.*;

public class Solution {
//  Assumption: 
//	1. Coordinates are integers.
//	2. The world can have zero or more events.
//	3. Each event has less than 120,000 tickets. 
//	(Based on my result from google search, the biggest stadium in the world can hold less than 120,000 people).
//	4. Each ticket is at least $1.00 US dollar.
//	(Based on my real life experience, the cheapest ticket is at least $1.00 US dollar)
	
	private final int MIN_COOR = -10;
	private final int MAX_COOR = 10;
	private final int MAX_EVENTS = 20 * 20;
	
	private int EventId = 0;
	
	public int events_number;
	// coordinates are from -10 to +10, for each pair of coordinates(x, y), 
	// map it into [x + 10][ y + 10] in the 2d array
	public Event[][] events = new Event[21][21];
	
	public static void main(String[] args) {
		Solution test = new Solution();
		// generate seed data
		System.out.println("Generating seed data...");
		test.generateEvents();
		System.out.println("total " + test.events_number + " events");
		
		// read user location as input
		int[] coordinates = test.readUserInput();
		int x = coordinates[0];
		int y = coordinates[1];

		List<Event> closestEvents = test.getClosestEvents(x, y);
		System.out.printf("Closet Events to (%d, %d):%n", x, y);
		if (closestEvents.size() == 0) {
			System.out.println("There is no events near you.");
		}
		for (Event e : closestEvents) {
			if (e.tickets.size() == 0) {
				System.out.printf("Event %d - No Tickets Left, Distance %d%n", 
						e.id, test.distance(x, y, e.x, e.y));	
			} else {
				System.out.printf("Event %d - $%.2f, Distance %d%n", 
						e.id, e.cheapestTicket, test.distance(x, y, e.x, e.y));
			}					
		}
	}
	
	private int[] readUserInput() {
		Scanner in = new Scanner(System.in);
		in.useDelimiter("[,\\s+]");
		int[] coordinates = new int[]{-11, -11};				
		while (coordinates[0] < -10 || coordinates[0] > 10 || coordinates[1] < -10 || coordinates[1] > 10) {
			int i = 0;
			System.out.println("Please Input Coordinates in format x,y between -10 and 10:");
			System.out.print(">");
			while (in.hasNextLine() && i < 2) {
				if (in.hasNextInt()) {
					coordinates[i] = in.nextInt();
					i++;
				} else {
					in.next();
				}
			}
		}
		in.close();
		return coordinates;
	}
	
	private void generateEvents() {
		// assumption: the world can have zero or more events
		// first generate number of events 
		Random r = new Random();
		events_number = r.nextInt(MAX_EVENTS + 1);
		// for each event, generate random pairs of coordinates (x, y)
		// each pair of coordinates only has one event, 
		for (int i = 0; i < events_number; i++) {
			int[] coordinates = generateCoor();
			Event event = new Event(++EventId, coordinates[0], coordinates[1]);	
			events[coordinates[0] + 10][coordinates[1] + 10] = event;
		}
	}
	
	private int[] generateCoor() {
		Random r = new Random();
		int x = r.nextInt(MAX_COOR + 1 - MIN_COOR) + MIN_COOR;
		int y = r.nextInt(MAX_COOR + 1 - MIN_COOR) + MIN_COOR;
		//System.out.println("x : " + x + "," + "y: " + y);
		while (events[x + 10][y + 10] != null) {
			x = r.nextInt(MAX_COOR + 1 - MIN_COOR) + MIN_COOR;
			y = r.nextInt(MAX_COOR + 1 - MIN_COOR) + MIN_COOR;
		}
		return new int[]{x, y};
	}
	
	private int distance(int start_x, int start_y, int end_x, int end_y) {
		// Manhattan Distance((x1, y1), (x2, y2)) = Abs(x1 - x2) + Abs(y1 - y2)
		return Math.abs(start_x - end_x) + Math.abs(start_y - end_y); 
	}
	
	public List<Event> getClosestEvents(int x, int y) {
		List<Event> result = new ArrayList<>();
		if (events_number == 0) {
			return result;
		}
		// use best first search, start from the initial coordinate (x, y), find five closest coordinates that have events
		PriorityQueue<Cell> pq = new PriorityQueue<>(events_number, new Comparator<Cell>() {
			@Override
			public int compare(Cell c1, Cell c2) {
				if (c1.distance == c2.distance) {
					return 0;
				}
				return c1.distance < c2.distance ? -1 : 1;
			}
		});
		// visited boolean array records visited cells
		boolean[][] visited = new boolean[21][21];
		int num = 0;
		pq.offer(new Cell(x, y, 0));
		visited[x + 10][y + 10] = true;
		
		while (!pq.isEmpty()) {
			Cell cell = pq.poll();
			if (events[cell.x + 10][cell.y + 10] != null) {
				result.add(events[cell.x + 10][cell.y + 10]);
				num++;
			}
			// go to right
			if (cell.x + 1 <= MAX_COOR && !visited[cell.x + 1 + 10][cell.y + 10]) {
				pq.offer(new Cell(cell.x + 1, cell.y, 1 + cell.distance));
				visited[cell.x + 1 + 10][cell.y + 10] = true;
			}
			// go up
			if (cell.y + 1 <= MAX_COOR && !visited[cell.x + 10][cell.y + 1 + 10]) {
				pq.offer(new Cell(cell.x, cell.y + 1, 1 + cell.distance));
				visited[cell.x + 10][cell.y + 1 + 10] = true;
			}
			// go to left
			if (cell.x - 1 >= MIN_COOR && !visited[cell.x - 1 + 10][cell.y + 10]) {
				pq.offer(new Cell(cell.x - 1, cell.y, 1 + cell.distance));
				visited[cell.x - 1 + 10][cell.y + 10] = true;
			}
			// go down
			if (cell.y - 1 >= MIN_COOR && !visited[cell.x + 10][cell.y - 1 + 10]) {
				pq.offer(new Cell(cell.x, cell.y - 1, 1 + cell.distance));
				visited[cell.x + 10][cell.y - 1 + 10] = true;
			}
			
			// early termination if we already get 5 closest events
			// or if we already get all events available
			if (num == 5 || result.size() == events_number) {
				return result;
			}	
		}
		
		return result;
	}
	
	static class Cell {
		int x;
		int y;
		int distance;
		
		Cell(int x, int y, int distance) {
			this.x = x;
			this.y = y;
			this.distance = distance;
		}
	}	
}
