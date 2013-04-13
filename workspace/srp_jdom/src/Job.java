import java.util.List;
import java.util.ArrayList;

public class Job {
	String name;
	String code;
	int prio;
	String intnr;
	int wcet;
	int arrival;
	int resp;
	int blocking;
	int interference;
	List<ResUtil> lResUtil;

	public Job(String arg_name, String arg_code, int arg_prio,
			String arg_intnr, int arg_wcet, int arg_arrival,
			List<ResUtil> arg_lResUtil) {
		name = arg_name;
		code = arg_code;
		prio = arg_prio;
		intnr = arg_intnr;
		wcet = arg_wcet;
		arrival = arg_arrival;
		lResUtil = arg_lResUtil;
	}

	public static List<Job> clone(List<Job> lj) {
		List<Job> ret = new ArrayList<Job>();
		for (Job j : lj) {
			ret.add(j);
		}
		return ret;
	}

	public String toString() {
		return "job : name = " + name + ", code = " + code + ", prio = " + prio
				+ ", intnr = " + intnr + ", wcet = " + wcet + ", arrival = "
				+ arrival + ", resp = " + resp + " (block " + blocking
				+ ", interference " + interference + ", wcet = " + wcet + ")\n"
				+ ResUtil.toString(lResUtil, 1);
	}

	public void print() {
		System.out.println(toString());
	}

	static public void print(List<Job> lj) {
		System.out.println("jobs");
		for (Job j : lj) {
			j.print();
		}
	}
}
