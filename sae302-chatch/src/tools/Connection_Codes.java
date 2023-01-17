package tools;

public enum Connection_Codes {
    // Codes de connexion
    CONNEXION(1),
    DECONNEXION(2),

    CREATION_DISCUSSION(3),
    SUPPRESSION_DISCUSSION(4),
    MODIFICATION_DISCUSSION(5),

    ENVOI_MESSAGE(6),
    SUPPRESSION_MESSAGE(7),
    MODIFICATION_MESSAGE(8),

    CREATION_GROUPE(9),
    SUPPRESSION_GROUPE(10),
    MODIFICATION_GROUPE(11),

    CREATION_UTILISATEUR(12),
    SUPPRESSION_UTILISATEUR(13),
    MODIFICATION_UTILISATEUR(14),

    CREATION_ADMIN_GROUPE(15),
    SUPPRESSION_ADMIN_GROUPE(16),
    MODIFICATION_ADMIN_GROUPE(17),
    // Codes de déconnexion
    DECONNEXION_OK(18),
    DECONNEXION_KO(19),
    // Codes de création de discussion
    CREATION_DISCUSSION_OK(20),
    CREATION_DISCUSSION_KO(21),
    // Codes de suppression de discussion
    SUPPRESSION_DISCUSSION_OK(22),
    SUPPRESSION_DISCUSSION_KO(23),
    // Codes de modification de discussion
    MODIFICATION_DISCUSSION_OK(24),
    MODIFICATION_DISCUSSION_KO(25),
    // Codes d'envoi de message
    ENVOI_MESSAGE_OK(26),
    ENVOI_MESSAGE_KO(27),
    // Codes de suppression de message
    SUPPRESSION_MESSAGE_OK(28),
    SUPPRESSION_MESSAGE_KO(29),
    // Codes de modification de message
    MODIFICATION_MESSAGE_OK(30),
    MODIFICATION_MESSAGE_KO(31),
    // Codes de création de groupe
    CREATION_GROUPE_OK(32),
    CREATION_GROUPE_KO(33),
    // Codes de suppression de groupe
    SUPPRESSION_GROUPE_OK(34),
    SUPPRESSION_GROUPE_KO(35),
    // Codes de modification de groupe
    MODIFICATION_GROUPE_OK(36),
    MODIFICATION_GROUPE_KO(37),
    // Codes de création d'utilisateur
    CREATION_UTILISATEUR_OK(38),
    CREATION_UTILISATEUR_KO(39),
    // Codes de suppression d'utilisateur
    SUPPRESSION_UTILISATEUR_OK(40),
    SUPPRESSION_UTILISATEUR_KO(41),
    // Codes de modification d'utilisateur
    MODIFICATION_UTILISATEUR_OK(42),
    MODIFICATION_UTILISATEUR_KO(43),
    // Codes de création d'admin de groupe
    CREATION_ADMIN_GROUPE_OK(44),
    CREATION_ADMIN_GROUPE_KO(45),
    // Codes de suppression d'admin de groupe
    SUPPRESSION_ADMIN_GROUPE_OK(46),
    SUPPRESSION_ADMIN_GROUPE_KO(47),
    // Codes de modification d'admin de groupe
    MODIFICATION_ADMIN_GROUPE_OK(48),
    MODIFICATION_ADMIN_GROUPE_KO(49),
    // Codes de connexion
    CONNEXION_OK(50),
    CONNEXION_KO(51);

    private final int code;

    Connection_Codes(int i) {
        this.code = i;
    }
}
