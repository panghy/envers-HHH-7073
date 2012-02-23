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
public class Person implements Serializable {
  @Id
  @GeneratedValue
  private long id;

  /**
   * A many-to-many relationship linking people to other people's addresses, effectively an address book.
   */
  @ManyToMany(cascade = {CascadeType.PERSIST})
  @JoinTable(name = "AddressBook",
      joinColumns = {@JoinColumn(name = "personId", nullable = false)},
      inverseJoinColumns = {@JoinColumn(name = "addressId", nullable = false)})
  private Set<Address> addressBook = new HashSet<Address>();

  /**
   * The addresses of this person. Contains all of the addresses that this person owns.
   */
  @OneToMany(mappedBy = "person", cascade = {CascadeType.PERSIST}, orphanRemoval = true)
  private Set<Address> myAddresses = new HashSet<Address>();

  public Person() {
  }

  /**
   * Add a new address for this person.
   *
   * @param address Address to add.
   */
  public void addAddress(Address address) {
    address.person = this;
    myAddresses.add(address);
    addressBook.add(address);
  }

  /**
   * Add a new address book entry for this person. The address must belong to someone else.
   *
   * @param address Address to add to address book.
   */
  public void addNewAddressBookEntry(Address address) {
    if (address.person.equals(this))
      throw new IllegalArgumentException("Cannot add your own address to your address book, " +
          "address must belong to somebody else");
    addressBook.add(address);
    address.getPersons().add(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Person)) return false;

    Person person = (Person) o;

    if (id != person.id) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return (int) (id ^ (id >>> 32));
  }

  @Override
  public String toString() {
    return "Person(id = " + id + ")";
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Set<Address> getAddressBook() {
    return myAddresses;
  }

  public void setAddressBook(Set<Address> addressBook) {
    this.myAddresses = addressBook;
  }
}
