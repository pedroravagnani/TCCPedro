package com.pedrofonseca.tcc.data;

public class User {

    private String name,email,password;
    private double cpf;

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        Double cpfDouble;
        cpf = cpf.replaceAll("[\\s\\-()]", "");
        cpfDouble = Double.parseDouble(cpf);
        this.cpf = cpfDouble;
    }
}
