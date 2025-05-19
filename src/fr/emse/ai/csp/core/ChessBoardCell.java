package fr.emse.ai.csp.core;

public class ChessBoardCell {
	private int l;
	private int c;

	public ChessBoardCell(int l, int c) {
		this.l = l;
		this.c = c;
	}

	
	public int getL() {
		return l;
	}
	public void setL(int l) {
		this.l = l;
	}
	public int getC() {
		return c;
	}
	public void setC(int c) {
		this.c = c;
	}
	
	public boolean sameLine(int l) {
		return l == this.l;
	}
	
	public boolean sameColumn(int c) {
		return c == this.c;
	}
	
	public boolean samediagonal(int _l, int _c) {
		return (Math.abs(l-_l)== Math.abs(c-_c));
	}
	@Override
	public String toString() {
		return "(" + l + "," + c + ")";
	}
	
}