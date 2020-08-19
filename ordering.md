Ordering Options
=================
**Want ordering to be simple as possible with no financial knowledge needed**

Buy a specific quantity of token with cash
-------------------------------------------
"Buy 10 tokens of A"

* Estimate price as price of A * 10
* Fill parital orders
* Give disclaimer
* Put into DB Order(sourceToken = cash, targetToken = A, targetQuantiy = 10)


Buy a dollar amount of token with cash
----------------------------------------
"Buy $10 of token A"

* Estimate tokens recieved as $10 / price of A
* Give disclaimer
* Put into DB Order(sourceToken = cash, targetToken = A, sourceQuantity = 10)


Buy a specific quantity of token with another token
-----------------------------------------------------
"Buy 10 tokens of A using B"

* Estimate transaction as 
	* cash needed = 10 * cost A
	* tokens B needed = cash needed / price of B
* Fill parital orders
* Give disclaimer
* Put into DB Order(sourceToken = B, targetToken = A, targetQuantity = 10)


Buy a amount of a token by selling a specific quantity of another token
------------------------------------------------------------------------
"Exchange 10 tokens of B for A"

* Estimate tokens of A as
	* cash = 10 * price of B
	* tokens A = cash / price of A

* Give disclaimer
* Put into DB Order(sourceToken = B, targetToken = A, sourceQuantity = 10)


Sell a specific amount of a token
--------------------------------------
"Sell 10 tokens of A"

* Estimate cash recieved as 10 * price of A
* Give discliamer
* Put into DB Order(sourceToken = A, targetToken = Cash, sourceQuantity = 10)


Other Ideas
--------------------------------------
* Fill parital order ?
* Fill all or nothing ? (Make user have estimated * constant?)
* Sell cash amount (seems pointless and just overly complicated for the user to me)
* Server side checking for sourceQuantity specified e.g. Buy $10 of token A) and backend checking for the rest
