package org.clothocad.tool.eugenescripter;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author cassie
 */
public enum TokenType {

    T__16(16, "character"),
    T__17(17, "character"),
    T__18(18, "character"),
    T__19(19, "character"),
    T__20(20, "character"),
    T__21(21, "character"),
    T__22(22, "character"),
    T__23(23, "character"),
    T__24(24, "character"),
    T__25(25, "character"),
    T__26(26, "character"),
    T__27(27, "character"),
    T__28(28, "character"),
    T__29(29, "character"),
    T__30(30, "character"),
    T__31(31, "character"),
    T__32(32, "character"),
    T__33(33, "character"),
    T__34(34, "character"),
    T__35(35, "character"),
    T__36(36, "character"),
    T__37(37, "character"),
    T__38(38, "character"),
    T__39(39, "character"),
    T__40(40, "character"),
    T__41(41, "character"),
    T__42(42, "character"),
    T__43(43, "type"),
    T__44(44, "character"),
    T__45(45, "character"),
    T__46(46, "character"),
    T__47(47, "character"),
    T__48(48, "character"),
    T__49(49, "character"),
    T__50(50, "character"),
    T__51(51, "character"),
    T__52(52, "character"),
    T__53(53, "character"),
    T__54(54, "character"),
    T__55(55, "character"),
    T__56(56, "character"),
    T__57(57, "character"),
    T__58(58, "character"),
    T__59(59, "character"),
    T__60(60, "character"),
    T__61(61, "character"),
    T__62(62, "character"),
    T__63(63, "character"),
    T__64(64, "character"),
    T__65(65, "character"),
    T__66(66, "character"),
    T__67(67, "character"),
    T__68(68, "character"),
    T__69(69, "character"),
    T__70(70, "character"),
    T__71(71, "character"),
    T__72(72, "character"),
    T__73(73, "character"),
    T__74(74, "character"),
    T__75(75, "character"),
    T__76(76, "character"),
    T__77(77, "character"),
    T__78(78, "character"),
    T__79(79, "character"),
    T__80(80, "character"),
    T__81(81, "character"),
    T__82(82, "character"),
    T__83(83, "character"),
    T__84(84, "character"),
    T__85(85, "character"),
    T__86(86, "character"),
    T__87(87, "character"),
    T__88(88, "character"),
    T__89(89, "character"),
    T__90(90, "character"),
    T__91(91, "character"),
    T__92(92, "character"),
    T__93(93, "character"),
    T__94(94, "character"),
    T__95(95, "character"),
    T__96(96, "character"),
    CHAR(4, "character"),
    COMMENT(5, "comment"),
    ESC_SEQ(6, "character"),
    EXPONENT(7, "character"),
    FLOAT(8, "character"),
    HEX_DIGIT(9, "character"),
    ID(10, "character"),
    INT(11, "character"),
    OCTAL_ESC(12, "character"),
    STRING(13, "string"),
    UNICODE_ESC(14, "character"),
    WS(15, "whitespace");
    
    public int id;
    public String category;
    public String text;

    private TokenType(int id, String category) {
        this.id = id;
        this.category = category;
    }

    public static TokenType valueOf(int id) {
        TokenType[] values = values();
        for (TokenType value : values) {
            if (value.id == id) {
                return value;
            }
        }
        throw new IllegalArgumentException("The id " + id + " is not recognized");
    }
}
