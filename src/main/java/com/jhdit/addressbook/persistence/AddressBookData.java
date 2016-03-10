package com.jhdit.addressbook.persistence;

import com.jhdit.addressbook.domain.Contact;

import java.util.Set;

/**
 * Interface specifying contract for Address Book data (without specifying/providing persistence implementation)
 */

public interface AddressBookData {
    Set<Contact> getContacts();
}
