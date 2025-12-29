package com.gym.management.system.entity;

import jakarta.persistence.*;

@Entity
public class MembershipPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false)
    private int durationDays;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private boolean active = true;

    public MembershipPlan() {
    }

    public MembershipPlan(Long planId, String name, int durationDays, double price, boolean active) {
        this.planId = planId;
        this.name = name;
        this.durationDays = durationDays;
        this.price = price;
        this.active = active;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
