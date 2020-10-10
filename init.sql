CREATE DATABASE IF NOT EXISTS unibursal;

USE unibursal;

CREATE TABLE tokens (
	Name varchar(255) PRIMARY KEY,
	Ticker varchar(4), 
	TokenSupply DOUBLE, 
	CashSupply DOUBLE,
	Price DOUBLE,
	TimeUpdated DATETIME
);

CREATE TABLE UserShares (
	ID int AUTO_INCREMENT,
	-- Will need to reference the Django table with users
	User varchar(255),
	Token varchar(255),
	Quantity DOUBLE, 
	PRIMARY KEY (ID), 
	UNIQUE (User, Token),
	FOREIGN KEY (TOKEN) REFERENCES tokens(Name)
);

CREATE TABLE orders (
	OrderID int AUTO_INCREMENT, 
	-- Will need to reference the Django table with users
	User varchar(255), 
	SourceToken varchar(255), 
	TargetToken varchar(255), 
	SourceQuantity DOUBLE,
	TargetQuantity DOUBLE,
	Filled varchar(1) NOT NULL,
	OrderTime DATETIME DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (OrderID),
	FOREIGN KEY (SourceToken) REFERENCES tokens(Name),	
	FOREIGN KEY (TargetToken) REFERENCES tokens(Name),
	CONSTRAINT checkQuantity CHECK ((SourceQuantity IS NOT NULL OR TargetQuantity IS NOT NULL) AND (SourceQuantity IS NULL OR TargetQuantity IS NULL))
);

CREATE TABLE tokenHistory (
	ID int AUTO_INCREMENT,
	Token varchar(255), 
	Time DATETIME, 
	Price DOUBLE,
	PRIMARY KEY (ID), 
	FOREIGN KEY (TOKEN) REFERENCES tokens(Name)
);

INSERT INTO tokens (Name, Ticker, Price) VALUES ("Cash", "Cash", 1.0);
INSERT INTO tokens (Name, Ticker, TokenSupply, CashSupply) VALUES ("A", "A", 100.0, 100.0);
INSERT INTO tokens (Name, Ticker, TokenSupply, CashSupply) VALUES ("B", "B", 100.0, 100.0);
