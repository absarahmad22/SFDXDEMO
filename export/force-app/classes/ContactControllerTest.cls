@isTest
public class ContactControllerTest {
    @isTest static void test_one(){
        Account acc = new Account(Name='Berlington Textile');
        Insert acc;
        Contact con = new Contact(LastName='Joun Doe',AccountId =acc.Id);
        Insert con;
        
        ContactController.getContactList();
        
    }
}