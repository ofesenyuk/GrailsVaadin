import grails.util.GrailsUtil;
import com.it.navigator.Client;
import com.it.navigator.Contact
class BootStrap {

    def init = { servletContext ->
        switch(GrailsUtil.environment) {
        case "development":
            Client.withNewTransaction {configureForDevelopment()};
            break
        case "production":
            configureForProduction()
            break
        }
    }
    def destroy = {
    }
    
    def configureForDevelopment() {
        def client = new Client(firstName: 'Ivan', lastName: 'Ivanenko', 
            middleName: 'Ivanov');
        client.save(flush:true, failOnError: true);
        def contact = new Contact(phoneNumber: '1(800) 555-1212', 
                                  phoneType: 'home')
        client.addToContacts(contact).save(flush:true, failOnError: true);
        client = new Client(firstName: 'Peter', lastName: 'Petrenko', 
            middleName: 'Petrovych');
        client.save(flush:true, failOnError: true);
        contact = new Contact(phoneNumber: '123', phoneType: 'work', 
            comment: 'do not disturb after 19:00');
        client.addToContacts(contact).save(flush:true, failOnError: true);
        client = new Client(firstName: 'Sydir', lastName: 'Sydorenko', 
            middleName: 'Sydorovych');
        client.save(flush:true, failOnError: true);
        contact = new Contact(phoneNumber: '1.800.555.1212', phoneType: 'work', 
            comment: 'call before 22:00');
        client.addToContacts(contact).save(flush:true, failOnError: true);
        client = new Client(firstName: 'Pasha', lastName: 'Pavlenko', 
            middleName: 'Pavlivna');
        client.save(flush:true, failOnError: true);
        contact = new Contact(phoneNumber: '800555.1212', 
            comment: 'call before 21:00');
        client.addToContacts(contact).save(flush:true, failOnError: true);
    }
}
