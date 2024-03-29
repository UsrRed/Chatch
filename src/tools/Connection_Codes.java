package tools;

/**
 * @author : Eliott LEBOSSE et Yohann DENOYELLE
 * Cette enumération représente les différentes intéractions possibles entre le client et le serveur.
 * Elle permet de formaliser les échanges entre les deux.
 */
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

    CREATION_UTILISATEUR(12),
    SUPPRESSION_UTILISATEUR(13),
    MODIFICATION_UTILISATEUR(14),

    CREATION_ADMIN_DISCUSSION(15),
    SUPPRESSION_ADMIN_DISCUSSION(16),
    MODIFICATION_ADMIN_DISCUSSION(17),
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
    CREATION_ADMIN_DISCUSSION_OK(44),
    CREATION_ADMIN_DISCUSSION_KO(45),
    // Codes de suppression d'admin de groupe
    SUPPRESSION_ADMIN_DISCUSSION_OK(46),
    SUPPRESSION_ADMIN_DISCUSSION_KO(47),
    // Codes de modification d'admin de groupe
    MODIFICATION_ADMIN_DISCUSSION_OK(48),
    MODIFICATION_ADMIN_DISCUSSION_KO(49),
    // Codes de connexion
    CONNEXION_OK(50),
    CONNEXION_KO(51),
    // Codes de récupération de données
    RECUPERATION_MESSAGES(52),
    RECUPERATION_DISCUSSIONS(53),
    RECUPERATION_DISCUSSION(54),
    RECUPERATION_UTILISATEURS(55),
    RECUPERATION_ADMIN_GROUPE(56),
    // Codes de récupération de données OK
    RECUPERATION_MESSAGES_OK(57),
    RECUPERATION_DISCUSSIONS_OK(58),
    RECUPERATION_DISCUSSION_OK(59),
    RECUPERATION_UTILISATEURS_OK(60),
    RECUPERATION_ADMIN_DISCUSSION_OK(61),
    // Codes de récupération de données KO
    RECUPERATION_MESSAGES_KO(62),
    RECUPERATION_DISCUSSIONS_KO(63),
    RECUPERATION_DISCUSSION_KO(64),
    RECUPERATION_UTILISATEURS_KO(65),
    RECUPERATION_ADMIN_DISCUSSION_KO(66),
    AJOUT_UTILISATEUR_DISCUSSION(67),
    SUPPRESSION_UTILISATEUR_DISCUSSION(68),
    AJOUT_UTILISATEUR_DISCUSSION_OK(69),
    AJOUT_UTILISATEUR_DISCUSSION_KO(70),
    SUPPRESSION_UTILISATEUR_DISCUSSION_OK(71),
    SUPPRESSION_UTILISATEUR_DISCUSSION_KO(72);

    private final int code;

    Connection_Codes(int i) {
        this.code = i;
    }
}
