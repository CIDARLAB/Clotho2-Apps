/*
 Copyright (c) 2009 The Regents of the University of California.
 All rights reserved.
 Permission is hereby granted, without written agreement and without
 license or royalty fees, to use, copy, modify, and distribute this
 software and its documentation for any purpose, provided that the above
 copyright notice and the following two paragraphs appear in all copies
 of this software.

 IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY
 FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
 ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
 THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
 PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
 CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
 ENHANCEMENTS, OR MODIFICATIONS..
 */

package org.clothocad.format.moclo;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import javax.swing.JOptionPane;
import org.clothocore.api.core.Collector;
import org.clothocore.api.data.Annotation;
import org.clothocore.api.data.Family;
import org.clothocore.api.data.Feature;
import org.clothocore.api.data.NucSeq;
import org.clothocore.api.data.Part;
import org.clothocore.api.data.Person;
import org.clothocore.api.data.Plasmid;
import org.clothocore.api.data.Vector;
import org.clothocore.api.plugin.ClothoFormat;



/**
 * Format Interface is what a ClothoFormat Plugin must implement
 * @author e.oberortner
 */
public class connect implements ClothoFormat {

    // input: one part 
    // Level 0
    // <BsaI_LEFT> N <FUSION-SITE> <PART-Sequence> <FUSION-SITE> N <BsaI_RIGHT>
        
    // input: 2-6 level 0 parts
    // output:
    // <BpiI_LEFT> NN <FUSION-SITE> <PART-1-Sequence> <FUSION-SITE> <PART-1-Sequence> <FUSION-SITE> ... <FUSION-SITE> NN <BpiI-RIGHT>
    private final String BsaI = "GGTCTC";
    private final String BpiI = "GAAGAC";
    
    private static final String prefix = "MOCLO_FS_";   
    private static HashMap<String, String> FUSION_SITES;
    private static boolean bStored = false;
    
    public connect() {
        /** SAVE THE FEATURES **/
        FUSION_SITES = new HashMap<String, String>();
        FUSION_SITES.put("A", "GGAG");
        FUSION_SITES.put("B", "TACT");
        FUSION_SITES.put("C", "AATG");
        FUSION_SITES.put("D", "AGGT");
        FUSION_SITES.put("E", "GCTT");
        FUSION_SITES.put("F", "CGCT");
        FUSION_SITES.put("G", "TGCC");
        FUSION_SITES.put("H", "ACTA");
        FUSION_SITES.put("I", "TCTA");
        FUSION_SITES.put("X", "CGTT");
        FUSION_SITES.put("Y", "TGTG");
    }
    
    private static void storeFS() {
    
        int i=1;
        Person author = Collector.getCurrentUser();
        
        for(String sFS : FUSION_SITES.keySet()) {
            Feature f = Feature.retrieveByName(sFS);
            if(null == f) {
                f = Feature.generateFeature(sFS, 
                        FUSION_SITES.get(sFS), author, false);
                f.saveDefault();                
            }
            i++;
        }
        
        bStored = true;
    }
    
