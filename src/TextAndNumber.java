
public class TextAndNumber {
	
	public String [] Word;
	String [] Number;

	public TextAndNumber(){
		initWord();
		initNumber();
	}
	public void drawWord(int x, int y, String word, int width, int height){
		for(int i=0; i < word.length(); i++){
			char c = word.charAt(i);
			StdDraw.picture(x+i*width+i*5, y, Word[c-97],width,height);
		}
	}
	public void drawNumber(int x, int y, int number, int width, int height){
		StdDraw.picture(x, y, Number[number/10],width,height);
		StdDraw.picture(x+width+5, y, Number[number%10],width,height);
	}
	private void initWord(){
		Word = new String[28];
		Word[0] = "Images/NumberWord/Word/A.png";
		Word[1] = "Images/NumberWord/Word/B.png";
		Word[2] = "Images/NumberWord/Word/C.png";
		Word[3] = "Images/NumberWord/Word/D.png";
		Word[4] = "Images/NumberWord/Word/E.png";
		Word[5] = "Images/NumberWord/Word/F.png";
		Word[6] = "Images/NumberWord/Word/G.png";
		Word[7] = "Images/NumberWord/Word/H.png";
		Word[8] = "Images/NumberWord/Word/I.png";
		Word[9] = "Images/NumberWord/Word/J.png";
		Word[10] = "Images/NumberWord/Word/K.png";
		Word[11] = "Images/NumberWord/Word/L.png";
		Word[12] = "Images/NumberWord/Word/M.png";
		Word[13] = "Images/NumberWord/Word/N.png";
		Word[14] = "Images/NumberWord/Word/O.png";
		Word[15] = "Images/NumberWord/Word/P.png";
		Word[16] = "Images/NumberWord/Word/Q.png";
		Word[17] = "Images/NumberWord/Word/R.png";
		Word[18] = "Images/NumberWord/Word/S.png";
		Word[19] = "Images/NumberWord/Word/T.png";
		Word[20] = "Images/NumberWord/Word/U.png";
		Word[21] = "Images/NumberWord/Word/V.png";
		Word[22] = "Images/NumberWord/Word/W.png";
		Word[23] = "Images/NumberWord/Word/X.png";
		Word[24] = "Images/NumberWord/Word/Y.png";
		Word[25] = "Images/NumberWord/Word/Z.png";
	}
	
	private void initNumber(){
		Number = new String[10];
		Number[0] = "Images/NumberWord/Number/0.png";
		Number[1] = "Images/NumberWord/Number/1.png";
		Number[2] = "Images/NumberWord/Number/2.png";
		Number[3] = "Images/NumberWord/Number/3.png";
		Number[4] = "Images/NumberWord/Number/4.png";
		Number[5] = "Images/NumberWord/Number/5.png";
		Number[6] = "Images/NumberWord/Number/6.png";
		Number[7] = "Images/NumberWord/Number/7.png";
		Number[8] = "Images/NumberWord/Number/8.png";
		Number[9] = "Images/NumberWord/Number/9.png";
	}
	
}
