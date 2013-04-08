import  java.util.List;

public class ResUtil {
	String res_name;
	Resource r = null;
	String code_name;
	int blocking_time;
	List<ResUtil> resList;
	
	public ResUtil(String arg_res_name, String arg_code_name, int arg_blocking_time, List<ResUtil> arg_resList, List<Resource> lr) {
		res_name = arg_res_name;
		r = Resource.lookup(lr, res_name);
		code_name = arg_code_name;
		blocking_time = arg_blocking_time;
		resList = arg_resList;
		if (r == null) {
			System.out.println("RROR r == null");
		}
	}
	public void print(int level) {
		for (int i = 0; i<level; i++){
			System.out.print("    ");
		}
		System.out.println("recource : " + 
				"lev = " + level +
				", res_name = " + res_name + 
				", code_name = " + code_name + 
				", blocking_time = " + blocking_time
				);
		printList(resList, level+1);
	}
	
	public static void printList(List<ResUtil> rl, int level) {
		for (ResUtil resu : rl) {
			resu.print(level);
		}
	}
	
	public boolean srpCeiling(int prio) {
		boolean updated = false;
		
		if (r.ceiling < prio) {
			updated = true;
			r.ceiling = prio; 
		}
		updated |= srpCeiling(resList, r.ceiling);
		return updated;
	}
	
	public static boolean srpCeiling(List<ResUtil> rl, int prio) {
		boolean updated = false;
		for (ResUtil r : rl ) {
			updated |= r.srpCeiling(prio);
		}
		return updated;
	}
}
