import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class TextFile extends JFrame
{
	public TextFile(boolean maze[][])
	{
		super("Text File");
		
		System.out.println(maze.length);
		
		JTextField txtField = new JTextField("", maze.length + 2);
		
		add(txtField);
		
		txtField.setText(txtField.getText() + "#");
		for(int j = 0; j < maze.length; j++)
		{
			txtField.setText(txtField.getText() + "#");
		}
		txtField.setText(txtField.getText() + "#");
		for(int j = 0; j < maze.length; j++)
		{
			txtField.setText(txtField.getText() + "#");
			for(int i = 0; i < maze.length; i++)
			{
				if(maze[i][j])
				{
					txtField.setText(txtField.getText() + " ");
				}
				else
				{
					txtField.setText(txtField.getText() + "#");
				}
			}
			txtField.setText(txtField.getText() + "#");
			txtField.setText(txtField.getText() + "\n");
		}
		txtField.setText(txtField.getText() + "#");
		for(int j = 0; j < maze.length; j++)
		{
			txtField.setText(txtField.getText() + "#");
		}
		txtField.setText(txtField.getText() + "#");
		
		setVisible(true);
		setSize(500, 500);
	}
}
public class Maze extends JFrame implements ActionListener
{
	private JPanel 	pnlTiles 	= new JPanel();
	private JPanel 	pnlControls = new JPanel();
	
	private JLabel 	lblTiles[][];
	private JLabel	lblX		= new JLabel("   X:   ");
	private JLabel	lblY		= new JLabel("   Y:   ");
	private JLabel	lblSpeed	= new JLabel("   Speed:   ");
	
	private JTextField txtX 	= new JTextField(2);
	private JTextField txtY 	= new JTextField(2);
	private JTextField txtSpeed	= new JTextField("10", 2);
	
	private JButton btnGenerate = new JButton("Generate");
	private JButton btnSolve	= new JButton("Solve");
	private JButton btnMinus	= new JButton("-");
	private JButton btnPlus		= new JButton("+");
	
	private Timer 	tmrGenerating 	= new Timer(50, this);
	private Timer	tmrSolving		= new Timer(50, this);
	
	final boolean WALL = false;
	final boolean FLOOR = true;
	
	final int NORTH 	= 0;
	final int WEST 		= 1;
	final int EAST 		= 2;
	final int SOUTH		= 3;
	final int NORTHWEST = 4;
	final int NORTHEAST = 5;
	final int SOUTHWEST = 6;
	final int SOUTHEAST = 7;
	
	boolean maze[][];
	
	int X_MAX;
	int Y_MAX;
	
	int ENTR_X;
	int ENTR_Y;
	
	int EXIT_X;
	int EXIT_Y;
	
	Stack stack;
	
