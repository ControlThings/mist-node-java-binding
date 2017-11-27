package mist.node;

import addon.AddonService;
import mist.node.MistNode;

/**
 * Created by jan on 11/7/17.
 */

public class Service extends AddonService {
    public void startAddon() {
        MistNode.getInstance().startMistApp(getBaseContext());
    }
}
