package com.rianbowsea.springcloud.test;

import java.util.function.Function;

public class T1 {

    public static void main(String[] args) {
        Dog dog = hi("小花猫", (String str) -> {
            Cat cat = new Cat();
            cat.setName(str);
            return cat;
        });

        System.out.println(dog);
    }

    // 模拟把 Cat -> Dog
    public static Dog hi(String str, Function<String, Cat> fn) {
        Cat cat = fn.apply(str);
        Dog dog = new Dog();
        dog.setName(cat.getName() + "~变成了小狗名");
        return dog;
    }

    static class Cat {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Cat{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    static class Dog {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Dog{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
