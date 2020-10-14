import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Minesweeper extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final int HEIGHT = 760;
	private static final int WIDTH = 760;
	private static final int ROWS = 16;
	private static final int COLS = 16;
	private static final int MINES = 16;
	private static int minesLeft = Minesweeper.MINES;
	private static int actualMinesLeft = Minesweeper.MINES;
	private static final String FLAGGED = "@";
	private static final String MINE = "M";
	
	private static final Color expColor = Color.lightGray;
	private static final Color colorMap[] = {Color.lightGray, Color.blue, Color.green, Color.cyan, Color.yellow,
	Color.orange, Color.pink, Color.magenta, Color.red, Color.red};
	
	private boolean running = true;
	
	private int[][] sGrid = new int[ROWS][COLS];
	
	public Minesweeper() {
		this.setTitle("MineSweap " + Minesweeper.minesLeft +" Mines left");
		this.setSize(WIDTH, HEIGHT);
		this.setResizable(false);
		this.setLayout(new GridLayout(ROWS, COLS, 0, 0));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.createContents();
		
		this.setMines();
		this.setVisible(true);
	}
	
	public void createContents() {
		for (int br = 0; br < ROWS; ++br) {
			for (int bc = 0; bc < COLS; ++bc) {
				
				sGrid[br][bc] = 0;
				
				MyJButton but = new MyJButton("", br, bc);
				but.addActionListener(new MyListener());
				this.add(but);
			}
		}
	}
	
	private class MyListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (running) {
				
				int mod = event.getModifiers();
				MyJButton jb = (MyJButton)event.getSource();
				
				boolean flagged = jb.getText().equals(Minesweeper.FLAGGED);
				boolean exposed = jb.getBackground().equals(expColor);
				
				if ( !flagged && !exposed && (mod & ActionEvent.CTRL_MASK) != 0 ) {
					jb.setText(Minesweeper.FLAGGED);
					Minesweeper.minesLeft--;
				
					if ( sGrid[jb.row][jb.col] == 9 ) {
						Minesweeper.actualMinesLeft--;
						if (actualMinesLeft == 0) {
							JOptionPane.showMessageDialog(null, "Would you look at that. You actually won!");
						}
						
					}
					setTitle("MineSweap " +
						Minesweeper.minesLeft + " Mines left");
				}
				
				else if ( flagged && !exposed && (mod & ActionEvent.ALT_MASK) != 0 ) {
					jb.setText("");
					++Minesweeper.minesLeft;
					
					if ( sGrid[jb.row][jb.col] == 9 ) {
						Minesweeper.actualMinesLeft++;
					}
					setTitle("MineSweap " +
						Minesweeper.minesLeft +" Mines left");
				}
				
				else if ( !flagged && !exposed) {
					exposeCell(jb);
				}
			}
	}
		public int to1D(int r, int c) {
			int lindex = (r * COLS) + c;
			return lindex;
		}
		
		public void exposeCell(MyJButton jb) {
			if ( !running )
				return;
			
			jb.setBackground(expColor);
			jb.setForeground(colorMap[sGrid[jb.row][jb.col]]);
			jb.setText(getStateStr(jb.row, jb.col));
			
			if ( sGrid[jb.row][jb.col] == 9 ) {
				for (int i = 0; i < ROWS; i++) {
					for (int j = 0; j < COLS; j++) {
						if (sGrid[i][j] == 9) {
							MyJButton mineButton = (MyJButton)jb.getParent().getComponent(to1D(i, j));
							mineButton.setBackground(expColor);
							mineButton.setForeground(colorMap[sGrid[i][j]]);
							mineButton.setText(getStateStr(i, j));
						}
					}
				}
				
				JOptionPane.showMessageDialog(null, "Lmao! You suck!");
				running = false;
				return;
			}
		
			if ( sGrid[jb.row][jb.col] == 0 ) {
				MyJButton neww;
				if (jb.row >= 0 && jb.row <= 15 && (jb.col - 1 >= 0 && jb.col - 1 <= 15) && sGrid[jb.row][jb.col - 1] >= 0 && sGrid[jb.row][jb.col - 1] <= 8) {
					neww = (MyJButton)jb.getParent().getComponent(to1D(jb.row, jb.col - 1));
					if (!neww.getBackground().equals(expColor)) {
						exposeCell(neww);
					}
				}
				if (jb.row - 1 >= 0 && jb.row - 1 <= 15 && (jb.col - 1 >= 0 && jb.col <= 15) && sGrid[jb.row - 1][jb.col - 1] >= 0 && sGrid[jb.row - 1][jb.col - 1] <= 8) {
					neww = (MyJButton)jb.getParent().getComponent(to1D(jb.row - 1, jb.col - 1));
					if (!neww.getBackground().equals(expColor)) {
						exposeCell(neww);
					}
				}
				if (jb.row - 1 >= 0 && jb.row - 1 <= 15 && (jb.col >= 0 && jb.col <= 15) && sGrid[jb.row - 1][jb.col] >= 0 && sGrid[jb.row - 1][jb.col] <= 8) {
					neww = (MyJButton)jb.getParent().getComponent(to1D(jb.row - 1, jb.col));
					if (!neww.getBackground().equals(expColor)) {
						exposeCell(neww);
					}
				}
				if (jb.row - 1 >= 0 && jb.row - 1 <= 15 && (jb.col + 1 >= 0 && jb.col + 1 <= 15) && sGrid[jb.row - 1][jb.col + 1] >= 0 && sGrid[jb.row - 1][jb.col + 1] <= 8) {
					neww = (MyJButton)jb.getParent().getComponent(to1D(jb.row - 1, jb.col + 1));
					if (!neww.getBackground().equals(expColor)) {
						exposeCell(neww);
					}
				}
				if (jb.row >= 0 && jb.row <= 15 && (jb.col + 1 >= 0 && jb.col + 1 <= 15) && sGrid[jb.row][jb.col + 1] >= 0 && sGrid[jb.row][jb.col + 1] <= 8) {
					neww = (MyJButton)jb.getParent().getComponent(to1D(jb.row, jb.col + 1));
					if (!neww.getBackground().equals(expColor)) {
						exposeCell(neww);
					}
				}
				if (jb.row + 1 >= 0 && jb.row + 1 <= 15 && (jb.col + 1 >= 0 && jb.col + 1 <= 15) && sGrid[jb.row + 1][jb.col + 1] >= 0 && sGrid[jb.row + 1][jb.col + 1] <= 8) {
					neww = (MyJButton)jb.getParent().getComponent(to1D(jb.row + 1, jb.col + 1));
					if (!neww.getBackground().equals(expColor)) {
						exposeCell(neww);
					}
				}
				if (jb.row + 1 >= 0 && jb.row + 1 <= 15 && (jb.col >= 0 && jb.col <= 15) && sGrid[jb.row + 1][jb.col] >= 0 && sGrid[jb.row + 1][jb.col] <= 8) {
					neww = (MyJButton)jb.getParent().getComponent(to1D(jb.row + 1, jb.col));
					if (!neww.getBackground().equals(expColor)) {
						exposeCell(neww);
					}
				}
				if (jb.row + 1 >= 0 && jb.row + 1 <= 15 && (jb.col - 1 >= 0 && jb.col - 1 <= 15) && sGrid[jb.row + 1][jb.col - 1] >= 0 && sGrid[jb.row + 1][jb.col - 1] <= 8) {
					neww = (MyJButton)jb.getParent().getComponent(to1D(jb.row + 1, jb.col - 1));
					if (!neww.getBackground().equals(expColor)) {
						exposeCell(neww);
					}
				}			
			}
		}
	}
	
	public static void main(String[] args) {
		new Minesweeper();
	}
	
	private void setMines() {
		for (int i = 0; i < MINES; i++) {
			int row = (int)(Math.random() * ROWS);
			int col = (int)(Math.random() * COLS);
			
			if (this.sGrid[row][col] == 9) {
				i--;
			}
			else {
				this.sGrid[row][col] = 9;				
			}
		}
		
		for (int i = 0; i < MINES; i++) {
			for (int j = 0; j < MINES; j++) {
				if (this.sGrid[i][j] == 9 && (i != 0 && i != 15) && (j != 0 && j != 15)) {
					if (this.sGrid[i - 1][j - 1] != 9) {
						this.sGrid[i - 1][j - 1] = this.sGrid[i - 1][j - 1] + 1;
					}
					if (this.sGrid[i - 1][j] != 9) {
						this.sGrid[i - 1][j] = this.sGrid[i - 1][j] + 1;
					}
					if (this.sGrid[i - 1][j + 1] != 9) {
						this.sGrid[i - 1][j + 1] = this.sGrid[i - 1][j + 1] + 1;
					}
					if (this.sGrid[i][j - 1] != 9) {
						this.sGrid[i][j - 1] = this.sGrid[i][j - 1] + 1;
					}
					if (this.sGrid[i][j + 1] != 9) {
						this.sGrid[i][j + 1] = this.sGrid[i][j + 1] + 1;
					}
					if (this.sGrid[i + 1][j - 1] != 9) {
						this.sGrid[i + 1][j - 1] = this.sGrid[i + 1][j - 1] + 1;
					}
					if (this.sGrid[i + 1][j] != 9) {
						this.sGrid[i + 1][j] = this.sGrid[i + 1][j] + 1;
					}
					if (this.sGrid[i + 1][j + 1] != 9) {
						this.sGrid[i + 1][j + 1] = this.sGrid[i + 1][j + 1] + 1;
					}										
				}
				
				if (this.sGrid[i][j] == 9 && (i == 0 && (j != 0 && j != 15))) {
					if (this.sGrid[i][j - 1] != 9) {
						this.sGrid[i][j - 1] = this.sGrid[i][j - 1] + 1;
					}
					if (this.sGrid[i][j + 1] != 9) {
						this.sGrid[i][j + 1] = this.sGrid[i][j + 1] + 1;
					}
					if (this.sGrid[i + 1][j - 1] != 9) {
						this.sGrid[i + 1][j - 1] = this.sGrid[i + 1][j - 1] + 1;
					}
					if (this.sGrid[i + 1][j + 1] != 9) {
						this.sGrid[i + 1][j + 1] = this.sGrid[i + 1][j + 1] + 1;
					}
					if (this.sGrid[i + 1][j] != 9) {
						this.sGrid[i + 1][j] = this.sGrid[i + 1][j] + 1;
					}					
				}
				
				if (this.sGrid[i][j] == 9 && (i == 15 && (j != 0 && j != 15))) {
					if (this.sGrid[i][j - 1] != 9) {
						this.sGrid[i][j - 1] = this.sGrid[i][j - 1] + 1;
					}
					if (this.sGrid[i][j + 1] != 9) {
						this.sGrid[i][j + 1] = this.sGrid[i][j + 1] + 1;
					}
					if (this.sGrid[i - 1][j - 1] != 9) {
						this.sGrid[i - 1][j - 1] = this.sGrid[i - 1][j - 1] + 1;
					}
					if (this.sGrid[i - 1][j + 1] != 9) {
						this.sGrid[i - 1][j + 1] = this.sGrid[i - 1][j + 1] + 1;
					}
					if (this.sGrid[i - 1][j] != 9) {
						this.sGrid[i - 1][j] = this.sGrid[i - 1][j] + 1;
					}					
				}
				
				if (this.sGrid[i][j] == 9 && (j == 0 && (i != 0 && i != 15))) {
					if (this.sGrid[i - 1][j] != 9) {
						this.sGrid[i - 1][j] = this.sGrid[i - 1][j] + 1;
					}
					if (this.sGrid[i + 1][j] != 9) {
						this.sGrid[i + 1][j] = this.sGrid[i + 1][j] + 1;
					}
					if (this.sGrid[i - 1][j + 1] != 9) {
						this.sGrid[i - 1][j + 1] = this.sGrid[i - 1][j + 1] + 1;
					}
					if (this.sGrid[i + 1][j + 1] != 9) {
						this.sGrid[i + 1][j + 1] = this.sGrid[i + 1][j + 1] + 1;
					}
					if (this.sGrid[i][j + 1] != 9) {
						this.sGrid[i][j + 1] = this.sGrid[i][j + 1] + 1;
					}					
				}
				
				if (this.sGrid[i][j] == 9 && (j == 15 && (i != 0 && i != 15))) {
					if (this.sGrid[i - 1][j] != 9) {
						this.sGrid[i - 1][j] = this.sGrid[i - 1][j] + 1;
					}
					if (this.sGrid[i + 1][j] != 9) {
						this.sGrid[i + 1][j] = this.sGrid[i + 1][j] + 1;
					}
					if (this.sGrid[i - 1][j - 1] != 9) {
						this.sGrid[i - 1][j - 1] = this.sGrid[i - 1][j - 1] + 1;
					}
					if (this.sGrid[i + 1][j - 1] != 9) {
						this.sGrid[i + 1][j - 1] = this.sGrid[i + 1][j - 1] + 1;
					}
					if (this.sGrid[i][j - 1] != 9) {
						this.sGrid[i][j - 1] = this.sGrid[i][j - 1] + 1;
					}					
				}
				
				if (this.sGrid[i][j] == 9 && (i == 0 && j == 0)) {
					if (this.sGrid[i + 1][j] != 9) {
						this.sGrid[i + 1][j] = this.sGrid[i + 1][j] + 1;
					}
					if (this.sGrid[i + 1][j + 1] != 9) {
						this.sGrid[i + 1][j + 1] = this.sGrid[i + 1][j + 1] + 1;
					}
					if (this.sGrid[i][j + 1] != 9) {
						this.sGrid[i][j + 1] = this.sGrid[i][j + 1] + 1;
					}					
				}
				
				if (this.sGrid[i][j] == 9 && (i == 15 && j == 0)) {
					if (this.sGrid[i - 1][j] != 9) {
						this.sGrid[i - 1][j] = this.sGrid[i - 1][j] + 1;
					}
					if (this.sGrid[i - 1][j + 1] != 9) {
						this.sGrid[i - 1][j + 1] = this.sGrid[i - 1][j + 1] + 1;
					}
					if (this.sGrid[i][j + 1] != 9) {
						this.sGrid[i][j + 1] = this.sGrid[i][j + 1] + 1;
					}					
				}
				
				if (this.sGrid[i][j] == 9 && (i == 0 && j == 15)) {
					if (this.sGrid[i + 1][j] != 9) {
						this.sGrid[i + 1][j] = this.sGrid[i + 1][j] + 1;
					}
					if (this.sGrid[i + 1][j] != 9) {
						this.sGrid[i + 1][j] = this.sGrid[i + 1][j] + 1;
					}
					if (this.sGrid[i + 1][j - 1] != 9) {
						this.sGrid[i + 1][j - 1] = this.sGrid[i + 1][j - 1] + 1;
					}
					if (this.sGrid[i][j - 1] != 9) {
						this.sGrid[i][j - 1] = this.sGrid[i][j - 1] + 1;
					}					
				}
				
				if (this.sGrid[i][j] == 9 && (i == 15 && j == 15)) {
					if (this.sGrid[i - 1][j] != 9) {
						this.sGrid[i - 1][j] = this.sGrid[i - 1][j] + 1;
					}
					if (this.sGrid[i - 1][j - 1] != 9) {
						this.sGrid[i - 1][j - 1] = this.sGrid[i - 1][j - 1] + 1;
					}
					if (this.sGrid[i][j - 1] != 9) {
						this.sGrid[i][j - 1] = this.sGrid[i][j - 1] + 1;
					}
					
				}
			}
		}
	}
	
	private String getStateStr(int row, int col) {
		if ( this.sGrid[row][col] == 0 )
			return "";
	
		else if ( this.sGrid[row][col] > 0 && this.sGrid[row][col] < 9 )
			return "" + this.sGrid[row][col];
	
		else
			return Minesweeper.MINE;
	}
}
