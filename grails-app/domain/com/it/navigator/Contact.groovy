package com.it.navigator

class Contact {

    static belongsTo = [Client];
    
    String phoneNumber;
    String phoneType;
    String comment;
    Client client;
    
    static constraints = {
        phoneNumber(nullable:false);
        phoneType(nullable:true);
        comment(maxSize:10000, nullable:true);
    }
    
    static mapping = {
        version false
    }
}
