package objects;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Board {
	private int x, y, mxn = 7, m[][] = {{-1, 0},{0, 1},{1, 0},{0, -1}};;
	private boolean vis[][] = new boolean[mxn+2][mxn+2];
	private Tile tile50, board[][] = new Tile[mxn+2][mxn+2];
	public Board() {
		Stack<Tile> tiles = new Stack<Tile>();
		Tile tile;
		Scanner in;
		try {
			in = new Scanner(new File("res/Tiles.txt"));
			in.useDelimiter(",");

			for (int i=0;i<50;i++) {
				String letter = in.next().replaceAll("\n", "").replaceAll("\r", "");
				String name = in.next();
				int rot = in.nextInt(), y = in.nextInt(), x = in.nextInt();
				tile = new Tile(letter, name, rot, x, y);
				if(i<16) {
					tile.rotate(rot*90, true, false);
					board[y][x] = tile;
				}
				else {
					tiles.add(tile);
				}
			}
			Collections.shuffle(tiles);

			for (int i=1;i<=mxn;i++) {
				for (int j=1;j<=mxn;j++) {
					if(board[i][j]==null) {
						int rot = (int) (Math.random() * 4 + 1);
						tile = tiles.pop();
						tile.setLeft(j); tile.setDown(i); tile.rotate(rot*90, true, true);
						board[i][j] = tile;
					}
				}
			}
			tile50 = tiles.pop();
			y = x = mxn+1;
			tile50.setDown(y); tile50.setLeft(x);
			board[y][x] = tile50;
		} catch (FileNotFoundException e) {
			System.out.println("ERROR. File not found.");
		}
	}
	//p: anything for right/down, - to go left/up
	//c: r to push row, c = to push column
	//n: row/col number to be pushed
	//Sample i/o:
	//+ r 1 --> shifts 1st row to the right
	//- c 2 --> shifts 2nd col up
	//+ r 8 --> shifts 8th row to the right (didn't include error checking)
	public void shiftTile(char p, char c, int n) {
		System.out.println(p+" "+c+" "+n);
		if(p=='-') { //push to the left or up
			for (int i=0;i<mxn;i++) {
				if(c=='r') swap(n, i, n, i+1);
				else swap(i, n, i+1, n);
			}
			if(c=='r') {
				swap(n, mxn);
				y = n; x = 0;
			} else {
				swap(mxn, n);
				y = 0; x = n;
			}
		} else { //push to the right or down
			for (int i=mxn+1;i>=2;i--) {
				if(c=='r') swap(n, i, n, i-1);
				else swap(i, n, i-1, n);
			}
			if(c=='r') {
				swap(n, 1);
				y = n; x = mxn+1;
			} else {
				swap(1, n);
				y = mxn+1; x = n;
			}
		}
		tile50 = board[y][x];
		System.out.println("Free: "+y+" "+x+" = "+board[y][x].getName());
		System.out.println("=============================");
	}
	public void swap(int y1, int x1) {
		board[y1][x1] = tile50;
		board[y1][x1].setDown(y1);
		board[y1][x1].setLeft(x1);
	}
	public void swap(int y1, int x1, int y2, int x2) {
		board[y1][x1] = board[y2][x2];
		board[y1][x1].setDown(y1);
		board[y1][x1].setLeft(x1);
	}
	//send y and x coords and it'll return a 2d array of the tiles that can be visited from (y, x)
	public boolean[][] getPath(int y, int x) {
		for (int i=0;i<=mxn+1;i++) {
			Arrays.fill(vis[i], false);
		}
		dfs(y, x);
		return vis;
	}
	public void dfs(int y, int x) {
		vis[y][x] = true;
		for (int i=0;i<4;i++) {
			int r = y+m[i][0], c = x+m[i][1];
			if(r<=0 || r>mxn || c<=0 || c>mxn || vis[r][c]) continue;
			if(!board[y][x].getMove()[i] || !board[r][c].getMove()[(i+2)%4]) continue;
			dfs(r, c);
		}
	}
	public Tile[][] getBoard() {
		return board;
	}
	public Tile getFreeTile() {
		return board[y][x];
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	@Override
	public String toString() {
		return "Board [board=" + Arrays.toString(board) + "]";
	}
}
