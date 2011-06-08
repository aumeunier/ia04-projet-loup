//package ia04.projet.loup.gui;
//
//
//import ia04.projet.loup.messages.mMessage;
//import ia04.projet.loup.messages.mToGui;
//import jade.core.behaviours.CyclicBehaviour;
//import jade.lang.acl.ACLMessage;
//
//public class BehaviourAgtPlayerGui extends CyclicBehaviour {
//	
//	private static final long serialVersionUID = 1958438630758473392L;
//
//	@Override
//	public void action() {
//		ACLMessage msg = myAgent.receive();
//		if (msg != null){
//			String msgString = msg.getContent();
//			mToGui msgObj = (mToGui) mMessage.parseJson(msgString, mToGui.class);
//			((AgtPlayerGui) myAgent).guiMaj(msgObj);
//		}
//	}
//}
