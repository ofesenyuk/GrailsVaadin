/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package app.table.builder

import com.vaadin.ui.Layout;
import com.vaadin.ui.Label;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Grid;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.TextField;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.grails.Grails
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

//import org.codehaus.groovy.grails.commons.ApplicationHolder

import com.it.navigator.Client;
import com.it.navigator.ClientService

/**
 *
 * @author sf
 */


class TableBuilder {
    
    def MESSAGE = '''<p style="color:green;background-color: red;">
                     Valid edited PHONE data 
                     will be persisted AFTER SELECTION CHANGE</p>''';
    def PERSIST_MESSAGE = '''<p style="color:yellow;background-color: blue;">
                             PHONE data are persisted </p>''';
    def messageLabel = new Label(MESSAGE, ContentMode.HTML);
    def startTime = System.currentTimeMillis();
    def MESSAGE_VISIBILITY_INTERVAL = 10000;
    def VALIDATION_MESSAGE = 'Please, enter a valid phone number';
    def WORK_PHONE_TYPE = 'work';
    def EDITOR_SAVE_BUTTON_CAPTION = 'Save to table';
    def SAVE_DB_BUTTON_CAPTION = 'Save to DB';
        
    def FIRST_NAME = 'first name';
    def MIDDLE_NAME = 'middle name';
    def LAST_NAME = 'last name';
    def PHONE_NUMBER = 'phone number';
    def PHONE_TYPE = 'phone type';
    def PHONE_COMMENT = 'phone comment';
//    Attributes without an access modifier are considered private.
    def layout;
    def table;
    def clientList = [];
//    def newClientList = [];
    def tableColumns = [:];
    def isMessageShown = true;
    def phoneValidation = /^[01]?\s*[\(\.-]?(\d{3})[\)\.-]?\s*(\d{3})[\.-]?(\d{4})$/;
    def tableColumnNames = [:];
    def saveButton = new Button(SAVE_DB_BUTTON_CAPTION);
    
    ClientService clientService;
    
    TableBuilder(Layout layout) {
        this.layout = layout;        
        layout.addComponent(messageLabel);
    }
        
    def tableValueChangeListener = new SelectionListener() {
        public void select(SelectionEvent event) {
            def selected = table.getSelectionModel().getSelectedRow();
            if (isMessageShown) {
                if (System.currentTimeMillis() - startTime 
                    > MESSAGE_VISIBILITY_INTERVAL) {
                    isMessageShown = false;
                    messageLabel.setValue('') ;
                }
            }    
        }
    }
    
    def createTable(def fields) {
        tableColumnNames = fields;
        table = new Grid();
        layout.addComponent(table);
        layout.addComponent(saveButton);
        
        // Send changes in selection immediately to server.
        table.setImmediate(true);
        // Handle selection change.
        table.addSelectionListener(tableValueChangeListener);
        table.setWidth("70%");
        table.setEditorEnabled(true);
        table.setEditorSaveCaption(EDITOR_SAVE_BUTTON_CAPTION);
        
        fields.each{ def field ->       
            table.addColumn(field.value, String.class);     
            if (field.key.toLowerCase() == FIRST_NAME
                || field.key.toLowerCase() == LAST_NAME
                || field.key.toLowerCase() == MIDDLE_NAME) {
                table.getColumn(field.value).setEditable(false);
            }
        }
        fillTableFromDB();
        addPhoneValidator(fields[PHONE_NUMBER], fields[PHONE_TYPE]);
        saveButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                persistContacts();
            }
        });
    }
    
    def fillTableFromDB() {
        clientList = Grails.get(ClientService).getClients();
        println 'found ' + clientList?.size + ' client(s)';
        clientList.each { def client ->
            client?.contacts?.each { def contact ->
                table.addRow(client.firstName, client.middleName, 
                             client.lastName, contact?.phoneNumber?:"", 
                             contact?.phoneType?:"", contact?.comment?:""); 
            }
        }
    }
    
    def persistContacts() {
        def pos = 0;
        clientList?.each {def client ->
            client?.contacts?.each { def contact ->
                def row = table.getContainerDataSource().getItem(++pos);
                def phoneNum = row?.getItemProperty(tableColumnNames[PHONE_NUMBER]);
                def phoneType = row?.getItemProperty(tableColumnNames[PHONE_TYPE]);
                if (!phoneNum) {
                    return;
                }
                def isContactChanged = false;
                if (!contact.phoneNumber.equals(phoneNum.toString())) {
                    isContactChanged = true;
                    contact.phoneNumber = phoneNum;
                }
                if (!contact.phoneType.equals(phoneType.toString())) {
                    isContactChanged = true;
                    contact.phoneType = phoneType;
                }
                def comment = row.getItemProperty(tableColumnNames[PHONE_COMMENT]);
                if (!contact.comment.equals(comment.toString())) {
                    isContactChanged = true;
                    contact.comment = comment;
                }
                if (isContactChanged) {
                    Grails.get(ClientService).saveContact(contact);
                    messageLabel.setValue(PERSIST_MESSAGE);
                    startTime = System.currentTimeMillis();
                }
            }
        }
    }
    
    def validatePhoneNumber(def value) 
            throws InvalidValueException {
        def row = table.getContainerDataSource()
            .getItem(table.getEditedItemId());
        def phoneType = row?.getItemProperty(tableColumnNames[PHONE_TYPE]);
        if (phoneType?.toString()?.toLowerCase()?.contains(WORK_PHONE_TYPE)) {
            return;
        }
        if (!(value instanceof String 
                && (value ==~ phoneValidation))) {
            throw new InvalidValueException(VALIDATION_MESSAGE);
        }
        
    }
    
    def addPhoneValidator(def fieldToValidate, def phoneTypeField) {
        def phoneValidator = new Validator() {
            public void validate(def value) throws InvalidValueException {
                validatePhoneNumber(value);
            }
        }
        def phoneFieldValid = new TextField();
        phoneFieldValid.addValidator(phoneValidator);
        table.setEditorField(fieldToValidate, phoneFieldValid);
    }
}

