CREATE DATABASE IF NOT EXISTS vase2;

USE vase2;

CREATE TABLE orders (
	User varchar(255), 
	SellToken varchar(255), 
	BuyToken varchar(255), 
	Quantity DOUBLE
)
