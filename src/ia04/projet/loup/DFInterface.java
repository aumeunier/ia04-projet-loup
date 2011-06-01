package ia04.projet.loup;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class DFInterface {
	/**
	 * Add a service to the DF
	 * @param a The agent to register
	 * @param sd Description of the agent's service (type + name)
	 */
	public static void registerService(Agent a, ServiceDescription sd){
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(a.getAID());
        dfd.addServices(sd);
        try {  
        	//FIXME: sometimes stuck here ?
            DFService.register(a, dfd);
        }
        catch (FIPAException fe) { fe.printStackTrace(); }	
	}
	/**
	 * Service research in the DF services list
	 * @param a Agent requesting the research
	 * @param serviceType Service researched
	 * @return
	 */
    public static AID getService(Agent a, String serviceType) {
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(serviceType);
        dfd.addServices(sd);
        try {
            DFAgentDescription[] result = DFService.search(a, dfd);
            if (result.length>0){
                return result[0].getName() ;
            }
        }
        catch (FIPAException fe) { fe.printStackTrace(); }
        return null;
    }
}
