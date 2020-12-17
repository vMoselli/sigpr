package br.com.sigpr.util.demanda;

import br.com.sigpr.entity.funcionalidade.Funcionalidade;
import br.com.sigpr.entity.projeto.Projeto;
import br.com.sigpr.entity.status.Status;
import br.com.sigpr.entity.tarefa.Tarefa;
import br.com.sigpr.enumerations.TipoStatus;
import br.com.sigpr.enumerations.TipoTarefa;
import br.com.sigpr.exceptions.SIGPRException;
import br.com.sigpr.util.SIGPRUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class DemandaReader {
	
	
	private static final String PROJETO = "projeto";
	private static final String FUNCIONALIDADE = "funcionalidade";
	private static final String TAREFA = "tarefa";
	
	public static Demanda importarDemanda(ByteArrayInputStream arquivoImportado, List<Status> listaStatus) throws SIGPRException{
		
		Demanda demanda = new Demanda();
		demanda.setProjetos(new ArrayList<Projeto>());
		demanda.setFuncionalidades(new ArrayList<Funcionalidade>());
		demanda.setTarefas(new ArrayList<Tarefa>());
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(arquivoImportado);      
			Node docElement = doc.getDocumentElement();  
			NodeList root = docElement.getChildNodes();

			for (int i=0; i<root.getLength(); i++) {
				
				recuperaTags(root.item(i), listaStatus, demanda, null);
			}

		} catch (Exception e) {
			throw new SIGPRException("Importa��o de Demanda", "Ocorreu um erro ao importar a Demanda", e);
		}
		
		return demanda;
	}
	
	private static void recuperaTags(Node node, List<Status> listaStatus, Demanda demanda, Object pai) {
		
		if (node.getNodeType() == Node.ELEMENT_NODE) {  
			String nodeName = node.getNodeName();  

			if (node.hasAttributes()) {
				Element el = (Element)node;

				if(nodeName.equals(PROJETO)){
					Projeto projeto = new Projeto();
					projeto.setNome(el.getAttribute("nome"));
					projeto.setDescricao(el.getAttribute("descricao"));
					String status = el.getAttribute("status");
					for(Status st : listaStatus){
						if(st.getNome().equals(status) && st.getTipoStatus().equals(TipoStatus.Projeto)){
							projeto.setStatus(st);
						}
					}
					projeto.setPrazo(SIGPRUtil.getData(el.getAttribute("prazo"), "dd/MM/yyyy"));
					
					for (int i=0; i< el.getChildNodes().getLength(); i++) { 
						recuperaTags(el.getChildNodes().item(i), listaStatus, demanda, projeto);
					}
					
					demanda.getProjetos().add(projeto);
					
				}
				else if(nodeName.equals(FUNCIONALIDADE)){
					Funcionalidade funcionalidade = new Funcionalidade();
					funcionalidade.setNome(el.getAttribute("nome"));
					funcionalidade.setDescricao(el.getAttribute("descricao"));
					String status = el.getAttribute("status");
					for(Status st : listaStatus){
						if(st.getNome().equals(status) && st.getTipoStatus().equals(TipoStatus.Funcionalidade)){
							funcionalidade.setStatus(st);
						}
					}
					funcionalidade.setPrazo(SIGPRUtil.getData(el.getAttribute("prazo"), "dd/MM/yyyy"));
					funcionalidade.setProjeto((Projeto)pai);
					
					for (int i=0; i< el.getChildNodes().getLength(); i++) { 
						recuperaTags(el.getChildNodes().item(i), listaStatus, demanda, funcionalidade);
					}
					
					demanda.getFuncionalidades().add(funcionalidade);
				}
				else if(nodeName.equals(TAREFA)){
					Tarefa tarefa = new Tarefa();
					tarefa.setNome(el.getAttribute("nome"));
					tarefa.setDescricao(el.getAttribute("descricao"));
					tarefa.setPrazo(SIGPRUtil.getData(el.getAttribute("prazo"), "dd/MM/yyyy"));
					String tipoTarefa = el.getAttribute("tipoTarefa");
					for(TipoTarefa tipo : TipoTarefa.values()){
						if(tipo.getNome().equals(tipoTarefa)){
							tarefa.setTipoTarefa(tipo);
						}
					}
					String status = el.getAttribute("status");
					for(Status st : listaStatus){
						if(st.getNome().equals(status) && st.getTipoStatus().equals(TipoStatus.Tarefa)){
							tarefa.setStatus(st);
						}
					}
					tarefa.setFuncionalidade((Funcionalidade)pai);
					demanda.getTarefas().add(tarefa);
				}
			}
		}  
	}
}