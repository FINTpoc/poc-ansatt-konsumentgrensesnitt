package no.fk.fint.user;

public enum Users {
    HFK("hfk.no", "https://register.geonorge.no/data/organizations/938626367_HFK_liten.png"),
    RFK("rfk.no", "http://www.rogfk.no/var/ezwebin_site/storage/images/filer-og-bilder/bilder_rfk_internett/logoer/rfk-logoer/rfk-logo-liggende-300dpi/79584-1-nor-NO/RFK-logo-liggende-300dpi_article_large.jpg");

    private String orgId;
    private String logo;

    Users(String orgId, String logo) {
        this.orgId = orgId;
        this.logo = logo;
    }

    public User get() {
        return new User(orgId, logo);
    }
}
