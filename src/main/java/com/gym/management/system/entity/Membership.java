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

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipStatus status; // ACTIVE, EXPIRED, UPCOMING

    @OneToOne
    @JoinColumn(name = "member_id", unique = true)
    @JsonIgnore
    private Members member;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private MembershipPlan plan;

    public Membership() {
    }

    public Long getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Long membershipId) {
        this.membershipId = membershipId;
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

    public void setEndDate(LocalDate endDate) {this.endDate = endDate;}

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

    public MembershipPlan getPlan() { return plan; }

    public void setPlan(MembershipPlan plan) { this.plan = plan; }
}
