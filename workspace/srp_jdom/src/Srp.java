import java.util.List;
import java.util.ArrayList;

public class Srp {
	/*
	 * Alternative solutions, does compute a pessimistic assignment of resource
	 * ceilings
	 */
	public static void srpCeiling(List<Job> jl, List<Resource> lr) {
		System.out.println("srpCeiling analysis, pessimistic, don't use!");

		boolean loop = true; // continue
		int iterations = 1;
		while (loop) {
			System.out.println("iteration = " + iterations);
			loop = false;
			for (Job j : jl) {
				// System.out.println("analyse job " + i);
				// j.print();
				loop = loop || ResUtil.srpCeiling(j.lResUtil, j.prio);
			}
			iterations++;
		}
	}

	/*
	 * setReachable, recursively iterates over subjobs (ResUtil), and for each
	 * reached resource the current job j.
	 */
	static void setReachable(Job j, List<ResUtil> resutil,
			List<Resource> claimed) {
		try {
			for (ResUtil r : resutil) {
				List<Resource> rec_claimed = Resource.clone(claimed);

				if (claimed.contains(r.r)) {
					// resource already taken by job, i.e., deadlock detected
					System.out.println("Already claimed");
					Resource.print(claimed);

					System.out.println("Trying to claim (fail)");
					r.r.print();

					System.out.println("Deadlock detected for job!");
					j.print();
					throw new Exception("Deadlock Error");
				}
				r.r.reachable.add(j);
				rec_claimed.add(r.r);
				setReachable(j, r.resList, rec_claimed);
			}
		}

		catch (Exception ex) {
			System.out.println(ex.getMessage());
			System.exit(-1);
		}
	}

	/*
	 * iterates over all jobs jl in the system
	 */
	public static void srpReach(List<Job> jl, List<Resource> lr) {
		System.out.println("srpReach analysis");

		// make new empty reachable list for each resource
		for (Resource r : lr) {
			r.reachable = new ArrayList<Job>();
		}

		for (Job j : jl) {
			System.out.println("analyse job ");
			j.print();
			setReachable(j, j.lResUtil, new ArrayList<Resource>());
		}
	}

	static void setPrio(List<Resource> lr) {
		for (Resource r : lr) {
			int max_prio = 0;
			for (Job j : r.reachable) {
				if (j.prio > max_prio) {
					max_prio = j.prio;
				}
			}
			r.ceiling = max_prio;
		}
	}

	static void utilization(List<Job> jobs) {
		float cpu = 0;
		for (Job k : jobs) {
			cpu += (float) k.wcet / k.arrival;
		}
		System.out.printf("cpu utilization " + cpu + "\n");
	}

	static int recCheckResUtilization(int prio, List<ResUtil> resutil) {
		int max_block = 0;
		for (ResUtil ru : resutil) {
			if (ru.r.ceiling >= prio) {
				max_block = Math.max(max_block, ru.blocking_time);
			} else {
				int rec_block = recCheckResUtilization(prio, ru.resList);
				max_block = Math.max(max_block, rec_block);
			}
		}
		return max_block;
	}

	static void responseTime(List<Job> jobs) {
		for (Job k : jobs) {
			// compute blocking for lower prioritized tasks
			System.out.printf("\n analyzing " + k.toString() + ", w.r.t. blocking -----------------------------------------------------\n");
			int blocking = 0;
			for (Job l : jobs) {
				if (k != l) {
					System.out.printf("\n comparing to " + l.toString() + "\n");
					// check blocking at same level
					if (l.prio == k.prio) {
						System.out
								.printf("\n same level, wcet" + l.wcet + "\n");
						blocking = Math.max(blocking, l.wcet);
					} else if (l.prio < k.prio) {
						// check blocking by lower prioritized tasks
						int rec_block = recCheckResUtilization(k.prio,
								l.lResUtil);
						System.out.printf("\n lower level, blocking = "
								+ rec_block + "\n");
						blocking = Math.max(blocking, rec_block);
					} else {
						System.out.printf("\n no blocking by job\n");
					}
				}
			}
			k.blocking = blocking;
			System.out.printf("\n max blocking = " + blocking + "\n");

			// compute interference from higher prioritized tasks
			System.out.printf("\n analyzing " + k.toString()
					+ ", interference\n");
			int new_busy_period = blocking + k.wcet;
			int busy_period = 0;
			while (new_busy_period > busy_period) {
				busy_period = new_busy_period;
				new_busy_period = blocking + k.wcet;
				for (Job l : jobs) {
					System.out.printf("\n busy period " + busy_period);

					if (k != l) {
						System.out.printf("\n comparing to " + l.toString()
								+ "\n");
						if (l.prio > k.prio) {
							int potint = l.wcet
									* (1 + (busy_period - 1) / l.arrival);
							System.out.printf("\n wcet =" + l.wcet
									+ ", arrival = " + l.arrival
									+ ",busy_period = " + busy_period
									+ "nr interferences = "
									+ (1 + (busy_period - 1) / l.arrival)
									+ ", interference time " + potint + "\n");

							new_busy_period += potint;
						}
					}
				}
			}
			k.resp = busy_period;
			k.interference = k.resp - k.wcet - k.blocking;
			System.out.printf(" response time  " + k.resp + "\n");
		}
	}
}

/*
 * for (Job l : jobs) { System.out.printf("\n busy period " + busy_period ); +
 * ", interference\n");
 * 
 * if (k != l) { System.out.printf("\n comparing to " + l.toString() + "\n");
 * 
 * if (l.prio > k.prio) { int potint = l.wcet * (1 + (k.arrival - 1) /
 * l.arrival); System.out
 * .printf("\n l.wcet * (1 + (k.arrival - 1) / l.arrival); " + potint); if
 * (potint > interference) { System.out.printf("\n updating interference " +
 * potint); interference = potint; } } } }
 */