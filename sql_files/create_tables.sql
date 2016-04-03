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
    AmountLeft      int,
    primary key (IngredientName),
    foreign key (IngredientName) references RecipeItem(IngredientName) 
    );

create table RecipeItem(
    CookieName      varchar(30),
    IngredientName  varchar(30),
    Amount          int,
    primary key (CookieName, IngredientName),
    foreign key (CookieName) references Cookie(CookieName),
    foreign key (IngredientName) references Ingredient(IngredientName)
    );

create table Cookie(
    CookieName  varchar(30),
    primary key (CookieName),
    foreign key (CookieName) references RecipeItem(CookieName)
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
    PalletID    int,
    CookieName  varchar(30),
    DateMade    date,
    Blocked     Boolean,
    primary key (PalletID),
    foreign key (CookieName) references Cookie(CookieName),
    foreign key (PalletID) references DeliverdItem(PalletID)
    );

create table Orders(
    OrderID         int,
    Customer        varchar(30),
    DeliveryDate    date,
    primary key (OrderID),
    foreign key (OrderID) references DeliverdItem(OrderID)
    );



create table Customer(
    UserName    varchar(30),
    Address     varchar(30),
    primary key (UserName)
    );

create table OrderedItem(
    OrderID         int,
    CookieName      varchar(30),
    NbrPallets      int,
    primary key (OrderID, CookieName),
    foreign key (OrderID) references Orders(OrderID)
    );    

set foreign_key_checks = 1;