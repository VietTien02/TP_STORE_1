package com.pts.entity;

import java.util.ArrayList;
import java.util.List;

public class AccountPage {
    private List<Account> accounts;
    private List<Authority> authorities;
    private List<Category> categories;
    private List<Course> courses;
    private List<Chapter>chapters;
    private List<Content>contents;
    private List<CourseCode>courseCodes;
    private List<Payment>paymentList;
    private List<Quiz>quizList;
    private List<Order>orderList;

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public List<Quiz> getQuizList() {
        return quizList;
    }

    public void setQuizList(List<Quiz> quizList) {
        this.quizList = quizList;
    }

    public AccountPage() {
        System.out.println("\n");
        paymentList = new ArrayList<>();
    }
    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public List<CourseCode> getCourseCodes() {
        return courseCodes;
    }

    public void setCourseCodes(List<CourseCode> courseCodes) {
        this.courseCodes = courseCodes;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    private int totalPages;
    private int currentPage;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
