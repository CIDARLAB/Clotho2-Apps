package org.clothocad.tool.ResDigest;

import org.clothocore.api.data.ObjBase;
import org.clothocore.api.plugin.ClothoTool;

/**
 *
 * @author Douglas Densmore
 */
public class StartRes implements ClothoTool {

    @Override
    public void launch() {
        GUIMain gui = new GUIMain();
        gui.setVisible(true);
    }

    @Override
    public void launch(ObjBase o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void init() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}