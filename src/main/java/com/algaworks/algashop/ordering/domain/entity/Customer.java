package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.validator.FieldValidations;
import com.algaworks.algashop.ordering.domain.exceptions.CustomerArchivedException;
import com.algaworks.algashop.ordering.domain.valueobject.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.valueobject.LoyaltyPoints;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.algaworks.algashop.ordering.domain.exceptions.ErrorMessages.*;


public class Customer {

     private CustomerId id;
     private FullName fullNAme;
     private LocalDate birthdate;
     private String email;
     private String phone;
     private String document;
     private Boolean promotionNotificationAllowed;
     private Boolean archived;
     private OffsetDateTime registeredAt;
     private OffsetDateTime archivedAt;
     private LoyaltyPoints loyaltyPointd;

    public Customer(CustomerId id, String document, String phone, String email,
                    FullName fullNAme, LocalDate birthdate,
                    Boolean promotionNotificationAllowed,
                    OffsetDateTime registeredAt) {
        this.setId(id);
        this.setDocument(document);
        this.setPhone(phone);
        this.setEmail(email);
        this.setFullNAme(fullNAme);
        this.setBirthdate(birthdate);
        this.setPromotionNotificationAllowed(promotionNotificationAllowed);
        this.setRegisteredAt(registeredAt);
        this.setArchived(false);
        this.setLoyaltyPointd(LoyaltyPoints.ZERO);
    }

    private void setId(CustomerId id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    private void setFullNAme(FullName fullNAme) {
        Objects.requireNonNull(fullNAme,VALIDATION_ERROR_FULLNAME_IS_NULL);

        this.fullNAme = fullNAme;
    }

    private void setBirthdate(LocalDate birthdate) {

        if(birthdate == null){
            this.birthdate = null;
            return;
        }
        if (birthdate.isAfter(LocalDate.now())){
            throw new IllegalArgumentException(VALIDATION_ERROR_BIRTHDATE_MUST_IN_PAST);
        }
        this.birthdate = birthdate;
    }

    private void setEmail(String email) {
        FieldValidations.requiresValidEmail(email,VALIDATION_ERROR_EMAIL_IS_INVALID);
        this.email = email;
    }

    private void setPhone(String phone) {
        Objects.requireNonNull(phone);
        this.phone = phone;
    }

    private void setDocument(String document) {
        Objects.requireNonNull(document);
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

    public Customer(CustomerId id, FullName fullNAme, LocalDate birthdate, String email,
                    String phone, String document, Boolean promotionNotificationAllowed,
                    Boolean archived, OffsetDateTime registeredAt, OffsetDateTime archivedAt,
                    LoyaltyPoints loyaltyPointd) {
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
    }

    public CustomerId id() {
        return id;
    }

    public FullName fullNAme() {
        return fullNAme;
    }

    public LocalDate birthdate() {
        return birthdate;
    }

    public String email() {
        return email;
    }

    public String phone() {
        return phone;
    }

    public String document() {
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

        public void archive(){

            verifyIfChangeable();
            this.setArchived(true);
         this.setFullNAme(new FullName("Anonymous","Anonymous"));
         this.setPhone("000-000-0000");
         this.setDocument("000-00-0000");
         this.setEmail(UUID.randomUUID()+"@anonymous.com");
         this.setBirthdate(null);
         this.setPromotionNotificationAllowed(false);
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

        public void changeEmail(String email){
            verifyIfChangeable();
            this.setEmail(email);
        }

        public void changePhone(String phone){
            verifyIfChangeable();
            this.setPhone(phone);
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
