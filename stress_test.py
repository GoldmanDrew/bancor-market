from random import randint, random
import mysql.connector

num_users = 50
num_tokens = 10

def initialize_connection():
	# Connect to MySQL
	db = mysql.connector.connect(
		host = "localhost",
#		user = "root",
#		password = "#Lacrosse0_LINUX",
		user = "som",
		password = "password",
		database = "unibursal",
		auth_plugin = "mysql_native_password"
	)

	return db

def initialize_db(db):

	cursor = db.cursor()

	# Insert the "Cash" token
	insert_cash_token = "INSERT INTO tokens (Name, Ticker, Price, TokenSupply, CashSupply, TimeUpdated) VALUES (\"Cash\", \"Cash\", 1.0, 1.0, 1.0, \"2020-01-01 00:00:00\")"
	try:
		cursor.execute(insert_cash_token)
	except mysql.connector.Error as e:
		if e.errno == 1062:
			print("")
		else: 
			reportError(e)

	db.commit()

	# Create a random number of users with 200 cash each
	# num_users = 50
	users = []
	for i in range(0, num_users):
		try:
			users.append((
				f"User_{i}",
				"Cash",
				200
			))
		except mysql.connector.Error as e:
			if e.errno == 1062:
				print("")
			else: 
				reportError(e)

	insert_users_statement = "INSERT INTO UserShares (User, Token, Quantity) VALUES (%s, %s, %s)"
	try:
		cursor.executemany(insert_users_statement, users)
	except mysql.connector.Error as e:
		if e.errno == 1062:
			print("")
		else: 
			reportError(e)


	db.commit()

	# Create a random number of actual tokens
	# num_tokens = 10 #randint(10, 20)
	tokens = []
	for i in range(0, num_tokens):
		tokens.append((
			f"token_{i}", 
			f"TK{i}",
			100,
			100,
			2.0,
			"2020-01-01 00:00:00"
		))

	insert_tokens_statement = "INSERT INTO tokens (Name, Ticker, TokenSupply, CashSupply, Price, TimeUpdated) VALUES (%s, %s, %s, %s, %s, %s)"
	try:
		cursor.executemany(insert_tokens_statement, tokens)
	except mysql.connector.Error as e:
		if e.errno == 1062:
			print("")
		else: 
			reportError(e)

	db.commit()

	return num_users, num_tokens

def initialize_orders(db, num_users, num_tokens):
	# Place a random number of orders into MySQL
	cursor = db.cursor()

	num_transactions = randint(100,1000)
	for i in range(0, num_transactions):
		user = randint(0, num_users - 1)
		token = randint(0, num_tokens - 1)

		# Buy
		if random() < 0.5:
			orders = []
			orders.append((
				f"User_{user}", 
				"Cash",
				f"token_{token}",
				randint(1, 11), 
				"N"
			))
			insert_orders_statement = "INSERT INTO orders (User, SourceToken, TargetToken, \
				TargetQuantity, Filled) VALUES (%s, %s, %s, %s, %s)"
			cursor.executemany(insert_orders_statement,orders)

		# Sell
		else:
			orders = []
			orders.append((
				f"User_{user}", 
				f"token_{token}", 
				"Cash",
				randint(1, 11),
				"N"
			))
			insert_orders_statement = "INSERT INTO orders (User, SourceToken, TargetToken, SourceQuantity, \
				Filled) VALUES (%s, %s, %s, %s, %s)"
			cursor.executemany(insert_orders_statement, orders)
	
	
	db.commit()

def clearTables(db):
	#Clear tables for testing
	cursor = db.cursor()
	clear_tokens_statement = "DELETE FROM tokens"
	clear_usershares_statement = "DELETE FROM usershares"
	clear_orders_statement = "DELETE FROM orders"
	clear_tokenhistory_statement = "DELETE FROM tokenhistory"
	cursor.execute(clear_orders_statement)
	cursor.execute(clear_tokenhistory_statement)
	cursor.execute(clear_usershares_statement)
	cursor.execute(clear_tokens_statement)
	db.commit()
	

	

	# Go through each user and have them incrementally cash out
def sellall(db, num_users, num_tokens):
	cursor = db.cursor()
	orders = []

	for i in range(0, num_users):
		user = i
		for j in range(0, num_tokens):
			token = j
			# print("User " + str(i) + " Token " + str(j))
			cursor.execute(f"SELECT Quantity FROM usershares WHERE user='User_{user}' AND Token='token_{token}'")
			quantitytosell = ""
			for x in cursor.fetchall():
				quantitytosell = str(x[0])
			if(quantitytosell != ""):
				orders.append((
					f"User_{user}", 
					f"token_{token}", 
					"Cash",
					quantitytosell,
					"N"
				))
	insert_orders_statement = "INSERT INTO orders (User, SourceToken, TargetToken, \
		SourceQuantity, Filled) VALUES (%s, %s, %s, %s, %s)"
	cursor.executemany(insert_orders_statement, orders)
	db.commit()

# Check cash is remaining
def validate(db):
	cursor = db.cursor()
	cursor.execute("SELECT SUM(CashSupply) FROM tokens")
	tokencash = cursor.fetchall()
	for x in tokencash:
		print("Total Token Cash: " + str(x))
	cursor.execute("SELECT SUM(Quantity) FROM usershares WHERE Token='Cash'")
	usercash = cursor.fetchall()
	for x in usercash:
		print("Total User Cash: " + str(x))

def reportError(e):
	print("Error code:" + str(e.errno))        # error number
	print("SQLSTATE value:" + e.sqlstate) # SQLSTATE value
	print ("Error message:" + e.msg + "\n")       # error message
	# print ("Error:" + e + "\n")                   # errno, sqlstate, msg values


if __name__ == "__main__":
	db = initialize_connection()
	# clearTables(db)
	# num_users, num_tokens = initialize_db(db)
	# initialize_orders(db, num_users, num_tokens)
	sellall(db, num_users, num_tokens)
	
	# validate(db)