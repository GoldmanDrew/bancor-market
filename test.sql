USE vase2

INSERT INTO tokens (Name, Ticker, Supply) VALUES ("a", "a", 10.0);
INSERT INTO tokens (Name, Ticker) VALUES ("b", "b");
INSERT INTO tokens (Name, Ticker) VALUES ("connector", "s");

INSERT INTO orders (User, SourceToken, TargetToken, Quantity, Filled) VALUES ("foo", "connector", "b", 10.0, "N");
INSERT INTO orders (User, SourceToken, TargetToken, Quantity, Filled) VALUES ("foo", "b", "connector", 4.98, "N");