    @Override
    public boolean checkPart(Part part) {
        if(null == part || null == part.getSeq() || 
           null == part.getSeq().getSeq() || part.getSeq().getSeq().isEmpty()) {
            return true;
        }

        if(!bStored) {
            storeFS();
        }

        // get the part's sequence
        String seq = part.getSeq().getSeq().toUpperCase();
        int length = seq.length();
        
        if(8 >= length) {
            JOptionPane.showMessageDialog( 
                   null, 
                "Invalid length of the "+part.getName()+" part's sequence!",
                "Clotho: MoCloFormat", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // get the first 4 characters of the part's sequence
        String sPartLeftFS = seq.substring(0, 4);
        String sPartRightFS = seq.substring(seq.length()-4);

        if(FUSION_SITES.containsValue(sPartLeftFS) &&
           FUSION_SITES.containsValue(sPartRightFS) &&
           !sPartLeftFS.equalsIgnoreCase(sPartRightFS)) {

            if(null == part.getSeq().getAnnotations() ||
                    part.getSeq().getAnnotations().isEmpty()) {                
                part.getSeq().autoAnnotate(Collector.getCurrentUser());                
            }
            
            return true;
        } else {
            JOptionPane.showMessageDialog( 
                null, 
                "The provided sequence of the "+
                    part.getName()+
                    " part does not comply with the MoClo format!",
                "Clotho: MoCloFormat", 
                JOptionPane.ERROR_MESSAGE);
        }        
        return false;
    }

    @Override
    public boolean checkVector(Vector v) {
        /***
        if(null == v || null == v.getSeq() || 
                null == v.getSeq().getSeq() || v.getSeq().getSeq().isEmpty()) {
            return true;
        }
       
        // get the vector's sequence
        String seq = v.getSeq().getSeq();
        int length = seq.length();
        
        // RESTRICTION SITES
        if(!seq.startsWith(this.BsaI) 
                || !seq.endsWith(new NucSeq(this.BsaI).revComp())) {            
            return false;
        }
        
        // FUSION SITES
        if(length - 12 <= 0) {
            return false;
        }
        
        String sLeftFusionSite = seq.substring(7,10);
        String sRightFusionSite = seq.substring(
                seq.length()-12,seq.length()-9);
        
        // check the fusion sites
        if(!FUSION_SITES.contains(sLeftFusionSite) ||
                !FUSION_SITES.contains(sRightFusionSite) ||
                sLeftFusionSite.equals(sRightFusionSite)) {
            return false;
        }
        
        if(length - 20 <= 0) {
            return false;
        }
        
        if(!new NucSeq(this.BpiI).revComp().equals(seq.substring(13, 18)) ||
                !this.BpiI.equals(seq.substring(length-20, length-18))) {
            return false;
        }
        ***/
        
        return true;
    }

    @Override
    public boolean checkComposite(ArrayList<Part> composition, Object additionalRequirements) {
        if(null == composition || composition.isEmpty()) {
            return true;
        }

        if(!bStored) {
            storeFS();
        }

        if(composition.size() == 1) {
            // this should actually never happen
            return this.checkPart(composition.get(0));
        } else if(composition.size() > 6) {
            JOptionPane.showMessageDialog( 
                null, 
                "MoClo can only compose up to 6 parts!",
                "Clotho: MoCloFormat", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Set<String> usedFusionSides = new HashSet<String>();
        
        // take the left most part
        Part p = composition.get(0);
        String seq = p.getSeq().getSeq();
        String fs = seq.substring(0,4);
        usedFusionSides.add(fs);
        
        for(int i=0; i<composition.size()-1; i++) {
            Part p1 = composition.get(i);
            Part p2 = composition.get(i+1);

            // this should always be true
            if(this.checkPart(p1) && this.checkPart(p2)) {
                // requirement: the fusion sites must be equal
                
                String seqLeft = p1.getSeq().getSeq();
                String seqRight = p2.getSeq().getSeq();
                
                // get the last 4 bp from the left sequence
                String fsLeft = seqLeft.substring(seqLeft.length()-4);
                String fsRight = seqRight.substring(0, 4);
                
                if(!fsLeft.equalsIgnoreCase(fsRight)) {
                    JOptionPane.showMessageDialog( 
                        null, 
                        "Invalid MoClo fusion sites for "+
                            p1.getName()+" and "+p2.getName()+" ("+fsLeft+"!="+fsRight+")",
                        "Clotho: MoCloFormat", 
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                } else if(usedFusionSides.contains(fsLeft)) {
                    JOptionPane.showMessageDialog( 
                        null, 
                        "The MoClo fusion site "+fsLeft.toUpperCase()+
                            " has been used already in the composition!",
                        "Clotho: MoCloFormat", 
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                
                usedFusionSides.add(fsLeft);
            }
        }

        // the right most part
        p = composition.get(composition.size()-1);
        seq = p.getSeq().getSeq();
        fs = seq.substring(seq.length()-4);
        if(usedFusionSides.contains(fs)) {
            JOptionPane.showMessageDialog( 
                null, 
                "The MoClo fusion site "+fs.toUpperCase()+
                    " has been used already in the composition!",
                "Clotho: MoCloFormat", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        usedFusionSides.clear();
        
        return true;
    }

    @Override
    public boolean checkPlasmid(Part p, Vector v, Object additionalRequirements) {
        return true;
    }

    public NucSeq generateBasicPartSequence(Part objPart) {

        return objPart.getSeq();
    }
    
    @Override
    public NucSeq generateCompositeSequence(
            ArrayList<Part> composition, Object additionalRequirements) {

        if(null == composition || composition.size()>6 || composition.isEmpty()) {
            return (NucSeq)null;
        }
        
        if(composition.size() == 1) {
            Part p = composition.get(0);
            if(null != p) {
                return p.getSeq();
            } 
            return (NucSeq)null;
        }
        
        StringBuilder sb = new StringBuilder();

        Part part = composition.get(0);
        if(null == part || 
                null == part.getSeq() || 
                null == part.getSeq().getSeq() || 
                part.getSeq().getSeq().isEmpty()) {
            return null;
        }

        sb.append(part.getSeq().getSeq());
        
        for(int i=1; i<composition.size(); i++) {
            part = composition.get(i);
            
            if(null == part || 
                    null == part.getSeq() || 
                    null == part.getSeq().getSeq() || 
                    part.getSeq().getSeq().isEmpty()) {
                return null;
            }

            String sSeq = part.getSeq().getSeq();
            sb.append(sSeq.substring(5));
        }

        NucSeq nseq = new NucSeq(sb.toString());
        nseq.autoAnnotate(Collector.getCurrentUser());
        return nseq;
    }
    
    @Override
    public NucSeq generatePlasmidSequence(Plasmid p) {
        //Return the part's NucSeq
        Part thepart = p.getPart();
        if(thepart==null) {
            return null;
        }
        return thepart.getSeq();
    }

    @Override
    public NucSeq generateSequencingRegion(Plasmid p) {
        return generatePlasmidSequence(p);
    }

    @Override
    public void init() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
