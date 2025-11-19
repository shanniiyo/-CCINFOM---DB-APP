CREATE TABLE ReturnTransaction (
    ReturnID VARCHAR(50) PRIMARY KEY,     
    ProductID INT NOT NULL,
    ClientID INT NOT NULL,
    StaffID INT NOT NULL,
    Quantity INT NOT NULL,
    Reason VARCHAR(255) NOT NULL,
    PurchaseDate DATE NOT NULL,
    ReturnDate DATE NOT NULL,
    RefundAmount DECIMAL(10,2) NOT NULL,  
    RefundDate DATE NOT NULL,             

    FOREIGN KEY (ProductID) REFERENCES Product(ProductID),
    FOREIGN KEY (ClientID) REFERENCES Client(ClientID),
    FOREIGN KEY (StaffID) REFERENCES Staff(StaffID)
);
