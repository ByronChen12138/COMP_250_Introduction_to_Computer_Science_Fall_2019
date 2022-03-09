# COMP 250 Project 2 (Fall 2019)

## Description
In this project, we will work with arithmetic operations on large positive integers. Although Java has its own BigInteger class, 
we are going to build our own classes, namely MyBigInteger.
The MyBigInteger class has two fields: base and coefficients. The base field is an int with
values in {2, 3, …, 10}. We could have allowed for larger bases but that would have required
using special symbols for the numbers greater than 1. In this project, we focus on developing three functions, they are:
- **dividedBy** (part division)
- **convert** (base conversion)
- **primeFactors**

## Project Structure

```console
.
├── Project Description.pdf
├── README.md
└── Codes
    ├── MyBigInteger.java
    ├── NewTester.java
    └── TesterPosted.java
```
