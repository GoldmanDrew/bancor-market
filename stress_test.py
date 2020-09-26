from random import randint, random
import mysql.connector

def initialize_connection():
	# Connect to MySQL
	db = mysql.connector.connect(
		host = "localhost",
		user = "root",
		password = "#Lacrosse0_LINUX",
		database = "unibursal",
		auth_plugin = "mysql_native_password"
	)

	return db

def initialize_db(db):

	cursor = db.cursor()

	# Insert the "Cash" token
	insert_cash_token = "INSERT INTO tokens (Name, Ticker, Price, TokenSupply, CashSupply, TimeUpdated) VALUES (\"Cash\", \"Cash\", 1.0, 1.0, 1.0, \"2020-01-01 00:00:00\")"
	cursor.execute(insert_cash_token)
	db.commit()

	# Create a random number of users with a random cash amount
	num_users = 80
	users = []
	for i in range(0, num_users):
		users.append((
			f"User_{i}",
			"Cash",
			randint(1, 1000)
		))

	insert_users_statement = "INSERT INTO UserShares (User, Token, Quantity) VALUES (%s, %s, %s)"
	cursor.executemany(insert_users_statement, users)
	db.commit()

	# Create a random number of actual tokens
	num_tokens = randint(1, 20)
	tokens = []
	for i in range(0, num_tokens):
		tokens.append((
			f"token_{i}", 
			f"TK{i}",
			100,
			100,
			1.0,
			"2020-01-01 00:00:00"
		))

	insert_tokens_statement = "INSERT INTO tokens (Name, Ticker, TokenSupply, CashSupply, Price, TimeUpdated) VALUES (%s, %s, %s, %s, %s, %s)"
	cursor.executemany(insert_tokens_statement, tokens)
	db.commit()

	return num_users, num_tokens

def initialize_orders(db, num_users, num_tokens):
	# Place a random number of orders into MySQL
	cursor = db.cursor()

	num_trasactions = randint(1, 100)

	orders = []
	for i in range(0, num_trasactions):
		user = randint(0, num_users - 1)
		token = randint(0, num_tokens - 1)

		# Buy
		if random() < 0.5:
			orders.append((
				f"User_{user}", 
				"Cash",
				f"token_{i}", 
				random() * 20, 
				"N"
			))

		# Sell
		else:
			orders.append((
				f"User_{user}", 
				f"token_{i}", 
				"Cash",
				random() * 20, 
				"N"
			))

	insert_orders_statement = "INSERT INTO orders (User, SourceToken, TargetToken, SourceQuantity, Filled) VALUES (%s, %s, %s, %s, %s)"
	cursor.executemany(insert_orders_statement, orders)
	db.commit()

	


	# Go through each user and have them incrementally cash out
def simulate(db):
	pass

# Check cash is remaining
def validate():
	return True


if __name__ == "__main__":
	db = initialize_connection()
	num_users, num_tokens = initialize_db(db)
	initialize_orders(db, num_users, num_tokens)
	# simulate(db)
	# validate(db)
