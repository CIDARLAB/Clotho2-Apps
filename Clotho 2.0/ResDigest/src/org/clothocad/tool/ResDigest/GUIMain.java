/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * GUIMain.java
 *
 * Created on Dec 1, 2011, 7:09:13 PM
 */

package org.clothocad.tool.ResDigest;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import org.clothocore.api.core.Collector;
import org.clothocore.api.data.ObjLink;
import org.clothocore.api.data.ObjType;
import org.clothocore.api.data.Part;
import org.clothocore.api.data.Plasmid;

/**
 *
 * @author rishi
 */
public class GUIMain extends javax.swing.JFrame {


    // my variables
    private static ArrayList<ObjLink> plasmidList = Collector.getAllLinksOf(ObjType.PLASMID);
    private static ArrayList<ObjLink> partList = Collector.getAllLinksOf(ObjType.PART);
    private Hashtable enzymeTable;
    private static final String enzymefile = "enzymes.txt";
    /** Creates new form GUIMain */
    public GUIMain() {
        initComponents();
        this.setTitle("Restriction Digest for Plasmid");
        populateLists();
        mapEnzymes();
        this.populateFromEnzymeFile();
    }

    private void populateLists()
    {
         
         Object [] o1 = plasmidList.toArray();
         this.jListPlasmid.setListData(o1);
         Object [] o2 = partList.toArray();
         this.jListParts.setListData(o2);
         
    }
    private void mapEnzymes()
    {
        enzymeTable =new Hashtable();
        enzymeTable.put("(AatII)", "GACGT*C");
        enzymeTable.put("(Acc65I)", "G*GTACC");
        enzymeTable.put("(AccI)", "GT*MKAC");
        enzymeTable.put("(AciI)", "CCGC (-3/-1)");
        enzymeTable.put("(AclI)", "AA*CGTT");
        enzymeTable.put("(AcuI)", "CTGAAG (16/14)");
        enzymeTable.put("(AfeI)", "AGC*GCT");
        enzymeTable.put("(AflII)", "C*TTAAG");
        enzymeTable.put("(AflIII)", "A*CRYGT");
        enzymeTable.put("(AgeI)", "A*CCGGT");
        enzymeTable.put("(AhdI)", "GACNNN*NNGTC");
        enzymeTable.put("(AleI)", "CACNN*NNGTG");
        enzymeTable.put("(AluI)", "AG*CT");
        enzymeTable.put("(AlwI)", "GGATC (4/5)");
        enzymeTable.put("(AlwNI)", "CAGNNN*CTG");
        enzymeTable.put("(ApaI)", "GGGCC*C");
        enzymeTable.put("(ApaLI)", "G*TGCAC");
        enzymeTable.put("(ApeKI)", "G*CWGC");
        enzymeTable.put("(ApoI)", "R*AATTY");
        enzymeTable.put("(AscI)", "GG*CGCGCC");
        enzymeTable.put("(AseI)", "AT*TAAT");
        enzymeTable.put("(AsiSI)", "GCGAT*CGC");
        enzymeTable.put("(AvaI)", "C*YCGRG");
        enzymeTable.put("(AvaII)", "G*GWCC");
        enzymeTable.put("(AvrII)", "C*CTAGG");
        enzymeTable.put("(BaeGI)", "GKGCM*C");
        enzymeTable.put("(BaeI)", "(10/15) ACNNNNGTAYC (12/7)");
        enzymeTable.put("(BamHI)", "G*GATCC");
        enzymeTable.put("(BanI)", "G*GYRCC");
        enzymeTable.put("(BanII)", "GRGCY*C");
        enzymeTable.put("(BbsI)", "GAAGAC (2/6)");
        enzymeTable.put("(BbvCI)", "CCTCAGC (-5/-2)");
        enzymeTable.put("(BbvI)", "GCAGC (8/12)");
        enzymeTable.put("(BccI)", "CCATC (4/5)");
        enzymeTable.put("(BceAI)", "ACGGC (12/14)");
        enzymeTable.put("(BcgI)", "(10/12) CGANNNNNNTGC (12/10)");
        enzymeTable.put("(BciVI)", "GTATCC (6/5)");
        enzymeTable.put("(BclI)", "T*GATCA");
        enzymeTable.put("(BcoDI)", "GTCTC (1/5)");
        enzymeTable.put("(BfaI)", "C*TAG");
        enzymeTable.put("(BfuAI)", "ACCTGC (4/8)");
        enzymeTable.put("(BfuCI)", "*GATC");
        enzymeTable.put("(BglI)", "GCCNNNN*NGGC");
        enzymeTable.put("(BglII)", "A*GATCT");
        enzymeTable.put("(BlpI)", "GC*TNAGC");
        enzymeTable.put("(BmgBI)", "CACGTC (-3/-3)");
        enzymeTable.put("(BmrI)", "ACTGGG (5/4)");
        enzymeTable.put("(BmtI)", "GCTAG*C");
        enzymeTable.put("(BpmI)", "CTGGAG (16/14)");
        enzymeTable.put("(Bpu10I)", "CCTNAGC (-5/-2)");
        enzymeTable.put("(BpuEI)", "CTTGAG (16/14)");
        enzymeTable.put("(BsaAI)", "YAC*GTR");
        enzymeTable.put("(BsaBI)", "GATNN*NNATC");
        enzymeTable.put("(BsaHI)", "GR*CGYC");
        enzymeTable.put("(BsaI)", "GGTCTC (1/5)");
        enzymeTable.put("(BsaJI)", "C*CNNGG");
        enzymeTable.put("(BsaWI)", "W*CCGGW");
        enzymeTable.put("(BsaXI)", "(9/12) ACNNNNNCTCC (10/7)");
        enzymeTable.put("(BseRI)", "GAGGAG (10/8)");
        enzymeTable.put("(BseYI)", "CCCAGC (-5/-1)");
        enzymeTable.put("(BsgI)", "GTGCAG (16/14)");
        enzymeTable.put("(BsiEI)", "CGRY*CG");
        enzymeTable.put("(BsiHKAI)", "GWGCW*C");
        enzymeTable.put("(BsiWI)", "C*GTACG");
        enzymeTable.put("(BslI)", "CCNNNNN*NNGG");
        enzymeTable.put("(BsmAI)", "GTCTC (1/5)");
        enzymeTable.put("(BsmBI)", "CGTCTC (1/5)");
        enzymeTable.put("(BsmFI)", "GGGAC (10/14)");
        enzymeTable.put("(BsmI)", "GAATGC (1/-1)");
        enzymeTable.put("(BsoBI)", "C*YCGRG");
        enzymeTable.put("(Bsp1286I)", "GDGCH*C");
        enzymeTable.put("(BspCNI)", "CTCAG (9/7)");
        enzymeTable.put("(BspDI)", "AT*CGAT");
        enzymeTable.put("(BspEI)", "T*CCGGA");
        enzymeTable.put("(BspHI)", "T*CATGA");
        enzymeTable.put("(BspMI)", "ACCTGC (4/8)");
        enzymeTable.put("(BspQI)", "GCTCTTC (1/4)");
        enzymeTable.put("(BsrBI)", "CCGCTC (-3/-3)");
        enzymeTable.put("(BsrDI)", "GCAATG (2/0)");
        enzymeTable.put("(BsrFI)", "R*CCGGY");
        enzymeTable.put("(BsrGI)", "T*GTACA");
        enzymeTable.put("(BsrI)", "ACTGG (1/-1)");
        enzymeTable.put("(BssHII)", "G*CGCGC");
        enzymeTable.put("(BssKI)", "*CCNGG");
        enzymeTable.put("(BssSI)", "CACGAG (-5/-1)");
        enzymeTable.put("(BstAPI)", "GCANNNN*NTGC");
        enzymeTable.put("(BstBI)", "TT*CGAA");
        enzymeTable.put("(BstEII)", "G*GTNACC");
        enzymeTable.put("(BstNI)", "CC*WGG");
        enzymeTable.put("(BstUI)", "CG*CG");
        enzymeTable.put("(BstXI)", "CCANNNNN*NTGG");
        enzymeTable.put("(BstYI)", "R*GATCY");
        enzymeTable.put("(BstZ17I)", "GTA*TAC");
        enzymeTable.put("(Bsu36I)", "CC*TNAGG");
        enzymeTable.put("(BtgI)", "C*CRYGG");
        enzymeTable.put("(BtgZI)", "GCGATG (10/14)");
        enzymeTable.put("(BtsCI)", "GGATG (2/0)");
        enzymeTable.put("(BtsI)", "GCAGTG (2/0)");
        enzymeTable.put("(BtsIMutI)", "CAGTG (2/0)");
        enzymeTable.put("(Cac8I)", "GCN*NGC");
        enzymeTable.put("(ClaI)", "AT*CGAT");
        enzymeTable.put("(CspCI)", "(11/13) CAANNNNNGTGG (12/10)");
        enzymeTable.put("(CviAII)", "C*ATG");
        enzymeTable.put("(CviKI-1)", "RG*CY");
        enzymeTable.put("(CviQI)", "G*TAC");
        enzymeTable.put("(DdeI)", "C*TNAG");
        enzymeTable.put("(DpnI)", "GA*TC");
        enzymeTable.put("(DpnII)", "*GATC");
        enzymeTable.put("(DraI)", "TTT*AAA");
        enzymeTable.put("(DraIII)", "CACNNN*GTG");
        enzymeTable.put("(DrdI)", "GACNNNN*NNGTC");
        enzymeTable.put("(EaeI)", "Y*GGCCR");
        enzymeTable.put("(EagI)", "C*GGCCG");
        enzymeTable.put("(EarI)", "CTCTTC (1/4)");
        enzymeTable.put("(EciI)", "GGCGGA (11/9)");
        enzymeTable.put("(Eco53kI)", "GAG*CTC");
        enzymeTable.put("(EcoNI)", "CCTNN*NNNAGG");
        enzymeTable.put("(EcoO109I)", "RG*GNCCY");
        enzymeTable.put("(EcoRI)", "G*AATTC");
        enzymeTable.put("(EcoRV)", "GAT*ATC");
        enzymeTable.put("(FatI)", "*CATG");
        enzymeTable.put("(FauI)", "CCCGC (4/6)");
        enzymeTable.put("(Fnu4HI)", "GC*NGC");
        enzymeTable.put("(FokI)", "GGATG (9/13)");
        enzymeTable.put("(FseI)", "GGCCGG*CC");
        enzymeTable.put("(FspEI)", "CC (12/16)");
        enzymeTable.put("(FspI)", "TGC*GCA");
        enzymeTable.put("(HaeII)", "RGCGC*Y");
        enzymeTable.put("(HaeIII)", "GG*CC");
        enzymeTable.put("(HgaI)", "GACGC (5/10)");
        enzymeTable.put("(HhaI)", "GCG*C");
        enzymeTable.put("(HincII)", "GTY*RAC");
        enzymeTable.put("(HindIII)", "A*AGCTT");
        enzymeTable.put("(HinfI)", "G*ANTC");
        enzymeTable.put("(HinP1I)", "G*CGC");
        enzymeTable.put("(HpaI)", "GTT*AAC");
        enzymeTable.put("(HpaII)", "C*CGG");
        enzymeTable.put("(HphI)", "GGTGA (8/7)");
        enzymeTable.put("(Hpy166II)", "GTN*NAC");
        enzymeTable.put("(Hpy188I)", "TCN*GA");
        enzymeTable.put("(Hpy188III)", "TC*NNGA");
        enzymeTable.put("(Hpy99I)", "CGWCG*");
        enzymeTable.put("(HpyAV)", "CCTTC (6/5)");
        enzymeTable.put("(HpyCH4III)", "ACN*GT");
        enzymeTable.put("(HpyCH4IV)", "A*CGT");
        enzymeTable.put("(HpyCH4V)", "TG*CA");
        enzymeTable.put("(KasI)", "G*GCGCC");
        enzymeTable.put("(KpnI)", "GGTAC*C");
        enzymeTable.put("(LpnPI)", "CCDG (10/14)");
        enzymeTable.put("(MboI)", "*GATC");
        enzymeTable.put("(MboII)", "GAAGA (8/7)");
        enzymeTable.put("(MfeI)", "C*AATTG");
        enzymeTable.put("(MluCI)", "*AATT");
        enzymeTable.put("(MluI)", "A*CGCGT");
        enzymeTable.put("(MlyI)", "GAGTC (5/5)");
        enzymeTable.put("(MmeI)", "TCCRAC (20/18)");
        enzymeTable.put("(MnlI)", "CCTC (7/6)");
        enzymeTable.put("(MscI)", "TGG*CCA");
        enzymeTable.put("(MseI)", "T*TAA");
        enzymeTable.put("(MslI)", "CAYNN*NNRTG");
        enzymeTable.put("(MspA1I)", "CMG*CKG");
        enzymeTable.put("(MspI)", "C*CGG");
        enzymeTable.put("(MspJI)", "CNNR (9/13)");
        enzymeTable.put("(MwoI)", "GCNNNNN*NNGC");
        enzymeTable.put("(NaeI)", "GCC*GGC");
        enzymeTable.put("(NarI)", "GG*CGCC");
        enzymeTable.put("(NciI)", "CC*SGG");
        enzymeTable.put("(NcoI)", "C*CATGG");
        enzymeTable.put("(NdeI)", "CA*TATG");
        enzymeTable.put("(NgoMIV)", "G*CCGGC");
        enzymeTable.put("(NheI)", "G*CTAGC");
        enzymeTable.put("(NlaIII)", "CATG*");
        enzymeTable.put("(NlaIV)", "GGN*NCC");
        enzymeTable.put("(NmeAIII)", "GCCGAG (21/19)");
        enzymeTable.put("(NotI)", "GC*GGCCGC");
        enzymeTable.put("(NruI)", "TCG*CGA");
        enzymeTable.put("(NsiI)", "ATGCA*T");
        enzymeTable.put("(NspI)", "RCATG*Y");
        enzymeTable.put("(PacI)", "TTAAT*TAA");
        enzymeTable.put("(PaeR7I)", "C*TCGAG");
        enzymeTable.put("(PciI)", "A*CATGT");
        enzymeTable.put("(PflFI)", "GACN*NNGTC");
        enzymeTable.put("(PflMI)", "CCANNNN*NTGG");
        enzymeTable.put("(PhoI)", "GG*CC");
        enzymeTable.put("(PleI)", "GAGTC (4/5)");
        enzymeTable.put("(PluTI)", "GGCGC*C");
        enzymeTable.put("(PmeI)", "GTTT*AAAC");
        enzymeTable.put("(PmlI)", "CAC*GTG");
        enzymeTable.put("(PpuMI)", "RG*GWCCY");
        enzymeTable.put("(PshAI)", "GACNN*NNGTC");
        enzymeTable.put("(PsiI)", "TTA*TAA");
        enzymeTable.put("(PspGI)", "*CCWGG");
        enzymeTable.put("(PspOMI)", "G*GGCCC");
        enzymeTable.put("(PspXI)", "VC*TCGAGB");
        enzymeTable.put("(PstI)", "CTGCA*G");
        enzymeTable.put("(PvuI)", "CGAT*CG");
        enzymeTable.put("(PvuII)", "CAG*CTG");
        enzymeTable.put("(RsaI)", "GT*AC");
        enzymeTable.put("(RsrII)", "CG*GWCCG");
        enzymeTable.put("(SacI)", "GAGCT*C");
        enzymeTable.put("(SacII)", "CCGC*GG");
        enzymeTable.put("(SalI)", "G*TCGAC");
        enzymeTable.put("(SapI)", "GCTCTTC (1/4)");
        enzymeTable.put("(Sau3AI)", "*GATC");
        enzymeTable.put("(Sau96I)", "G*GNCC");
        enzymeTable.put("(SbfI)", "CCTGCA*GG");
        enzymeTable.put("(ScaI)", "AGT*ACT");
        enzymeTable.put("(ScrFI)", "CC*NGG");
        enzymeTable.put("(SexAI)", "A*CCWGGT");
        enzymeTable.put("(SfaNI)", "GCATC (5/9)");
        enzymeTable.put("(SfcI)", "C*TRYAG");
        enzymeTable.put("(SfiI)", "GGCCNNNN*NGGCC");
        enzymeTable.put("(SfoI)", "GGC*GCC");
        enzymeTable.put("(SgrAI)", "CR*CCGGYG");
        enzymeTable.put("(SmaI)", "CCC*GGG");
        enzymeTable.put("(SmlI)", "C*TYRAG");
        enzymeTable.put("(SnaBI)", "TAC*GTA");
        enzymeTable.put("(SpeI)", "A*CTAGT");
        enzymeTable.put("(SphI)", "GCATG*C");
        enzymeTable.put("(SspI)", "AAT*ATT");
        enzymeTable.put("(StuI)", "AGG*CCT");
        enzymeTable.put("(StyD4I)", "*CCNGG");
        enzymeTable.put("(StyI)", "C*CWWGG");
        enzymeTable.put("(SwaI)", "ATTT*AAAT");
        enzymeTable.put("(TaqI)", "T*CGA");
        enzymeTable.put("(TfiI)", "G*AWTC");
        enzymeTable.put("(TseI)", "G*CWGC");
        enzymeTable.put("(Tsp45I)", "*GTSAC");
        enzymeTable.put("(TspMI)", "C*CCGGG");
        enzymeTable.put("(TspRI)", "CASTGNN*");
        enzymeTable.put("(Tth111I)", "GACN*NNGTC");
        enzymeTable.put("(XcmI)", "CCANNNNN*NNNNTGG");
        enzymeTable.put("(XhoI)", "C*TCGAG");
        enzymeTable.put("(XmaI)", "C*CCGGG");
        enzymeTable.put("(XmnI)", "GAANN*NNTTC");
        enzymeTable.put("(ZraI)", "GAC*GTC");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox1 = new javax.swing.JCheckBox();
        buttonGroupConfirmation = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaPlasmidText = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListPlasmid = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jScrollPaneEnzymeList = new javax.swing.JScrollPane();
        jListRestrictionEnzymes = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jTextFieldlowerBound = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButtonFindOpt = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jButtonRun = new javax.swing.JButton();
        jButtonAddEnzyme = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jListParts = new javax.swing.JList();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jRadioButtonCircular = new javax.swing.JRadioButton();
        jRadioButtonLinear = new javax.swing.JRadioButton();

        jCheckBox1.setText(org.openide.util.NbBundle.getMessage(GUIMain.class, "GUIMain.jCheckBox1.text")); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 255));

        jTextAreaPlasmidText.setColumns(20);
        jTextAreaPlasmidText.setLineWrap(true);
        jTextAreaPlasmidText.setRows(5);
        jScrollPane1.setViewportView(jTextAreaPlasmidText);

        jScrollPane2.setViewportView(jListPlasmid);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(GUIMain.class, "GUIMain.jLabel1.text")); // NOI18N

        jListRestrictionEnzymes.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "AatII", "Acc65I", "AccI", "AciI", "AclI", "AcuI", "AfeI", "AflII", "AflIII", "AgeI", "AhdI", "AleI", "AluI", "AlwI", "AlwNI", "ApaI", "ApaLI", "ApeKI", "ApoI", "AscI", "AseI", "AsiSI", "AvaI", "AvaII", "AvrII", "BaeGI", "BaeI", "BamHI", "BanI", "BanII", "BbsI", "BbvCI", "BbvI", "BccI", "BceAI", "BcgI", "BciVI", "BclI", "BcoDI", "BfaI", "BfuAI", "BfuCI", "BglI", "BglII", "BlpI", "BmgBI", "BmrI", "BmtI", "BpmI", "Bpu10I", "BpuEI", "BsaAI", "BsaBI", "BsaHI", "BsaI", "BsaJI", "BsaWI", "BsaXI", "BseRI", "BseYI", "BsgI", "BsiEI", "BsiHKAI", "BsiWI", "BslI", "BsmAI", "BsmBI", "BsmFI", "BsmI", "BsoBI", "Bsp1286I", "BspCNI", "BspDI", "BspEI", "BspHI", "BspMI", "BspQI", "BsrBI", "BsrDI", "BsrFI", "BsrGI", "BsrI", "BssHII", "BssKI", "BssSI", "BstAPI", "BstBI", "BstEII", "BstNI", "BstUI", "BstXI", "BstYI", "BstZ17I", "Bsu36I", "BtgI", "BtgZI", "BtsCI", "BtsI", "BtsIMutI", "Cac8I", "ClaI", "CspCI", "CviAII", "CviKI-1", "CviQI", "DdeI", "DpnI", "DpnII", "DraI", "DraIII", "DrdI", "EaeI", "EagI", "EarI", "EciI", "Eco53kI", "EcoNI", "EcoO109I", "EcoRI", "EcoRV", "FatI", "FauI", "Fnu4HI", "FokI", "FseI", "FspEI", "FspI", "HaeII", "HaeIII", "HgaI", "HhaI", "HincII", "HindIII", "HinfI", "HinP1I", "HpaI", "HpaII", "HphI", "Hpy166II", "Hpy188I", "Hpy188III", "Hpy99I", "HpyAV", "HpyCH4III", "HpyCH4IV", "HpyCH4V", "KasI", "KpnI", "LpnPI", "MboI", "MboII", "MfeI", "MluCI", "MluI", "MlyI", "MmeI", "MnlI", "MscI", "MseI", "MslI", "MspA1I", "MspI", "MspJI", "MwoI", "NaeI", "NarI", "NciI", "NcoI", "NdeI", "NgoMIV", "NheI", "NlaIII", "NlaIV", "NmeAIII", "NotI", "NruI", "NsiI", "NspI", "PacI", "PaeR7I", "PciI", "PflFI", "PflMI", "PhoI", "PleI", "PluTI", "PmeI", "PmlI", "PpuMI", "PshAI", "PsiI", "PspGI", "PspOMI", "PspXI", "PstI", "PvuI", "PvuII", "RsaI", "RsrII", "SacI", "SacII", "SalI", "SapI", "Sau3AI", "Sau96I", "SbfI", "ScaI", "ScrFI", "SexAI", "SfaNI", "SfcI", "SfiI", "SfoI", "SgrAI", "SmaI", "SmlI", "SnaBI", "SpeI", "SphI", "SspI", "StuI", "StyD4I", "StyI", "SwaI", "TaqI", "TfiI", "TseI", "Tsp45I", "TspMI", "TspRI", "Tth111I", "XcmI", "XhoI", "XmaI", "XmnI", "ZraI", " " };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPaneEnzymeList.setViewportView(jListRestrictionEnzymes);

        jLabel2.setText(org.openide.util.NbBundle.getMessage(GUIMain.class, "GUIMain.jLabel2.text")); // NOI18N

        jTextPane1.setBackground(new java.awt.Color(204, 204, 255));
        jTextPane1.setFont(new java.awt.Font("Tahoma", 0, 18));
        jTextPane1.setText(org.openide.util.NbBundle.getMessage(GUIMain.class, "GUIMain.jTextPane1.text")); // NOI18N
        jScrollPane4.setViewportView(jTextPane1);

        jTextFieldlowerBound.setText(org.openide.util.NbBundle.getMessage(GUIMain.class, "GUIMain.jTextFieldlowerBound.text")); // NOI18N
        jTextFieldlowerBound.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldlowerBoundActionPerformed(evt);
            }
        });

        jLabel3.setText(org.openide.util.NbBundle.getMessage(GUIMain.class, "GUIMain.jLabel3.text")); // NOI18N

        jButtonFindOpt.setText(org.openide.util.NbBundle.getMessage(GUIMain.class, "GUIMain.jButtonFindOpt.text")); // NOI18N
        jButtonFindOpt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFindOptActionPerformed(evt);
            }
        });

        jLabel4.setText(org.openide.util.NbBundle.getMessage(GUIMain.class, "GUIMain.jLabel4.text")); // NOI18N

        jButtonRun.setText(org.openide.util.NbBundle.getMessage(GUIMain.class, "GUIMain.jButtonRun.text")); // NOI18N
        jButtonRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRunActionPerformed(evt);
            }
        });

        jButtonAddEnzyme.setText(org.openide.util.NbBundle.getMessage(GUIMain.class, "GUIMain.jButtonAddEnzyme.text")); // NOI18N
        jButtonAddEnzyme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddEnzymeActionPerformed(evt);
            }
        });

        jScrollPane3.setViewportView(jListParts);

        jLabel5.setText(org.openide.util.NbBundle.getMessage(GUIMain.class, "GUIMain.jLabel5.text")); // NOI18N

        jLabel6.setText(org.openide.util.NbBundle.getMessage(GUIMain.class, "GUIMain.jLabel6.text")); // NOI18N

        buttonGroupConfirmation.add(jRadioButtonCircular);
        jRadioButtonCircular.setSelected(true);
        jRadioButtonCircular.setText(org.openide.util.NbBundle.getMessage(GUIMain.class, "GUIMain.jRadioButtonCircular.text")); // NOI18N
        jRadioButtonCircular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonCircularActionPerformed(evt);
            }
        });

        buttonGroupConfirmation.add(jRadioButtonLinear);
        jRadioButtonLinear.setText(org.openide.util.NbBundle.getMessage(GUIMain.class, "GUIMain.jRadioButtonLinear.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jRadioButtonCircular, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jRadioButtonLinear))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(44, 44, 44))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPaneEnzymeList, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButtonAddEnzyme))
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButtonRun)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel2))
                        .addGap(13, 13, 13))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonFindOpt)
                    .addComponent(jTextFieldlowerBound, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(40, 40, 40))
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 905, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(20, 20, 20))
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(9, 9, 9))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jRadioButtonCircular)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jRadioButtonLinear)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(157, 157, 157)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonAddEnzyme)
                                    .addComponent(jButtonRun))
                                .addGap(9, 9, 9))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPaneEnzymeList, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldlowerBound, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonFindOpt)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldlowerBoundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldlowerBoundActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldlowerBoundActionPerformed

    private void jButtonFindOptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFindOptActionPerformed
        // TODO add your handling code here:
        if (this.jTextAreaPlasmidText.getText().length() < 5)
        {
            JOptionPane.showMessageDialog(this,"Please copy-paste DNA string of plasmid in box first");
            return;
        }

        //first, search based on one enzyme
       Enumeration keys = this.enzymeTable.keys();
       ArrayList<String> enzlinks = new ArrayList<String>();
       ArrayList<String>enzymeSites = new ArrayList<String>();
       ArrayList<String> output = new ArrayList<String>();
       String plasmid = this.jTextAreaPlasmidText.getText();
       int lowerBound = Integer.parseInt(this.jTextFieldlowerBound.getText());
       while (keys.hasMoreElements())
       {
           String token = (String)keys.nextElement();
           enzlinks.add(token);
           enzymeSites.add((String)this.enzymeTable.get(token));

       }
       //JOptionPane.showMessageDialog(this,"use Enzymesites :" + enzymeSites + "enzymelinks :" + enzlinks);
       ArrayList<String> allLocations = new ArrayList<String>();
       Mapper mapper = Mapper.getOptimalMap(plasmid, enzymeSites, enzlinks,lowerBound, output,allLocations);
       JOptionPane.showMessageDialog(this,"use Enzyme(s) :" + output + "locations: " + allLocations);
       
       ArrayList<String> frags = mapper.getFragments();
       OutputFrame frame = new OutputFrame("Optimal output generated for plasmid string");
       frame.writeOutput(frags.size() > 1, frags, output, allLocations);
       frame.writeToPlasmidViewer(mapper.getAnnotatedPlasmidString(),this.enzymeTable);
       frame.setVisible(true);

    }//GEN-LAST:event_jButtonFindOptActionPerformed

    private void jButtonRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRunActionPerformed
        // TODO add your handling code here:
        Object [] values = this.jListRestrictionEnzymes.getSelectedValues();
        if (values.length == 0)
        {
            JOptionPane.showMessageDialog(this,"Please choose at least ONE Restriction Enzyme");
            return;
        }
           //JOptionPane.showMessageDialog(this, (String)values[0]);
        ArrayList<String> tokens = new ArrayList<String>();
        //ArrayList<String> enzymes = new ArrayList<String>();
        for (int i =0; i < values.length ; i++)
            {
                String token = (String)values[i];
               // String token = st.substring(0, st.lastIndexOf(')') + 1);
                //enzymes.add(st);
                tokens.add(token);
                //JOptionPane.showMessageDialog(this,token);
            }
        //first check if plasmid DNA is copy pasted in the text window,
        if (this.jTextAreaPlasmidText.getText().length() > 5)
        {
            Mapper mapper = new Mapper(this.jTextAreaPlasmidText.getText());
            boolean hasResult =false;
            ArrayList<String> allLocations = new ArrayList<String>();
            for (int i =0 ; i < tokens.size(); i ++)
            {
                String token = tokens.get(i);
                ArrayList<Integer> locations = mapper.markRestrictionSites((String)this.enzymeTable.get("("+token+")"),"["+token+"]");
                if (locations.size() > 0)
                    hasResult =true;
                allLocations.add(locations.toString());
            }
            ArrayList<String> frags = mapper.getFragments(this.jRadioButtonCircular.isSelected());
            OutputFrame frame = new OutputFrame("Output for Plasmid String");
            frame.writeOutput(hasResult, frags, tokens, allLocations);
            frame.writeToPlasmidViewer(mapper.getAnnotatedPlasmidString(),this.enzymeTable);
            frame.setVisible(true);
            
        }
        // Next check if plasmids are selected in the plasmid list
        Object[] plasmidNames = this.jListPlasmid.getSelectedValues();
        for (int index =0; index < plasmidNames.length; index++)
        {
            String s = plasmidNames[index].toString();
            Plasmid plasmid = Plasmid.retrieveByName(s);
            //String seq = plasmid.toString();
           // JOptionPane.showMessageDialog(this,plasmid.getSeq().toString());
            if (plasmid.getSeq()==null || plasmid.getSeq().toString().length()== 0)
            {
                JOptionPane.showMessageDialog(this,"sorry cannot find sequence for "+s);
                continue;
            }
            Mapper mapper = new Mapper(plasmid.getSeq().toString());
            boolean hasResult =false;
            ArrayList<String> allLocations = new ArrayList<String>();
            for (int i =0 ; i < tokens.size(); i ++)
            {
                String token = tokens.get(i);
                ArrayList<Integer> locations = mapper.markRestrictionSites((String)this.enzymeTable.get("("+token+")"),"["+token+"]");
                if (locations.size() > 0)
                    hasResult =true;
                allLocations.add(locations.toString());
            }
            ArrayList<String> frags = mapper.getFragments(this.jRadioButtonCircular.isSelected());
            OutputFrame frame = new OutputFrame("Output for Plasmid : "+s);
            frame.writeOutput(hasResult, frags, tokens, allLocations);
            frame.writeToPlasmidViewer(mapper.getAnnotatedPlasmidString(),this.enzymeTable);
            frame.setVisible(true);

            
        }
        
         // Next check if parts are selected in the plasmid list
        Object[] partNames = this.jListParts.getSelectedValues();
        for (int index =0; index < partNames.length; index++)
        {
            String s = partNames[index].toString();
            Part part = Part.retrieveByName(s);
            //String seq = plasmid.toString();
           // JOptionPane.showMessageDialog(this,plasmid.getSeq().toString());
            Mapper mapper = new Mapper(part.getSeq().toString());
            boolean hasResult =false;
            ArrayList<String> allLocations = new ArrayList<String>();
            for (int i =0 ; i < tokens.size(); i ++)
            {
                String token = tokens.get(i);
                //JOptionPane.showMessageDialog(this,(String)this.enzymeTable.get(token)+","+token);
                ArrayList<Integer> locations = mapper.markRestrictionSites((String)this.enzymeTable.get("("+token+")"),"["+token+"]");
                if (locations.size() > 0)
                    hasResult =true;
                allLocations.add(locations.toString());
            }
            ArrayList<String> frags = mapper.getFragments(this.jRadioButtonCircular.isSelected());
            OutputFrame frame = new OutputFrame("Output for Part : "+s);
            frame.writeOutput(hasResult, frags, tokens, allLocations);
            frame.writeToPlasmidViewer(mapper.getAnnotatedPlasmidString(),this.enzymeTable);
            frame.setVisible(true);

            
        }
        // test frame..
        //OutputFrame frame = new OutputFrame("test");
        //frame.setVisible(true);
    }//GEN-LAST:event_jButtonRunActionPerformed

