/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author fafulja
 */
@Entity
@Table(name = "video")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Video.findAll", query = "SELECT v FROM Video v"),
    @NamedQuery(name = "Video.findByIdVid", query = "SELECT v FROM Video v WHERE v.idVid = :idVid"),
    @NamedQuery(name = "Video.findByNaziv", query = "SELECT v FROM Video v WHERE v.naziv = :naziv"),
    @NamedQuery(name = "Video.findByTrajanje", query = "SELECT v FROM Video v WHERE v.trajanje = :trajanje"),
    @NamedQuery(name = "Video.findByDatum", query = "SELECT v FROM Video v WHERE v.datum = :datum"),
    @NamedQuery(name = "Video.findByVrijeme", query = "SELECT v FROM Video v WHERE v.vrijeme = :vrijeme")})
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idVid")
    private Integer idVid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "naziv")
    private String naziv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "trajanje")
    private double trajanje;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datum")
    @Temporal(TemporalType.DATE)
    private Date datum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vrijeme")
    @Temporal(TemporalType.TIME)
    private Date vrijeme;
    @ManyToMany(mappedBy = "videoList")
    private List<Kategorija> kategorijaList;
    @JoinColumn(name = "idKor", referencedColumnName = "idKor")
    @ManyToOne(optional = false)
    private Korisnik idKor;

    public Video() {
    }

    public Video(Integer idVid) {
        this.idVid = idVid;
    }

    public Video(Integer idVid, String naziv, double trajanje, Date datum, Date vrijeme) {
        this.idVid = idVid;
        this.naziv = naziv;
        this.trajanje = trajanje;
        this.datum = datum;
        this.vrijeme = vrijeme;
    }

    public Integer getIdVid() {
        return idVid;
    }

    public void setIdVid(Integer idVid) {
        this.idVid = idVid;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public double getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(double trajanje) {
        this.trajanje = trajanje;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public Date getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(Date vrijeme) {
        this.vrijeme = vrijeme;
    }

    @XmlTransient
    public List<Kategorija> getKategorijaList() {
        return kategorijaList;
    }

    public void setKategorijaList(List<Kategorija> kategorijaList) {
        this.kategorijaList = kategorijaList;
    }

    public Korisnik getIdKor() {
        return idKor;
    }

    public void setIdKor(Korisnik idKor) {
        this.idKor = idKor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVid != null ? idVid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Video)) {
            return false;
        }
        Video other = (Video) object;
        if ((this.idVid == null && other.idVid != null) || (this.idVid != null && !this.idVid.equals(other.idVid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Video " + this.idVid + " " + this.getNaziv();
    }
    
}
