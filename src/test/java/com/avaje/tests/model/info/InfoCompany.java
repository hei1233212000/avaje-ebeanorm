package com.avaje.tests.model.info;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class InfoCompany extends Model {

  public static Finder<Long,InfoCompany> find = new Finder<Long,InfoCompany>(InfoCompany.class);

  @Id
  Long id;

  @Version
  Long version;

  String name;

  @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
  List<InfoContact> contacts = new ArrayList<InfoContact>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<InfoContact> getContacts() {
    return contacts;
  }

  public void setContacts(List<InfoContact> contacts) {
    this.contacts = contacts;
  }
}
