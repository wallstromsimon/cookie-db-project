set foreign_key_checks = 0;
drop table if exists Ingredient;
drop table if exists RecipeItem;
drop table if exists Cookie;
drop table if exists Pallet;
drop table if exists Orders;
drop table if exists DeliverdItem;
drop table if exists Customer;
drop table if exists OrderedItem;


CREATE TABLE Ingredient(
    IngredientName  varchar(30),
    AmountLeft      int not null,
    primary key (IngredientName)
    );

CREATE TABLE RecipeItem(
    CookieName      varchar(30) not null,
    IngredientName  varchar(30) not null,
    Amount          int not null,
    primary key (CookieName, IngredientName),
    foreign key (CookieName) references Cookie(CookieName),
    foreign key (IngredientName) references Ingredient(IngredientName),
    constraint uniqueNames unique (CookieName, IngredientName)
    );

CREATE TABLE Cookie(
    CookieName  varchar(30),
    primary key (CookieName)
    );

CREATE TABLE DeliverdItem(
    PalletID        int,
    OrderID         int,
    CustomerName    varchar(30),
    DeliveryDate    date,
    primary key (PalletID, OrderID),
    foreign key (PalletID) references Pallet(PalletID),
    foreign key (OrderID) references Orders(OrderID)
    );

CREATE TABLE Pallet(
    PalletID    int AUTO_INCREMENT,
    CookieName  varchar(30) not null,
    DateMade    date,
    Blocked     Boolean,
    primary key (PalletID),
    foreign key (CookieName) references Cookie(CookieName)
    );

CREATE TABLE Customer(
    UserName    varchar(30),
    Address     varchar(30) not null,
    primary key (UserName)
    );

CREATE TABLE Orders(
    OrderID         int AUTO_INCREMENT,
    Customer        varchar(30),
    DeliveryDate    date,
    primary key (OrderID),
    foreign key (Customer) references Customer(UserName)
    );

CREATE TABLE OrderedItem(
    OrderID         int,
    CookieName      varchar(30),
    NbrPallets      int not null,
    primary key (OrderID, CookieName),
    foreign key (OrderID) references Orders(OrderID)
    );    
