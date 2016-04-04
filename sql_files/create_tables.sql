set foreign_key_checks = 0;
drop table if exists Ingredient;
drop table if exists RecipeItem;
drop table if exists Cookie;
drop table if exists Pallet;
drop table if exists Orders;
drop table if exists DeliverdItem;
drop table if exists Customer;
drop table if exists OrderedItem;


create table Ingredient(
    IngredientName  varchar(30),
    AmountLeft      int not null,
    primary key (IngredientName)
    );

create table RecipeItem(
    CookieName      varchar(30) not null,
    IngredientName  varchar(30) not null,
    Amount          int not null,
    primary key (CookieName, IngredientName),
    foreign key (CookieName) references Cookie(CookieName),
    foreign key (IngredientName) references Ingredient(IngredientName),
    constraint uniqueNames unique (CookieName, IngredientName)
    );

create table Cookie(
    CookieName  varchar(30),
    primary key (CookieName)
    );

create table DeliverdItem(
    PalletID        int,
    OrderID         int,
    CustomerName    varchar(30),
    DeliveryDate    date,
    primary key (PalletID, OrderID),
    foreign key (PalletID) references Pallet(PalletID),
    foreign key (OrderID) references Orders(OrderID)
    );

create table Pallet(
    PalletID    int AUTO_INCREMENT,
    CookieName  varchar(30) not null,
    DateMade    date,
    Blocked     Boolean,
    primary key (PalletID),
    foreign key (CookieName) references Cookie(CookieName)
    );

create table Customer(
    UserName    varchar(30),
    Address     varchar(30) not null,
    primary key (UserName)
    );

create table Orders(
    OrderID         int AUTO_INCREMENT,
    Customer        varchar(30),
    DeliveryDate    date,
    primary key (OrderID),
    foreign key (Customer) references Customer(UserName)
    );

create table OrderedItem(
    OrderID         int,
    CookieName      varchar(30),
    NbrPallets      int not null,
    primary key (OrderID, CookieName),
    foreign key (OrderID) references Orders(OrderID)
    );    
