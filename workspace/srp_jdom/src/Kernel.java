import java.util.List;
//import java.util.ArrayList;

public class Kernel  {
	public enum Target {notarget, LPC11xx,LPC3xx,LPC5xx};
	
	public Target parseTarget(String t) {
		for (Target tt : Target.values()) {
			if (tt.name().equals(t)) {
				return tt;
			}
		}
		return Target.notarget;
	}
	
	public Kernel(String target, List<Resource> lr, List<Job> jl) throws Exception {
		Target tt = parseTarget(target);
		switch (tt) {
		case LPC11xx: 
			System.out.println("// LPC11xx target OK");
				
			/* we have 4 distinct interrupt priority levels 0 (highest), 64, 128, 192, + idle which is running in thread mode */
			int[] hwpri = {192, 128, 64, 0}; // 4 elements
			
			System.out.println(
					"// Generated macros\n" +
					"PEND(job)  ISPR(job##_INR)");
			
			
			
			for (Job j : jl) {
				System.out.println("/*");
				j.print();
				System.out.println("( 1 << " + j.intnr + "_IRQn )");
				System.out.println("*/");
				System.out.println(j.intnr +"_IRQHandler() { " + j.code() + "}"); 
						
			}
			
		
			/* we have 4 free vectors, EINT 0,...,EINT3 */ 
			//		  EINT3_IRQn                    = 28,       /*!< External Interrupt 3 Interrupt                   */
			//		  EINT2_IRQn                    = 29,       /*!< External Interrupt 2 Interrupt                   */
			//		  EINT1_IRQn                    = 30,       /*!< External Interrupt 1 Interrupt                   */
			//		  EINT0_IRQn                    = 31,       /*!< External Interrupt 0 Interrupt                   */
			//		} IRQn_Type;

			System.out.println("LPC11xx target OK");
			break;
		
		//case LPC3xx:
		//case LPC5xx:
		//case notarget:
		//	break;
		default:
			new Exception("Unknwon target " + target );
			break;
		
		}	
	}
}
