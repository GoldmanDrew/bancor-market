USE vase2

INSERT INTO tokens (Name, Ticker, Supply) VALUES ("a", "a", 10.0);
INSERT INTO tokens (Name, Ticker) VALUES ("b", "b");
INSERT INTO tokens (Name, Ticker) VALUES ("connector", "s");

INSERT INTO orders (User, SellToken, BuyToken, Quantity) VALUES ("foo", "a", "b", 10.0);
INSERT INTO orders (User, SellToken, BuyToken, Quantity) VALUES ("foo", "a", "b", 10.0);
INSERT INTO orders (User, SellToken, BuyToken, Quantity) VALUES ("foo", "b", "connector", 10.0);
INSERT INTO orders (User, SellToken, BuyToken, Quantity) VALUES ("foo", "a", "connector", 10.0);
