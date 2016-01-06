package com.it.navigator

import grails.transaction.Transactional


class ClientService {

    def serviceMethod() {

    }
    
    def getClients() {
        Client.list();
    }
    
    @Transactional
    def saveContact(def contact) {
        contact.merge(flush:true);
    }
}
