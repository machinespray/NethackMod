package com.machinespray.ROYAL.knowledge;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public interface IKnowledgeHandler {
	void addKnowledge(String ul);
	boolean hasKnowledge(String ul);
	@Nonnull
	ArrayList<String> getKnowledge();
	void setKnowledge(ArrayList<String> knowledge);
}
