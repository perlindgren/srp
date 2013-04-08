import java.util.List;
import java.util.ArrayList;

public class Srp {
	public static void srpCeiling(List<Job> jl, List<Resource> lr) {
		System.out.println("srpCeiling analysis");
		
		boolean loop = true; // continue
		int iterations = 1;
		while (loop) {
			System.out.println("iteration = " + iterations);
			loop = false;
			for (Job j : jl) {
				//System.out.println("analyse job " + i);
				//j.print();
				loop = loop || ResUtil.srpCeiling(j.lResUtil, j.prio);
			}
			iterations++;
		}
	}
	
	static void setReachable(Job j, List<ResUtil> resutil) {
		for (ResUtil r : resutil) {
			r.r.reachable.add(j);
			setReachable(j, r.resList);
		}
	}	
	
	public static void srpReach(List<Job> jl, List<Resource> lr) {
		System.out.println("srpReach analysis");
		
		// make new empty reachable list for each resource
		for (Resource r : lr) {
			r.reachable = new ArrayList<Job>();
		}
		
		for (Job j :jl) {
			System.out.println("analyse job ");
			j.print();
			setReachable(j, j.lResUtil);
		}		
	}

	static void setPrio(List<Resource> lr) {
		for (Resource r : lr) {
			int max_prio = 0;
			for (Job j : r.reachable) {
				if (j.prio> max_prio) {
					max_prio = j.prio;
				}
			}
			r.ceiling = max_prio;
		}
	}

}