private void jButtonAddEnzymeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddEnzymeActionPerformed
// TODO add your handling code here:
    String msg ="Enter new enzyme as following : <(Enzyme abbreviation)> [SPACE] <Enzyme Name> [SPACE] <DNA string that matches restriction site for enzyme>"
            + "\nFor example : (E1) MyEnzyme AGTCC";
    String entry = JOptionPane.showInputDialog(this,msg);
    if (entry ==null) return;
    String [] tokens = entry.split("\\s+");
    if (tokens.length != 3)
    {
        JOptionPane.showMessageDialog(this, "your entry format is wrong, you should have 3 pieces of information. Please try again");
        return;
    }
    
    else if (tokens[0].charAt(0)!='(' && tokens[0].charAt(tokens[0].length() -1) != ')')
    {
        JOptionPane.showMessageDialog(this,"Your first entry should be within brackets. Please try again");
        return;
        
    }
    
    else if (this.enzymeTable.containsKey(tokens[0]))
    {
        JOptionPane.showMessageDialog(this, tokens[0] + " is already in use. Please use another abbreviation ");
        return;
    }
    
    else if (this.enzymeTable.containsValue(tokens[2]))
    {
        int i = JOptionPane.showConfirmDialog(this, tokens[2] + " is already mapped to an enzyme. Do you want a duplicate?");
        if ( i > 0) return;
    }
    
    this.enzymeTable.put(tokens[0], tokens[2]);
    DefaultListModel listModel = new DefaultListModel();
    
    listModel.addElement(tokens[0]+" - "+tokens[1]);
    ListModel model = this.jListRestrictionEnzymes.getModel();
    for (int i = 0 ; i < model.getSize(); i ++)
        listModel.addElement(model.getElementAt(i));
    
    this.jListRestrictionEnzymes.setModel(listModel);
    this.jListRestrictionEnzymes.updateUI();
    
    //add this enzyme to the enzyme.txt file.
    try
    {
    Writer output = new BufferedWriter(new FileWriter(new File(GUIMain.enzymefile),true));
    
    output.write(tokens[0] + " " +tokens[1] + " " + tokens[2] + "\n");
    output.close();
    }
    catch (IOException e) 
    {
        JOptionPane.showMessageDialog(this, "Error writing enzyme file! "+ e.getMessage() );
    }
    
   
    
}//GEN-LAST:event_jButtonAddEnzymeActionPerformed

