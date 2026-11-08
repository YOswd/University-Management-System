package com.university.entity;

import jakarta.persistence.*;

@Entity
@Table 
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roll;
    @Column(name = "student_name")
    private String name;
    @Column(name = "student_dept")
    private String dept;
    
    public int getRoll() {
        return roll;
    }
    public void setRoll(int roll) {
        this.roll = roll;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDept() {
        return dept;
    }
    public void setDept(String dept) {
        this.dept = dept;
    }
  
}
