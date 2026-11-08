# Assignment 6 - Visitor Pattern

## Project Description

This project implements an in-memory hierarchical file system
using the Visitor Design Pattern.

It supports files, directories, and three independent operations.

## Visitors

1. TotalSizeVisitor - calculates the total file size.
2. SearchVisitor - searches files by name and minimum size.
3. StatisticsVisitor - calculates file count, directory count,
   and maximum tree depth.

## Design

The Element interface is implemented by FileNode and DirectoryNode.

The Visitor interface declares two overloaded visit methods.
The accept method provides double dispatch.

AbstractTreeVisitor controls traversal and resets visitor state
before each execution.

## Requirements

- Java 17
- Maven
- JUnit 5

## Run Application

mvn compile exec:java

## Run Tests

mvn test
