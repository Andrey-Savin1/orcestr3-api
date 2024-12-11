package com.example.orcestr3api;


import org.springframework.boot.SpringApplication;

public class TestIndividualsApiApplication {

    public static void main(String[] args) {
        SpringApplication.from(Orcestr3ApiApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}