	public Maze(int x, int y)
	{
		super("Maze Generator and Solver using Depth-First Search Algorithm");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		btnGenerate.addActionListener(this);
		btnSolve.addActionListener(this);
		btnPlus.addActionListener(this);
		btnMinus.addActionListener(this);
		
		pnlTiles.setLayout(new GridLayout(x, y));
		
		X_MAX = x;
		Y_MAX = y;
		
		lblTiles = new JLabel[x][y];
		
		for(int i = 0; i < x; i++)
		{
			for(int j = 0; j < y; j++)
			{
				lblTiles[i][j] = new JLabel("");
				lblTiles[i][j].setOpaque(true);
				lblTiles[i][j].setBackground(Color.BLACK);
				pnlTiles.add(lblTiles[i][j]);
			}
		}
		
		pnlControls.add(lblX);
		pnlControls.add(txtX);
		pnlControls.add(lblY);
		pnlControls.add(txtY);
		pnlControls.add(btnGenerate);
		pnlControls.add(btnSolve);
		pnlControls.add(lblSpeed);
		pnlControls.add(txtSpeed);
		pnlControls.add(btnMinus);
		pnlControls.add(btnPlus);
		
		btnSolve.setEnabled(false);
		txtSpeed.setEditable(false);
		
		add(pnlTiles, 		BorderLayout.CENTER);
		add(pnlControls,	BorderLayout.SOUTH);
	}
	public void generate()
	{
		int x = X_MAX;
		int y = Y_MAX;
		
		pnlTiles.removeAll();
		pnlTiles.setLayout(new GridLayout(x, y));
		
		lblTiles = new JLabel[x][y];
		
		for(int i = 0; i < x; i++)
		{
			for(int j = 0; j < y; j++)
			{
				lblTiles[i][j] = new JLabel("");
				lblTiles[i][j].setOpaque(true);
				lblTiles[i][j].setBackground(Color.BLACK);
				pnlTiles.add(lblTiles[i][j]);
				System.out.println((i * x) + j);
			}
		}
		
		add(pnlTiles, BorderLayout.CENTER);
		
		//btnGenerate.setEnabled(false);
		//btnSolve.setEnabled(false);
		
		setSize(0, 0);
		setSize(500, 500);
		
		stack = new Stack();
		maze = new boolean[x][y];
		stack.push(x/2, y/2);
		
		tmrGenerating.setInitialDelay(500);
		tmrGenerating.start();
	}
	public void solve()
	{	
		//btnGenerate.setEnabled(false);
		//btnSolve.setEnabled(false);
		
		stack = new Stack();
		stack.push(ENTR_X, ENTR_Y);
		
		tmrSolving.setInitialDelay(500);
		tmrSolving.start();
	}
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource() == tmrGenerating) 
		{
			int i = stack.topX();
			int j = stack.topY();
			
			while(maze[i][j] == FLOOR || solidWall(i, j))
			{
				stack.pop();
				if(stack.isEmpty())
				{
					tmrGenerating.stop();
					findEntranceAndExit();
					btnGenerate.setEnabled(true);
					btnSolve.setEnabled(true);
					return;
				}
				i = stack.topX();
				j = stack.topY();
			}
			
			maze[i][j] = FLOOR;
			lblTiles[i][j].setBackground(Color.WHITE);
			
			int directions[] = {NORTH, WEST, SOUTH, EAST};
			directions = randomizeArray(directions);
			
			for(int direction : directions)
			{
				switch(direction)
				{
					case NORTH:
					if(!solidWall(i, j-1))
					{
						stack.push(i, j-1);
					}
					break;
					
					case WEST:
					if(!solidWall(i-1, j))
					{
						stack.push(i-1, j);
					}
					break;
					
					case EAST:
					if(!solidWall(i+1, j))
					{
						stack.push(i+1, j);
					}
					break;
					
					case SOUTH:
					if(!solidWall(i, j+1))
					{
						stack.push(i, j+1);
					}
					break;
				}
			}
		}
		else if(ae.getSource() == tmrSolving)
		{
			int i = stack.topX();
			int j = stack.topY();
			
			maze[i][j] = WALL;
			lblTiles[i][j].setBackground(Color.BLUE); 
			
			if(i == EXIT_X && j == EXIT_Y)
			{
				tmrSolving.stop();
				btnGenerate.setEnabled(true);
				return;
			}
			if(possiblePaths4(i, j) == 0)
			{
				lblTiles[i][j].setBackground(Color.WHITE);
				stack.pop();
				return;
			}
			int directions[] = {WEST, NORTH, SOUTH, EAST};
			
			for(int direction : directions)
			{
				switch(direction)
				{
					case NORTH:
					if(j == 0) break;
					if(maze[i][j-1] == FLOOR) stack.push(i, j-1);
					break;
					
					case WEST:
					if(i == 0) break;
					if(maze[i-1][j] == FLOOR) stack.push(i-1, j);
					break;
					
					case EAST:
					if(i == X_MAX-1) break;
					if(maze[i+1][j] == FLOOR) stack.push(i+1, j);
					break;
					
					case SOUTH:
					if(j == Y_MAX-1) break;
					if(maze[i][j+1] == FLOOR) stack.push(i, j+1);
					break;
				}
			}
		}
		else if(ae.getSource() == btnGenerate)
		{
			try
			{
				int x = Integer.parseInt(txtX.getText());
				int y = Integer.parseInt(txtY.getText());
				
				if(x >= 1001 || y >= 1001)
				{
					JOptionPane.showMessageDialog(this, "Maze size is too large.");
					throw new ArrayIndexOutOfBoundsException();
				}
				else if(x <= 3 || y <= 3)
				{
					JOptionPane.showMessageDialog(this, "Maze size is too small.");
					throw new ArrayIndexOutOfBoundsException();
				}
				
				X_MAX = x;
				Y_MAX = y;
				
				generate();
			} 
			catch(NumberFormatException e)
			{
				JOptionPane.showMessageDialog(this, "Please input numerical values only.");
				return;
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				return;
			}
		}
		else if(ae.getSource() == btnSolve)
		{
			solve();
		}
		else if(ae.getSource() == btnPlus)
		{
			int speed = Integer.parseInt(txtSpeed.getText());
			if(speed == 1) btnMinus.setEnabled(true);
			speed++;
			tmrGenerating.setDelay(100 - (speed) * 5);
			tmrSolving.setDelay(100 - (speed) * 5);
			txtSpeed.setText("" + (speed));
			if(speed == 20) btnPlus.setEnabled(false);
		}
		else if(ae.getSource() == btnMinus)
		{
			int speed = Integer.parseInt(txtSpeed.getText());
			if(speed == 20) btnPlus.setEnabled(true);
			speed--;
			tmrGenerating.setDelay(100 - (speed) * 5);
			tmrSolving.setDelay(100 - (speed) * 5);
			txtSpeed.setText("" + (speed));
			if(speed == 1) btnMinus.setEnabled(false);
		}	
	}
	private int[] randomizeArray(int array[])
	{
		int temp;
		if((int)(Math.random() * 2) == 0)
		{
			temp = array[0];
			array[0] = array[array.length-1];
			array[array.length-1] = temp;
		}
		for(int i = 0; i < array.length; i++)
		{
			for(int j = 0; j < array.length-1; j++)
			{
				if((int)(Math.random() * 2) == 0)
				{
					temp = array[j];
					array[j] = array[j+1];
					array[j+1] = temp;
				}
			}
		}
		return array;
	}
	private boolean solidWall(int x, int y)
	{
		if(possiblePaths8(x, y) >= 3) 	return true;
		else							return false;
	}
	private int possiblePaths4(int x, int y)
	{
		int directions[] = {NORTH, WEST, SOUTH, EAST};
		return countAllPossiblePaths(x, y, directions);
	}
	private int possiblePaths8(int x, int y)
	{
		int directions[] = {NORTH, WEST, SOUTH, EAST, NORTHWEST, NORTHEAST, SOUTHWEST, SOUTHEAST};
		return countAllPossiblePaths(x, y, directions);
	}
	private int countAllPossiblePaths(int x, int y, int directions[])
	{
		int possiblePaths = directions.length;
		for(int direction : directions)
		{
			try {
				switch(direction)
				{
					case NORTH:
					if(y == 0) break;
					if(maze[x][y-1] == WALL)
					{
						possiblePaths--;
					}
					break;
					
					case WEST:
					if(x == 0) break;
					if(maze[x-1][y] == WALL)
					{
						possiblePaths--;
					}
					break;
					
					case EAST:
					if(x == X_MAX-1) break;
					if(maze[x+1][y] == WALL)
					{
						possiblePaths--;
					}
					break;
					
					case SOUTH:
					if(y == Y_MAX-1) break;
					if(maze[x][y+1] == WALL)
					{
						possiblePaths--;
					}
					break;
					
					case NORTHWEST:
					if(y == 0) break;
					if(x == 0) break;
					if(maze[x-1][y-1] == WALL)
					{
						possiblePaths--;
					}
					break;
					
					case NORTHEAST:
					if(y == 0) break;
					if(x == X_MAX-1) break;
					if(maze[x+1][y-1] == WALL)
					{
						possiblePaths--;
					}
					break;
					
					case SOUTHWEST:
					if(y == Y_MAX-1) break;
					if(x == 0) break;
					if(maze[x-1][y+1] == WALL)
					{
						possiblePaths--;
					}
					break;
					
					case SOUTHEAST:
					if(y == Y_MAX-1) break;
					if(x == X_MAX-1) break;
					if(maze[x+1][y+1] == WALL)
					{
						possiblePaths--;
					}
					break;
				}
			} catch(ArrayIndexOutOfBoundsException e) {}
		}
		return possiblePaths;
	}
	private void findEntranceAndExit()
	{	
		int x = 1;
		int y = 1;
		try 
		{
			switch((int)(Math.random() * 2))
			{
				case 0:
				while(maze[x][y] == WALL || possiblePaths4(x, y) != 1) x++;
				y--;
				break;
				
				case 1:
				while(maze[x][y] == WALL || possiblePaths4(x, y) != 1) y++;
				x--;
				break;
			}
		} 
		catch(ArrayIndexOutOfBoundsException e)
		{
			x = 1;
			y = 1;
			switch((int)(Math.random() * 2))
			{
				case 0:
				while(maze[x][y] == WALL) x++;
				y--;
				break;
				
				case 1:
				while(maze[x][y] == WALL) y++;
				x--;
				break;
			}
		}
		ENTR_X = x;
		ENTR_Y = y;
		x = X_MAX - 2;
		y = Y_MAX - 2;
		try
		{
			switch((int)(Math.random() * 2))
			{
				case 0:
				while(maze[x][y] == WALL || possiblePaths4(x, y) != 1) x--;
				y++;
				break;
				
				case 1:
				while(maze[x][y] == WALL || possiblePaths4(x, y) != 1) y--;
				x++;
				break;	
			}
		} 
		catch(ArrayIndexOutOfBoundsException e)
		{
			x = X_MAX - 2;
			y = Y_MAX - 2;
			switch((int)(Math.random() * 2))
			{
				case 0:
				while(maze[x][y] == WALL) x--;
				y++;
				break;
				
				case 1:
				while(maze[x][y] == WALL) y--;
				x++;
				break;
			}
		}
		EXIT_X = x;
		EXIT_Y = y;
		
		maze[ENTR_X][ENTR_Y] = FLOOR;
		lblTiles[ENTR_X][ENTR_Y].setBackground(Color.GREEN);
		
		maze[EXIT_X][EXIT_Y] = FLOOR;
		lblTiles[EXIT_X][EXIT_Y].setBackground(Color.RED);
	}
	public static void main(String args[])
	{
		Maze maze = new Maze(1, 1);
		maze.setSize(500, 500);
		maze.setVisible(true);
	}
}
