package org.hibernate.antoniak;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@Entity
@Audited
public class Address implements Serializable {
  @Id
  @GeneratedValue
  private long id;

  /**
   * List of people who has this address in their address book.
   */
  @ManyToMany(cascade = {CascadeType.PERSIST})
  private Set<Person> persons = new HashSet<Person>();

  /**
   * The person who owns this address.
   */
  @ManyToOne
  @JoinColumn(nullable = false, updatable = false)
  Person person;

  public Address() {
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Address)) return false;

    Address address = (Address) o;

    if (id != address.id) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return (int) (id ^ (id >>> 32));
  }

  @Override
  public String toString() {
    return "Address(id = " + id + ")";
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Set<Person> getPersons() {
    return persons;
  }

  public void setPersons(Set<Person> persons) {
    this.persons = persons;
  }
}
