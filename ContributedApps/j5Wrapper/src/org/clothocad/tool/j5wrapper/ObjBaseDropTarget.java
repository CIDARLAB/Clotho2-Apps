/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.tool.j5wrapper;

import org.clothocore.api.data.ObjBase;

/**
 *
 * @author jenhan
 */
public interface ObjBaseDropTarget {
    /**
     * this method should be called after an ObjBase has been dropped
     */
    public void handleDroppedObject(ObjBase o);
}