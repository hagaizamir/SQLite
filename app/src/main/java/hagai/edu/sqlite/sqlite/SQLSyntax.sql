-- Relational databases
-- SQL is Not case sensitive


 -- SELECT
SELECT col1Name, col2Name, col3Name, col4Name FROM Customers;

-- * = All Columns.
SELECT * FROM Customers;

SELECT * FROM Products
WHERE CategoryId = 1

SELECT * FROM Products
WHERE ProductName = 'Chais'


-- NOT
SELECT * FROM Products
WHERE CategoryId <> 1

SELECT * FROM Products
WHERE CategoryId != 1


SELECT *
FROM Products
WHERE ProductName LIKE '%ch%'


SELECT *
FROM Products
WHERE ProductName LIKE '_on%'

SELECT *
FROM Products
WHERE ProductName LIKE 'ch___'

-- ORDER BY

SELECT *
FROM Products
WHERE Price > 30
ORDER BY Price -- ASC

SELECT *
FROM Products
WHERE Price > 30
ORDER BY Price ASC -- DESC


SELECT *
FROM Products
WHERE Price > 30
ORDER BY Price ASC, ProductName DESC


SELECT ProductName, CategoryName
FROM Products
JOIN Categories
ON Products.CategoryID = Categories.CategoryID
ORDER BY CategoryName, ProductName

SELECT CustomerName
FROM Customers JOIN Orders
ON Customers.CustomerID = Orders.CustomerID
JOIN OrderDetails ON Orders.OrderID = OrderDetails.OrderID
JOIN Products ON Products.ProductID = OrderDetails.ProductID
WHERE ProductName = 'Chais'

SELECT *
FROM Customers
LEFT JOIN Orders
ON Customers.CustomerID = Orders.CustomerID
WHERE Orders.CustomerID IS NULL

CREATE TABLE Students(
	studentID INTEGER,
	firstName TEXT,
    lastName TEXT);

CREATE TABLE Phones(phoneID INTEGER,
					studentID INTEGER,
                    phone TEXT);

-- Many to many:
CREATE TABLE Students(
	studentID INTEGER,
	firstName TEXT,
    lastName TEXT);

CREATE TABLE Courses(
	courseID INTEGER,
	courseName TEXT);

--Many to many
CREATE TABLE StudentCourses(
	studentCoursesID INTEGER,
	studentID INTEGER,
    courseID INTEGER
);

--INSERT
INSERT INTO Students(studentID, firstName, lastName)
VALUES(1, 'Moshe', 'Doe')


-- AUTO_INCREMENT
CREATE TABLE Students(
	studentID INTEGER PRIMARY KEY AUTO_INCREMENT,
	firstName TEXT,
    lastName TEXT);

INSERT INTO Students(firstName, lastName)
VALUES('Moshe', 'Doe');


--DROP TABLE
DROP TABLE Students

--DELETE ROWS FROM A TABLE
DELETE FROM Customers -- dangarous don't forget the WHERE

DELETE FROM PRODUCTS
WHERE ProductID < 4

DELETE FROM Products -- dangarous
WHERE ProductName = 'Chais'


UPDATE Products
SET ProductName = 'Chocho'
WHERE ProductName = 'Chais'