private void jRadioButtonCircularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonCircularActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jRadioButtonCircularActionPerformed

/**
 * populates restriction enzyme list from the external text file, which stores user's previously 
 * added list of enzymes in addition to the hard-coded core restriction enzymes in the UI list.
 * 
 * Does 2 things : 
 * 1. Populates the hashtable "enzymetable" with the entries in the text file
 * 2. re-populates the List of enzymes in the UI, so that the new enzymes also shows in it.
 */
private void populateFromEnzymeFile()
{
    try
    {
    BufferedReader input = new BufferedReader(new FileReader(new File(GUIMain.enzymefile)));
    if (input == null) return; //no enzyme file present at the moment (?)
    String line =null;
    DefaultListModel listModel = new DefaultListModel(); 
    while ((line = input.readLine())!=null)
    {
        String [] tokens = line.split("\\s+");
        if (tokens.length != 3) continue; // something wrong with this line in text!       
        this.enzymeTable.put(tokens[0], tokens[2]);        
        listModel.addElement(tokens[0]+" - "+tokens[1]);
    }
    ListModel model = this.jListRestrictionEnzymes.getModel();
    for (int i = 0 ; i < model.getSize(); i ++)
        listModel.addElement(model.getElementAt(i));
    
    this.jListRestrictionEnzymes.setModel(listModel);
    this.jListRestrictionEnzymes.updateUI();
    }
    catch (IOException e)
    {
        JOptionPane.showMessageDialog(this,"Error reading from enzyme file: "+e.getMessage());
    }
}
 /**
     * Returns a list of all possible "matches" that is annotations that might be there 
     * in the plasmid string.
     * @return ArrayList of all matches
     */
    private ArrayList<String> getAllMatches()
    {
        ArrayList<String> match = new ArrayList<String>();
        Enumeration keys = this.enzymeTable.keys();
        while (keys.hasMoreElements())
        {
            String key = (String)keys.nextElement();
            String entry ="["+ key +"]"+ this.enzymeTable.get(key);
            match.add(entry);
        }
       // JOptionPane.showMessageDialog(this,match);
        return match;
        
    }
    
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUIMain().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupConfirmation;
    private javax.swing.JButton jButtonAddEnzyme;
    private javax.swing.JButton jButtonFindOpt;
    private javax.swing.JButton jButtonRun;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JList jListParts;
    private javax.swing.JList jListPlasmid;
    private javax.swing.JList jListRestrictionEnzymes;
    private javax.swing.JRadioButton jRadioButtonCircular;
    private javax.swing.JRadioButton jRadioButtonLinear;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPaneEnzymeList;
    private javax.swing.JTextArea jTextAreaPlasmidText;
    private javax.swing.JTextField jTextFieldlowerBound;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables

}
