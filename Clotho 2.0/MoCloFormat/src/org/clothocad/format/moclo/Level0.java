package org.clothocad.format.moclo;

import org.clothocore.api.data.NucSeq;

/**
 *
 * @author Ernst Oberortner
 */

public class Level0 {
    
    // a Level 0 part looks like this...
    // <BsaI_LEFT> N <FUSION-SITE> <PART-Sequence> <FUSION-SITE> N <BsaI_RIGHT>

    private String BsaI;
    private String leftFusionSite;
    private String rightFusionSite;
    private String BpiI;
    
    private String basicPartSequence;
    

    public Level0(
            String BsaI, String BpiI, 
            String leftFusionSite, String rightFusionSite,
            String basicPartSequence) {    
        
        this.BsaI = BsaI;
        this.BpiI = BpiI;
        this.leftFusionSite = leftFusionSite;
        this.rightFusionSite = rightFusionSite;
        this.basicPartSequence = basicPartSequence;
    }
    
    public NucSeq getBsaI() {
        return new NucSeq(this.BsaI);
    }
    
    public NucSeq getLeftFusionSite() {
        return new NucSeq(this.leftFusionSite);
    }
    
    public NucSeq getBpiI() {
        return new NucSeq(this.BpiI);
    }

    public NucSeq getBasiPartSequence() {
        return new NucSeq(this.basicPartSequence);
    }
    
    public NucSeq getRightFusionSite() {
        return new NucSeq(this.rightFusionSite);
    }
        
    public NucSeq toNucSeq() {
        return new NucSeq(this.toString());
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.BsaI);
        sb.append("A");
        sb.append(this.leftFusionSite);
        sb.append("AT");
        sb.append(new NucSeq(this.BpiI).revComp());
        sb.append(this.basicPartSequence);
        sb.append(this.BpiI);
        sb.append("GT");
        sb.append(this.rightFusionSite);
        sb.append("A");
        sb.append(new NucSeq(this.BsaI).revComp());
        return sb.toString();
    }
}
