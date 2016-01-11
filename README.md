# GrailsVaadin 
Application is developed with using Grails and Vaadin
To run application 1)download it (see github docs), 2)download grails2.4.4 from https://grails.org/download.html, section Previous Versions, 3)unpack grails-2.4.4, 4) go to application root folder, 5) execute path_to_grails-2.4.4/bin/grails run-app (e.g 
c:\Users\sf\Java\Grails\grails-2.4.4\bin\grails run-app). I used 2.4.4 version because I am familiar with is and because Netbeans IDE hardly supports 3.1 version.  
DB connection properties are given in \GrailsVaadinP\grails-app\conf\DataSource.groovy. At present, there is connection to h2DB; an example MySQL DB connection properties are commented in this file.
UI-part is located inside GrailsVaadinP\grails-app\vaadin\app\ folder

I'd like to make some remarks concerning my coding and concerning some features of Groovy that are strange to pure Java developer.
Keyword 'def' used to declare a (private) field or a (public) method. The corresponding type is determined at object creation (e.g., with new).
After grails application creation, 
I edited BootStrap.groovy to load some sample data, 
I edited DataSource.groovy, 
I commented all inside mappings in UrlMappings.groovy, 
I set mapping to "app.ClientUI" in VaadinConfig.groovy, 
I created 2 domain classes: Client.groovy and Contact.groovy, I set relations between them with "belongsTo" and "hasMany", I wrote "contacts lazy: false" to exclude LazyLoading exception inside Client.groovy and "version false" to exclude some problems with (optimistic) locking inside both classes.
I created ClientService.groovy;
I creted ClientUI.groovy and TableBuilder.groovy with UI logic.
