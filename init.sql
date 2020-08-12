CREATE DATABASE IF NOT EXISTS vase2;

USE vase2;

CREATE TABLE tokens (
	Name varchar(255) PRIMARY KEY,
	Ticker varchar(4), 
	Supply DOUBLE, 
	Price DOUBLE
);

CREATE TABLE orders (
	OrderID int AUTO_INCREMENT, 
	User varchar(255), 
	SellToken varchar(255), 
	BuyToken varchar(255), 
	Quantity DOUBLE,
	PRIMARY KEY (OrderID),
	FOREIGN KEY (SellToken) REFERENCES tokens(Name),	
	FOREIGN KEY (BuyToken) REFERENCES tokens(Name)
);
