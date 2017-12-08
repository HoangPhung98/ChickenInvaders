import java.util.Arrays;

public class test  {
	test2 [] test22;
	
	public test(){
		test22 = new test2[3];
		for(int i=0; i<3; i++){
			test22[i] = new test2();
		}
		show();
	}
	
	private void show(){
		for(int i=0; i<3; i++){
			System.out.printf("test22: %d \n ",test22[i].result);
		}
	}
	
	
}
