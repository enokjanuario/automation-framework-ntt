package com.automation.models.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Modelo que representa um Pais da API RESTCountries.
 *
 * Arquitetura:
 * - Estrutura aninhada para representar JSON complexo
 * - Utiliza classes internas para sub-objetos
 * - Flexivel para ignorar propriedades desconhecidas
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {

    @JsonProperty("name")
    private Name name;

    @JsonProperty("tld")
    private List<String> tld;

    @JsonProperty("cca2")
    private String cca2;

    @JsonProperty("cca3")
    private String cca3;

    @JsonProperty("cioc")
    private String cioc;

    @JsonProperty("independent")
    private Boolean independent;

    @JsonProperty("status")
    private String status;

    @JsonProperty("unMember")
    private Boolean unMember;

    @JsonProperty("currencies")
    private Map<String, Currency> currencies;

    @JsonProperty("capital")
    private List<String> capital;

    @JsonProperty("region")
    private String region;

    @JsonProperty("subregion")
    private String subregion;

    @JsonProperty("languages")
    private Map<String, String> languages;

    @JsonProperty("latlng")
    private List<Double> latlng;

    @JsonProperty("landlocked")
    private Boolean landlocked;

    @JsonProperty("borders")
    private List<String> borders;

    @JsonProperty("area")
    private Double area;

    @JsonProperty("population")
    private Long population;

    @JsonProperty("timezones")
    private List<String> timezones;

    @JsonProperty("continents")
    private List<String> continents;

    @JsonProperty("flags")
    private Flags flags;

    @JsonProperty("coatOfArms")
    private CoatOfArms coatOfArms;

    @JsonProperty("startOfWeek")
    private String startOfWeek;

    @JsonProperty("capitalInfo")
    private CapitalInfo capitalInfo;

    public Country() {
    }

    // Getters
    public Name getName() {
        return name;
    }

    public List<String> getTld() {
        return tld;
    }

    public String getCca2() {
        return cca2;
    }

    public String getCca3() {
        return cca3;
    }

    public String getCioc() {
        return cioc;
    }

    public Boolean getIndependent() {
        return independent;
    }

    public String getStatus() {
        return status;
    }

    public Boolean getUnMember() {
        return unMember;
    }

    public Map<String, Currency> getCurrencies() {
        return currencies;
    }

    public List<String> getCapital() {
        return capital;
    }

    public String getRegion() {
        return region;
    }

    public String getSubregion() {
        return subregion;
    }

    public Map<String, String> getLanguages() {
        return languages;
    }

    public List<Double> getLatlng() {
        return latlng;
    }

    public Boolean getLandlocked() {
        return landlocked;
    }

    public List<String> getBorders() {
        return borders;
    }

    public Double getArea() {
        return area;
    }

    public Long getPopulation() {
        return population;
    }

    public List<String> getTimezones() {
        return timezones;
    }

    public List<String> getContinents() {
        return continents;
    }

    public Flags getFlags() {
        return flags;
    }

    public CoatOfArms getCoatOfArms() {
        return coatOfArms;
    }

    public String getStartOfWeek() {
        return startOfWeek;
    }

    public CapitalInfo getCapitalInfo() {
        return capitalInfo;
    }

    // Setters
    public void setName(Name name) {
        this.name = name;
    }

    public void setTld(List<String> tld) {
        this.tld = tld;
    }

    public void setCca2(String cca2) {
        this.cca2 = cca2;
    }

    public void setCca3(String cca3) {
        this.cca3 = cca3;
    }

    public void setCioc(String cioc) {
        this.cioc = cioc;
    }

    public void setIndependent(Boolean independent) {
        this.independent = independent;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUnMember(Boolean unMember) {
        this.unMember = unMember;
    }

    public void setCurrencies(Map<String, Currency> currencies) {
        this.currencies = currencies;
    }

    public void setCapital(List<String> capital) {
        this.capital = capital;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setSubregion(String subregion) {
        this.subregion = subregion;
    }

    public void setLanguages(Map<String, String> languages) {
        this.languages = languages;
    }

    public void setLatlng(List<Double> latlng) {
        this.latlng = latlng;
    }

    public void setLandlocked(Boolean landlocked) {
        this.landlocked = landlocked;
    }

    public void setBorders(List<String> borders) {
        this.borders = borders;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

    public void setTimezones(List<String> timezones) {
        this.timezones = timezones;
    }

    public void setContinents(List<String> continents) {
        this.continents = continents;
    }

    public void setFlags(Flags flags) {
        this.flags = flags;
    }

    public void setCoatOfArms(CoatOfArms coatOfArms) {
        this.coatOfArms = coatOfArms;
    }

    public void setStartOfWeek(String startOfWeek) {
        this.startOfWeek = startOfWeek;
    }

    public void setCapitalInfo(CapitalInfo capitalInfo) {
        this.capitalInfo = capitalInfo;
    }

    /**
     * Classe interna para representar o nome do pais.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Name {

        @JsonProperty("common")
        private String common;

        @JsonProperty("official")
        private String official;

        @JsonProperty("nativeName")
        private Map<String, NativeName> nativeName;

        public Name() {
        }

        public Name(String common, String official, Map<String, NativeName> nativeName) {
            this.common = common;
            this.official = official;
            this.nativeName = nativeName;
        }

        public String getCommon() {
            return common;
        }

        public void setCommon(String common) {
            this.common = common;
        }

        public String getOfficial() {
            return official;
        }

        public void setOfficial(String official) {
            this.official = official;
        }

        public Map<String, NativeName> getNativeName() {
            return nativeName;
        }

        public void setNativeName(Map<String, NativeName> nativeName) {
            this.nativeName = nativeName;
        }

        @Override
        public String toString() {
            return "Name{common='" + common + "', official='" + official + "'}";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Name name = (Name) o;
            return Objects.equals(common, name.common) && Objects.equals(official, name.official);
        }

        @Override
        public int hashCode() {
            return Objects.hash(common, official);
        }
    }

    /**
     * Classe interna para nome nativo.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NativeName {

        @JsonProperty("official")
        private String official;

        @JsonProperty("common")
        private String common;

        public NativeName() {
        }

        public NativeName(String official, String common) {
            this.official = official;
            this.common = common;
        }

        public String getOfficial() {
            return official;
        }

        public void setOfficial(String official) {
            this.official = official;
        }

        public String getCommon() {
            return common;
        }

        public void setCommon(String common) {
            this.common = common;
        }

        @Override
        public String toString() {
            return "NativeName{official='" + official + "', common='" + common + "'}";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NativeName that = (NativeName) o;
            return Objects.equals(official, that.official) && Objects.equals(common, that.common);
        }

        @Override
        public int hashCode() {
            return Objects.hash(official, common);
        }
    }

    /**
     * Classe interna para moeda.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Currency {

        @JsonProperty("name")
        private String name;

        @JsonProperty("symbol")
        private String symbol;

        public Currency() {
        }

        public Currency(String name, String symbol) {
            this.name = name;
            this.symbol = symbol;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return "Currency{name='" + name + "', symbol='" + symbol + "'}";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Currency currency = (Currency) o;
            return Objects.equals(name, currency.name) && Objects.equals(symbol, currency.symbol);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, symbol);
        }
    }

    /**
     * Classe interna para bandeiras.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Flags {

        @JsonProperty("png")
        private String png;

        @JsonProperty("svg")
        private String svg;

        @JsonProperty("alt")
        private String alt;

        public Flags() {
        }

        public Flags(String png, String svg, String alt) {
            this.png = png;
            this.svg = svg;
            this.alt = alt;
        }

        public String getPng() {
            return png;
        }

        public void setPng(String png) {
            this.png = png;
        }

        public String getSvg() {
            return svg;
        }

        public void setSvg(String svg) {
            this.svg = svg;
        }

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        @Override
        public String toString() {
            return "Flags{png='" + png + "', svg='" + svg + "'}";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Flags flags = (Flags) o;
            return Objects.equals(png, flags.png) && Objects.equals(svg, flags.svg);
        }

        @Override
        public int hashCode() {
            return Objects.hash(png, svg);
        }
    }

    /**
     * Classe interna para brasao.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CoatOfArms {

        @JsonProperty("png")
        private String png;

        @JsonProperty("svg")
        private String svg;

        public CoatOfArms() {
        }

        public CoatOfArms(String png, String svg) {
            this.png = png;
            this.svg = svg;
        }

        public String getPng() {
            return png;
        }

        public void setPng(String png) {
            this.png = png;
        }

        public String getSvg() {
            return svg;
        }

        public void setSvg(String svg) {
            this.svg = svg;
        }

        @Override
        public String toString() {
            return "CoatOfArms{png='" + png + "', svg='" + svg + "'}";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CoatOfArms that = (CoatOfArms) o;
            return Objects.equals(png, that.png) && Objects.equals(svg, that.svg);
        }

        @Override
        public int hashCode() {
            return Objects.hash(png, svg);
        }
    }

    /**
     * Classe interna para informacoes da capital.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CapitalInfo {

        @JsonProperty("latlng")
        private List<Double> latlng;

        public CapitalInfo() {
        }

        public CapitalInfo(List<Double> latlng) {
            this.latlng = latlng;
        }

        public List<Double> getLatlng() {
            return latlng;
        }

        public void setLatlng(List<Double> latlng) {
            this.latlng = latlng;
        }

        @Override
        public String toString() {
            return "CapitalInfo{latlng=" + latlng + "}";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CapitalInfo that = (CapitalInfo) o;
            return Objects.equals(latlng, that.latlng);
        }

        @Override
        public int hashCode() {
            return Objects.hash(latlng);
        }
    }

    /**
     * Obtem o nome comum do pais de forma segura.
     *
     * @return Nome comum ou null se nao disponivel
     */
    public String getCommonName() {
        return name != null ? name.getCommon() : null;
    }

    /**
     * Obtem o nome oficial do pais de forma segura.
     *
     * @return Nome oficial ou null se nao disponivel
     */
    public String getOfficialName() {
        return name != null ? name.getOfficial() : null;
    }

    /**
     * Obtem a capital principal do pais.
     *
     * @return Nome da capital ou null se nao disponivel
     */
    public String getPrimaryCapital() {
        return capital != null && !capital.isEmpty() ? capital.get(0) : null;
    }

    @Override
    public String toString() {
        return "Country{" +
                "name=" + name +
                ", cca2='" + cca2 + '\'' +
                ", capital=" + capital +
                ", region='" + region + '\'' +
                ", population=" + population +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return Objects.equals(cca2, country.cca2) && Objects.equals(cca3, country.cca3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cca2, cca3);
    }
}
