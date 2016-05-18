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
public class Stack
{
	Node top;
	public void push(int x, int y)
	{
		if(this.isEmpty())
		{
			top = new Node(x, y);
		}
		else
		{
                        Node current = new Node(x, y);
                        current.next = top;
                        top = current;
		}
	}
	public int topX()
	{
		return top.x;
	}
	public int topY()
	{
		return top.y;
	}
	public Node pop()
	{
		Node current = top;
		top = top.next;
		return current;
	}
	public boolean isEmpty()
	{
		return (top == null);
	}
	public boolean isNotEmpty()
	{
		return !isEmpty();
	}
}
