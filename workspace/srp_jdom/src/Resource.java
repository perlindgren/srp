import  java.util.List;
import java.util.ArrayList;

public class Resource {
	String name;
	public int ceiling;
	List<Job> reachable;
	
	public Resource(String arg_name) {
		name = arg_name;
		ceiling = 0; // lowest priority
		reachable = new ArrayList<Job>();
	}
	
	void print() {
		System.out.print("recource : " +
				"res_name = " + name + 
				", ceiling = " + ceiling +
				", reachable form jobs {"
				);
		for (int i = 0; i < reachable.size(); i++) {
			System.out.print("(" + reachable.get(i).code + ", "  + ", prio =" + reachable.get(i).prio + "), ");
		}
		System.out.println("}");
		
	}
	
	static public void print(List<Resource> lr) {
		System.out.println("recources");
		for (Resource r : lr) {
			r.print();
		}
	}

	static public Resource lookup(List<Resource> lr, String name) {
		try {
			for (Resource r : lr){
				if (r.name == name) {
					return r;
				}
			}
			throw new Exception("Error in XML, failed to match " + name );
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
			System.exit(-1);
		}
		return null;
	}
}
