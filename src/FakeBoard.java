
public class FakeBoard {
	private int[][] history;
	private int size;
	
	public FakeBoard(int s) {
		size = s;
		resetHistory();
	}
	
	public void resetHistory() {
		history = new int[size][size];
	}
	
	public boolean marked(int y, int x) {
		return history[y][x] == 1;
	}
	
	public void mark(int y, int x) {
		history[y][x] = 1;
	}
}
