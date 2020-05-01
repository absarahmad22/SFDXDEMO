public with sharing class ContactController {

    @AuraEnabled(cacheable=true)
    public static List<Contact> getContactList() {
        return [SELECT Id, Name, Title, Phone,MobilePhone, Email,Account.Name,Level__c,Languages__c FROM Contact  LIMIT 15];
    }

}