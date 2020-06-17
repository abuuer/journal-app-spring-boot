/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.bean;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author anoir
 */
@Entity
public class Issue implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int number;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date publishDate;
    private String status;
    private String startMonth;
    private String endMonth;
    private String issn;

    @JsonBackReference
    @ManyToOne
    private Volume volume;

    @JsonManagedReference
    @OneToMany(mappedBy = "issue")
    private List<Article> articles;

    @JsonManagedReference
    @OneToMany(mappedBy = "issue")
    private List<FileInfo> fileInfos;

    public Issue() {
        this.status = "On Hold";
    }

    public Issue(int number, String status, String startMonth, String endMonth, String issn, Volume volume) {
        this.number = number;
        this.status = status;
        this.startMonth = startMonth;
        this.endMonth = endMonth;
        this.issn = issn;
        this.volume = volume;
    }

    
    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<FileInfo> getFileInfos() {
        return fileInfos;
    }

    public void setFileInfos(List<FileInfo> fileInfos) {
        this.fileInfos = fileInfos;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(String startMonth) {
        this.startMonth = startMonth;
    }

    public String getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(String endMonth) {
        this.endMonth = endMonth;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Volume getVolume() {
        return volume;
    }

    public void setVolume(Volume volume) {
        this.volume = volume;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Issue)) {
            return false;
        }
        Issue other = (Issue) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.journal.journal.bean.Issue[ id=" + id + " ]";
    }

}
