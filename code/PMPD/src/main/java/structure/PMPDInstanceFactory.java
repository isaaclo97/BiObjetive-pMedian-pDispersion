package structure;

import grafo.optilib.structure.InstanceFactory;

public class PMPDInstanceFactory extends InstanceFactory<PMPDInstance> {
    @Override
    public PMPDInstance readInstance(String s) {
        return new PMPDInstance(s);
    }
}
