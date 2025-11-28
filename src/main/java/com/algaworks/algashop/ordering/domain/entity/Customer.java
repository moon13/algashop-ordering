package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.validator.FieldValidations;
import com.algaworks.algashop.ordering.domain.exceptions.CustomerArchivedException;
import com.algaworks.algashop.ordering.domain.valueobject.*;
import lombok.Builder;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.algaworks.algashop.ordering.domain.exceptions.ErrorMessages.*;


public class Customer {

     private CustomerId id;
     private FullName fullNAme;
     private Birthdate birthdate;
     private Email email;
     private Phone phone;
     private Document document;
     private Boolean promotionNotificationAllowed;
     private Boolean archived;
     private OffsetDateTime registeredAt;
     private OffsetDateTime archivedAt;
     private LoyaltyPoints loyaltyPointd;
     private Address address;

     @Builder(builderClassName = "BrandNewCustomerBuild", builderMethodName = "brandNew")
     private static Customer createBrandNew(Document document, Phone phone, Email email,
                                     FullName fullNAme, Birthdate birthdate,
                                     Boolean promotionNotificationAllowed,
                                     Address address){

         return new Customer(new CustomerId(),
                            fullNAme,
                            birthdate,
                            email,
                            phone,
                            document,
                            promotionNotificationAllowed,
                            false,
                            OffsetDateTime.now(),
                             null,
                            LoyaltyPoints.ZERO,
                            address
         );

     }


//    public Customer(CustomerId id, Document document, Phone phone, Email email,
//                    FullName fullNAme, Birthdate birthdate,
//                    Boolean promotionNotificationAllowed,
//                    OffsetDateTime registeredAt,
//                    Address address) {
//        this.setId(id);
//        this.setDocument(document);
//        this.setPhone(phone);
//        this.setEmail(email);
//        this.setFullNAme(fullNAme);
//        this.setBirthdate(birthdate);
//        this.setPromotionNotificationAllowed(promotionNotificationAllowed);
//        this.setRegisteredAt(registeredAt);
//        this.setArchived(false);
//        this.setLoyaltyPointd(LoyaltyPoints.ZERO);
//        this.setAddress(address);
//    }

