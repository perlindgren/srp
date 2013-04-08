
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.jdom2.*; /* external library, make available in the build path */

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
	
	
	public class srp_jdom {
		private static List<ResUtil> recResource (List<Element> list, List<Resource> lr) throws DataConversionException {
			List<ResUtil> l= new ArrayList<ResUtil>();
			for (Element noder:list) {
				String sub_job_code = ""; if (noder.getAttribute("sub_job_code") != null) {sub_job_code = noder.getAttribute("sub_job_code").getValue();}
				int wcet = -1; if (noder.getAttribute("wcet") != null) {wcet = noder.getAttribute("wcet").getIntValue();}
				
				List<ResUtil> resRec = new ArrayList<ResUtil>();
				if (noder.getChildren()!=null){resRec = recResource(noder.getChildren(), lr); }
				ResUtil newRes = new ResUtil( noder.getName(), sub_job_code, wcet, resRec, lr);
				l.add(newRes);
			}
			return l;	 
		}
		
		public static void main(String[] args) {
	 
		  SAXBuilder builder = new SAXBuilder();
		  File xmlFile = new File("/home/parallels/srp/workspace/srp_jdom/tst.xml");
		  System.out.println("Start\n");
	 
		  try {
	 
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			System.out.println("System : " + rootNode.getName());
			
			List<Element> listr = rootNode.getChildren("resources");
			List<Resource> lr = new ArrayList<Resource>();
			
			for (Element node : listr) {
				List<Element> list = node.getChildren();
				for (Element noder : list) {
					lr.add(new Resource(noder.getName()));
				}
			}
			
			List<Job> jl = new ArrayList<Job>();
			
			List<Element> listj = rootNode.getChildren("jobs");
			
			for (Element node : listj) {
				System.out.println("Jobs : " + node.getName());
				
				List<Element> listk = node.getChildren();
				
				for (Element nodek: listk) {
					String job_name = nodek.getName();
					String job_code = ""; if (nodek.getAttribute("job_code") != null) {job_code = nodek.getAttribute("job_code").getValue();}
					int prio = -1; if (nodek.getAttribute("prio") != null) {prio = nodek.getAttribute("prio").getIntValue();}
					String intnr = ""; if (nodek.getAttribute("intnr") != null) {intnr = nodek.getAttribute("intnt").getValue();}
					int wcet = -1; if (nodek.getAttribute("wcet") != null) {wcet = nodek.getAttribute("wcet").getIntValue();}
					int arrival = -1; if (nodek.getAttribute("arrival") != null) {arrival = nodek.getAttribute("arrival").getIntValue();}
					List<ResUtil> lRecUtil = recResource(nodek.getChildren(), lr);
					Job myjob = new Job(
							job_name,
							job_code,
							prio,
							intnr,
							wcet,
							arrival,
							lRecUtil
							);
					jl.add(myjob);
				}
			}
			
			// here we have parsed and can go on to do some analysis
			System.out.println("Parsing ready \n print jobs \n");
			Job.print(jl);
			
			System.out.println("print resources before srp analysis\n");
			Resource.print(lr); 
			
			System.out.println("srpCeiling analysis\n");
			Srp.srpCeiling(jl, lr);
			Resource.print(lr);
			
			System.out.println("srpReach analysis\n");
			Srp.srpReach(jl, lr);
			Resource.print(lr);
			
			System.out.println("Srp.setPrio \n");
			Srp.setPrio(lr);
			Resource.print(lr);
			
	 
		  } catch (IOException io) {
			System.out.println(io.getMessage());
		  } catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		  } catch (Exception ex) {
				System.out.println(ex.getMessage());
				
		  }
		}
	}

