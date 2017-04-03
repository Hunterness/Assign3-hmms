import Jama.Matrix;
import model.HMM;
import model.OurLocalizer;
import model.Reading;
import model.Sensor;

public class Test {
	
	public static void main(String[] args) {
		OurLocalizer l = new OurLocalizer(4,4,4);
		Sensor s = new Sensor();
		HMM h = new HMM(l, s);
	
		
		
//		Matrix p = h.getO(h.readings[0]);
//		for (int i =  1 ; i  < h.readings.length ; i++) {
//				p.plusEquals(h.getO(h.readings[i]));
//		}
//		for (int i = 0 ; i < p.getRowDimension() ; i++) {
//			for (int j = 0 ; j < p.getColumnDimension() ; j++)
//				System.out.print(p.get(i, j)+ " ");
//			System.out.println();
//		}
	
	}

}
