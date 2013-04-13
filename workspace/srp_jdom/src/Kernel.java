import java.util.List;

//import java.util.ArrayList;

public class Kernel {
	public enum Target {
		notarget, LPC11xx, M0, M1, M3, M4, LPC3xx, LPC5xx
	};

	public enum Scheduler {
		noscheduler, level_masking, source_masking
	}

	public Target parseTarget(String t) {
		for (Target tt : Target.values()) {
			if (tt.name().equals(t)) {
				return tt;
			}
		}
		return Target.notarget;
	}

	public Scheduler parseScheduler(String t) {
		for (Scheduler st : Scheduler.values()) {
			if (st.name().equals(t)) {
				return st;
			}
		}
		return Scheduler.noscheduler;
	}

	public Kernel(String target, String s_type, List<Resource> lr, List<Job> jl)
			throws Exception {
		Target tt = parseTarget(target);
		Scheduler st = parseScheduler(s_type);
		switch (tt) {
		case LPC11xx: 
		case M0:
		case M1:
				System.out.println("// LPC11xx/M0/M1 target OK");
	
				for (Job j : jl) {
					System.out.println("/*");
					j.print();
					System.out.println("( 1 << " + j.intnr + "_IRQn )");
					System.out.println("*/");
					System.out.println(j.intnr + "_IRQHandler() { " + j.code + "(); }");
					

				}
				
/*
			case level_masking:
				System.out.println("// level masking");
				break;

			default:
				throw new Exception("Unknwon scheduler " + s_type);

			}
*/
			for (Job j : jl) {
				System.out.println("/*");
				j.print();
				System.out.println("( 1 << " + j.intnr + "_IRQn )");
				System.out.println("*/");
				System.out.println(j.intnr + "_IRQHandler() { " + j.code + "(); }");
				

			}

			/* we have 4 free vectors, EINT 0,...,EINT3 */
			// EINT3_IRQn = 28, /*!< External Interrupt 3 Interrupt */
			// EINT2_IRQn = 29, /*!< External Interrupt 2 Interrupt */
			// EINT1_IRQn = 30, /*!< External Interrupt 1 Interrupt */
			// EINT0_IRQn = 31, /*!< External Interrupt 0 Interrupt */
			// } IRQn_Type;

			System.out.println("LPC11xx target OK");
			break;

		// case LPC3xx:
		// case LPC5xx:
		// case notarget:
		// break;
		default:
			new Exception("Unknwon target " + target);
			break;

		}
	}
}
