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
	Token_id varchar(255),
	Quantity DOUBLE,
	PRIMARY KEY (ID),
	UNIQUE (User, Token_id),
	FOREIGN KEY (Token_id) REFERENCES tokens(Name)
);

CREATE TABLE orders (
	OrderID int AUTO_INCREMENT,
	-- Will need to reference the Django table with users
	User varchar(255),
	SourceToken_id varchar(255),
	TargetToken_id varchar(255),
	SourceQuantity DOUBLE,
	TargetQuantity DOUBLE,
	Filled varchar(1) NOT NULL,
	OrderTime DATETIME,
	PRIMARY KEY (OrderID),
	FOREIGN KEY (SourceToken_id) REFERENCES tokens(Name),
	FOREIGN KEY (TargetToken_id) REFERENCES tokens(Name),
	CONSTRAINT checkQuantity CHECK ((SourceQuantity IS NOT NULL OR TargetQuantity IS NOT NULL) AND (SourceQuantity IS NULL OR TargetQuantityt IS NULL))
);

CREATE TABLE tokenHistory (
	ID int AUTO_INCREMENT,
	Token_id varchar(255),
	Time DATETIME,
	Price DOUBLE,
	PRIMARY KEY (ID),
	FOREIGN KEY (Token_id) REFERENCES tokens(Name)
);

INSERT INTO tokens (Name, Ticker, Price) VALUES ("Cash", "Cash", 1.0);
INSERT INTO tokens (Name, Ticker, TokenSupply, CashSupply) VALUES ("A", "A", 100.0, 100.0);
INSERT INTO tokens (Name, Ticker, TokenSupply, CashSupply) VALUES ("B", "B", 100.0, 100.0);
