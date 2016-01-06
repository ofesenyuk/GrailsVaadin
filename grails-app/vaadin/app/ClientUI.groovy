package app

import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
import com.vaadin.server.VaadinRequest
import com.vaadin.ui.Label
import com.vaadin.grails.Grails;
import com.vaadin.shared.ui.label.ContentMode;

import app.table.builder.TableBuilder;

/**
 *
 *
 * @author
 */
class ClientUI extends UI {
    public final String CONTACT_DATA = '<h1>Contact data</h1>';
    def FIRST_NAME = 'First name';
    def MIDDLE_NAME = 'Middle name';
    def LAST_NAME = 'Last name';
    def PHONE_NUMBER = 'Phone number';
    def PHONE_TYPE = 'Phone type';
    def PHONE_COMMENT = 'Phone comment';

    @Override
    protected void init(VaadinRequest vaadinRequest) {

	VerticalLayout layout = new VerticalLayout();

        def CLIENT_TITLE_LABEL = new Label(CONTACT_DATA, ContentMode.HTML);
        layout.addComponent(CLIENT_TITLE_LABEL);
        def tableBuilder = new TableBuilder(layout);
        def captions = [:];
        captions[tableBuilder.FIRST_NAME] = FIRST_NAME;
        captions[tableBuilder.MIDDLE_NAME] = MIDDLE_NAME;
        captions[tableBuilder.LAST_NAME] = LAST_NAME;
        captions[tableBuilder.PHONE_NUMBER] = PHONE_NUMBER;
        captions[tableBuilder.PHONE_TYPE] = PHONE_TYPE;
        captions[tableBuilder.PHONE_COMMENT] = PHONE_COMMENT;
        def table = tableBuilder.createTable(captions);
        setContent(layout)
    }
}
