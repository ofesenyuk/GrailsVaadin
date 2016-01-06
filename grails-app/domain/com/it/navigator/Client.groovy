package com.it.navigator

class Client {

    static hasMany = [contacts: Contact]
    
    String firstName;
    String middleName;
    String lastName;
    
    static constraints = {
        firstName(blank:false);
        lastName(blank:false);
        middleName(nullable:true);
    }
    
    static mapping = {
        contacts lazy: false
        version false
    }
}
