import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Event {
	int id;
	int x;
	int y;
	List<Ticket> tickets;
	double cheapestTicket = Double.MAX_VALUE;
		
	public Event(int id, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.tickets = generateTickets();
	}
	
	private List<Ticket> generateTickets() {
		List<Ticket> tickets = new ArrayList<>();
		Random r = new Random();
		// generate number of tickets
		// google shows that the biggest stadium in the world can hold less than 120,000 people
		// assume lowest ticket price >= 1.0
		int num_tickets = r.nextInt(120000);
		for (int i = 0; i < num_tickets; i++) {
			double price = 1.0 + (100000.0 - 1.0) * r.nextDouble();
			cheapestTicket = Math.min(cheapestTicket, price);
			Ticket ticket = new Ticket(price);
			tickets.add(ticket);
		}
		return tickets;
	}
}
