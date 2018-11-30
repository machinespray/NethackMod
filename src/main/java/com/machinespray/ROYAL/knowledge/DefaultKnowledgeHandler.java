package com.machinespray.ROYAL.knowledge;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class DefaultKnowledgeHandler implements IKnowledgeHandler {
	private ArrayList<String> knowledge = new ArrayList<>();

	@Override
	public void addKnowledge(String ul) {
		knowledge.add(ul);
	}

	@Override
	public boolean hasKnowledge(String ul) {
		return knowledge.contains(ul);
	}

	@Nonnull
	@Override
	public ArrayList<String> getKnowledge() {
		return knowledge;
	}

	@Override
	public void setKnowledge(ArrayList<String> knowledge) {
		this.knowledge = knowledge;
	}

}
