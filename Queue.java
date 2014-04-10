class Node
{
	Node next;
	int x; int y;
	public Node(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}
public class Queue
{
	Node front;
	Node back;
	public void enqueue(int x, int y)
	{
		if(this.isEmpty())
		{
			front = back = new Node(x, y);
		}
		else
		{
			back.next = new Node(x, y);
			back = back.next;
		}
	}
	public int frontX()
	{
		return front.x;
	}
	public int frontY()
	{
		return front.y;
	}
	public Node dequeue()
	{
		Node current = front;
		front = front.next;
		return current;
	}
	public boolean isEmpty()
	{
		return (front == null);
	}
	public boolean isNotEmpty()
	{
		return !isEmpty();
	}
}