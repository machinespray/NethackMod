package com.machinespray.ROYAL.sync.knowledge;

import java.util.ArrayList;

public class DefaultKnowledgeHandler implements IKnowledgeHandler {
    private ArrayList<String> knowledge = new ArrayList<String>();

    @Override
    public void addKnowledge(String ul) {
        knowledge.add(ul);
    }

    @Override
    public boolean hasKnowledge(String ul) {
        return knowledge.contains(ul);
    }

    @Override
    public ArrayList<String> getKnowledge() {
        return knowledge;
    }

    @Override
    public void setKnowledge(ArrayList<String> knowledge) {
        this.knowledge = knowledge;
    }

}
