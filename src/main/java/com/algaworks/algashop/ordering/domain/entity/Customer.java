package com.algaworks.algashop.ordering.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;


public class Customer {

     private UUID id;
     private String fullNAme;
     private LocalDate birthdate;
     private String email;
     private String phone;
     private String document;
     private Boolean promotionNotificationAllowed;
     private Boolean archived;
     private OffsetDateTime registeredAt;
     private OffsetDateTime archivedAt;
     private Integer loyaltyPointd;

    public Customer(UUID id, String document, String phone, String email,
                    String fullNAme, LocalDate birthdate,
                    Boolean promotionNotificationAllowed,
                    OffsetDateTime registeredAt) {
        this.id = id;
        this.document = document;
        this.phone = phone;
        this.email = email;
        this.fullNAme = fullNAme;
        this.birthdate = birthdate;
        this.promotionNotificationAllowed = promotionNotificationAllowed;
        this.registeredAt = registeredAt;
    }

    private void setId(UUID id) {
        this.id = id;
    }

    private void setFullNAme(String fullNAme) {
        this.fullNAme = fullNAme;
    }

    private void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    private void setPhone(String phone) {
        this.phone = phone;
    }

    private void setDocument(String document) {
        this.document = document;
    }

    private void setPromotionNotificationAllowed(Boolean promotionNotificationAllowed) {
        this.promotionNotificationAllowed = promotionNotificationAllowed;
    }

    private void setArchived(Boolean archived) {
        this.archived = archived;
    }

    private void setRegisteredAt(OffsetDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    private void setArchivedAt(OffsetDateTime archivedAt) {
        this.archivedAt = archivedAt;
    }

    private void setLoyaltyPointd(Integer loyaltyPointd) {
        this.loyaltyPointd = loyaltyPointd;
    }

    public Customer(UUID id, String fullNAme, LocalDate birthdate, String email,
                    String phone, String document, Boolean promotionNotificationAllowed,
                    Boolean archived, OffsetDateTime registeredAt, OffsetDateTime archivedAt,
                    Integer loyaltyPointd) {
        this.id = id;
        this.fullNAme = fullNAme;
        this.birthdate = birthdate;
        this.email = email;
        this.phone = phone;
        this.document = document;
        this.promotionNotificationAllowed = promotionNotificationAllowed;
        this.archived = archived;
        this.registeredAt = registeredAt;
        this.archivedAt = archivedAt;
        this.loyaltyPointd = loyaltyPointd;
    }

        public void addLoyaltyPoints(Integer points){

        }

        public void archive(){

        }

        public void enablePromotionNotifications(){
             this.setPromotionNotificationAllowed(true);
        }

        public void disablePromotionNotifications(){
            this.setPromotionNotificationAllowed(false);
        }

        public void changeName(String fullName){
             this.setFullNAme(fullName);
        }

        public void changeEmail(String email){
            this.setEmail(email);
        }

        public void changePhone(String phone){
            this.setPhone(phone);
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
