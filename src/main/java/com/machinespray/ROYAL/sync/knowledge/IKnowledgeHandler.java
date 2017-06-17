package com.machinespray.ROYAL.sync.knowledge;

import java.util.ArrayList;

public interface IKnowledgeHandler {

    void addKnowledge(String ul);

    boolean hasKnowledge(String ul);

    ArrayList<String> getKnowledge();

    void setKnowledge(ArrayList<String> knowledge);
}
