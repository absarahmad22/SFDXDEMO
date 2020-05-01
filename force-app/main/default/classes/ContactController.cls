public with sharing class ContactController {

    @AuraEnabled(cacheable=true)
    public static List<Contact> getContactList() {
        return [SELECT Id, Name, Title, Phone,Mobile, Email,Account.Name,Level,Language121 FROM Contact  LIMIT 15];
    }

}