    private void setId(CustomerId id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    private void setFullNAme(FullName fullNAme) {
        Objects.requireNonNull(fullNAme,VALIDATION_ERROR_FULLNAME_IS_NULL);

        this.fullNAme = fullNAme;
    }

    private void setBirthdate(Birthdate birthdate) {

        if(birthdate == null){
            this.birthdate = null;
            return;
        }
//        if (birthdate.isAfter(LocalDate.now())){
//            throw new IllegalArgumentException(VALIDATION_ERROR_BIRTHDATE_MUST_IN_PAST);
//        }
        this.birthdate = birthdate;
    }

    private void setEmail(Email email) {
       // FieldValidations.requiresValidEmail(email,VALIDATION_ERROR_EMAIL_IS_INVALID);
        this.email = email;
    }

    private void setPhone(Phone phone) {
        //Objects.requireNonNull(phone);
        this.phone = phone;
    }

    private void setDocument(Document document) {
        //Objects.requireNonNull(document);
        this.document = document;
    }

    private void setPromotionNotificationAllowed(Boolean promotionNotificationAllowed) {
        Objects.requireNonNull(promotionNotificationAllowed);
        this.promotionNotificationAllowed = promotionNotificationAllowed;
    }

    private void setArchived(Boolean archived) {
        Objects.requireNonNull(archived);
        this.archived = archived;
    }

    private void setRegisteredAt(OffsetDateTime registeredAt) {
        Objects.requireNonNull(registeredAt);
        this.registeredAt = registeredAt;
    }

    private void setArchivedAt(OffsetDateTime archivedAt) {
        this.archivedAt = archivedAt;
    }

    private void setLoyaltyPointd(LoyaltyPoints loyaltyPointd) {
        Objects.requireNonNull(loyaltyPointd);

        this.loyaltyPointd = loyaltyPointd;
    }

    private void setAddress(Address address) {
        Objects.requireNonNull(address);
        this.address = address;
    }

    @Builder(builderClassName = "ExistingCustomerBuild", builderMethodName = "existing")
    private static Customer createExisting(CustomerId id, FullName fullNAme, Birthdate birthdate, Email email,
                                    Phone phone, Document document, Boolean promotionNotificationAllowed,
                                    Boolean archived, OffsetDateTime registeredAt, OffsetDateTime archivedAt,
                                    LoyaltyPoints loyaltyPointd,
                                    Address address){
            return new Customer(
                    id,
                    fullNAme,
                    birthdate,
                    email,
                    phone,
                     document,
                    promotionNotificationAllowed,
                    archived,
                    registeredAt,
                     archivedAt,
                    loyaltyPointd,
                    address

            );
    }

    private Customer(CustomerId id, FullName fullNAme, Birthdate birthdate, Email email,
                    Phone phone, Document document, Boolean promotionNotificationAllowed,
                    Boolean archived, OffsetDateTime registeredAt, OffsetDateTime archivedAt,
                    LoyaltyPoints loyaltyPointd,
                    Address address) {
        this.setId(id);
        this.setFullNAme(fullNAme);
        this.setBirthdate(birthdate);
        this.setEmail(email);
        this.setPhone(phone);
        this.setDocument(document);
        this.setPromotionNotificationAllowed(promotionNotificationAllowed);
        this.setArchived(archived);
        this.setRegisteredAt(registeredAt);
        this.setArchivedAt(archivedAt);
        this.setLoyaltyPointd(loyaltyPointd);
        this.setAddress(address);
    }

    public CustomerId id() {
        return id;
    }

    public FullName fullNAme() {
        return fullNAme;
    }

    public Birthdate birthdate() {
        return birthdate;
    }

    public Email email() {
        return email;
    }

    public Phone phone() {
        return phone;
    }

    public Document document() {
        return document;
    }

    public Boolean isPromotionNotificationAllowed() {
        return promotionNotificationAllowed;
    }

    public Boolean isArchived() {
        return archived;
    }

    public OffsetDateTime registeredAt() {
        return registeredAt;
    }

    public OffsetDateTime archivedAt() {
        return archivedAt;
    }

    public LoyaltyPoints loyaltyPointd() {
        return loyaltyPointd;
    }

    public void addLoyaltyPoints(LoyaltyPoints loyaltyPointsAdded){
        verifyIfChangeable();

        this.setLoyaltyPointd(loyaltyPointd.add(loyaltyPointsAdded));
    }

    public Address address() {
        return address;
    }

    public void archive(){

            verifyIfChangeable();
            this.setArchived(true);
         this.setFullNAme(new FullName("Anonymous","Anonymous"));
         this.setPhone(new Phone("000-000-0000"));
         this.setDocument(new Document("000-00-0000"));
         this.setEmail(new Email(UUID.randomUUID()+"@anonymous.com"));
         this.setBirthdate(null);
         this.setPromotionNotificationAllowed(false);
         this.setAddress(this.address().toBuilder()
                 .number("Anonymized").complement(null).build());
        }



    public void enablePromotionNotifications(){
             verifyIfChangeable();
             this.setPromotionNotificationAllowed(true);
        }

        public void disablePromotionNotifications(){
            verifyIfChangeable();
            this.setPromotionNotificationAllowed(false);
        }

        public void changeName(FullName fullName){
            verifyIfChangeable();
             this.setFullNAme(fullName);
        }

        public void changeEmail(Email email){
            verifyIfChangeable();
            this.setEmail(email);
        }

        public void changePhone(Phone phone){
            verifyIfChangeable();
            this.setPhone(phone);
        }

        public void changeAddress(Address address){
            verifyIfChangeable();
            this.setAddress(address);
        }

    private void verifyIfChangeable() {
        if ( this.isArchived()){
            throw new CustomerArchivedException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
