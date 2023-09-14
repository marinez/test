package fr.insy2s.sesame.domain.enumeration;

public enum TypeContract {
    CDI("Contrat à Durée Indéterminée"),
    CDD("Contrat à Durée Déterminée"),
    PROFESSIONNALISATION("Contrat de Professionnalisation"),
    APPRENTISSAGE("Contrat d'Apprentissage"),
    TEMPS_PARTIEL("Temps Partiel"),
    CHANTIER("Contrat de Chantier"),
    PARTAGE("Contrat de Partage"),
    INTERMITTENT("Contrat d'Intermittent"),
    INTERIM("Contrat temporaire"),
    ANNUALISE("Contrat Annualisé");


    private final String description;

    TypeContract(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
