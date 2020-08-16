CREATE DATABASE IF NOT EXISTS vase2;

USE vase2;

CREATE TABLE tokens (
	Name varchar(255) PRIMARY KEY,
	Ticker varchar(4), 
	Supply DOUBLE, 
	Price DOUBLE
);

CREATE TABLE UserShares (
	ID int AUTO_INCREMENT,
	User varchar(255),
	Token varchar(255),
	Quantity DOUBLE, 
	PRIMARY KEY (ID), 
	UNIQUE (User, Token),
	FOREIGN KEY (TOKEN) REFERENCES tokens(Name)
);

CREATE TABLE orders (
	OrderID int AUTO_INCREMENT, 
	User varchar(255), 
	SourceToken varchar(255), 
	TargetToken varchar(255), 
	Quantity DOUBLE,
	Filled varchar(1) NOT NULL,
	PRIMARY KEY (OrderID),
	FOREIGN KEY (SourceToken) REFERENCES tokens(Name),	
	FOREIGN KEY (TargetToken) REFERENCES tokens(Name)
);
