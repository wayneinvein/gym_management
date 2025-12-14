package com.gym.management.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gym.management.system.enums.MembershipStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@JsonPropertyOrder({"membershipId", "type", "startDate", "endDate", "price", "status"})
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long membershipId;

    private String type;

    private LocalDate startDate;

    private LocalDate endDate;

    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipStatus status; // ACTIVE, EXPIRED, UPCOMING

    @OneToOne
    @JoinColumn(name = "member_id", unique = true)
    @JsonIgnore
    private Members member;

    public Membership() {
    }

    public Long getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Long membershipId) {
        this.membershipId = membershipId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public MembershipStatus getStatus() {return status;}

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }

    public Members getMember() {
        return member;
    }

    public void setMember(Members member) {
        this.member = member;
    }
}
