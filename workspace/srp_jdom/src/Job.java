import  java.util.List;

public class Job {
	String name;
	String code;
	int prio;
	String intnr;
	int wcet;
	int arrival;
	List<ResUtil> lResUtil;
	
	public Job(String arg_name, String arg_code, int arg_prio, String arg_intnr, int arg_wcet, int arg_arrival, List<ResUtil> arg_lResUtil) {
		name = arg_name;
		code = arg_code;
		prio = arg_prio;
		intnr = arg_intnr;
		wcet = arg_wcet;
		arrival = arg_arrival;
		lResUtil = arg_lResUtil;
	}
	
	public void print() {
		System.out.println(
				"job : name = " + name +
				", code = " + code + 
				", prio = " + prio + 
				", intnr = " + intnr + 
				", wcet = " + code + 
				", arrival = " + arrival
				);
		ResUtil.printList(lResUtil, 1);
	}
	
	static public void print(List<Job> lj) {
		System.out.println("jobs");
		for (Job j : lj) {
			j.print();
		}
	}	
}
