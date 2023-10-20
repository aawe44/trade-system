package com.jason.trade.goods.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private String id;
    private String name;
    private String address;
    private int age;

    @Override
    public String toString() {
        return "Person{"
                + "id='" + id + '\''
                + ", name='" + name + '\''
                + ", address='" + address + '\''
                + '}';
    }

}
