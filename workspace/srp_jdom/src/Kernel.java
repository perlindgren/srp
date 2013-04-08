import java.util.List;
//import java.util.ArrayList;

public class Kernel  {
	
	public Kernel(String target, List<Resource> lr, List<Job> jl) throws Exception {
		
		if (target.equals("LPC11xx")) {
			/* LPC11xx */
			System.out.println("LPC11xx target OK");
		} else if (target == "LPC11xx") {
			System.out.println("target OK");
		} else if (target == "LPC11xx") {
			System.out.println("target OK");	
		} else {
			throw new Exception("Unknwon target " + target );
		}	
	}
}
