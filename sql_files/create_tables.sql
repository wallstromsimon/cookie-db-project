create table Ingredient{
    IngredientName  varchar(30),
    AmountLeft      int,
    primary key (IngredientName),
    foreign key (IngredientName) references RecipeItem(IngredientName) 
    };

create table RecipeItem{
    CookieName      varchar(30),
    IngredientName  varchar(30),
    Amount          int,
    primary key (CookieName, IngredientName),
    foreign key (CookieName) references Cookie(CookieName),
    foreign key (IngredientName) references Ingredient(IngredientName)
    };

create table Cookie{
    CookieName  varchar(30),
    primary key (CookieName),
    foreign key (CookieName) references RecipeItem(CookieName)
    };

create table Pallet{
    PalletID    int,
    CookieName  varchar(30),
    DateMade    date,
    Blocked     Boolean,
    primary key (PalletID),
    foreign key (CookieName) references Cookie(CookieName),
    foreign key (PalletID) references DeliverdItem(PalletID)
    };

create table DeliverdItem{
    PalletID        int,
    OrderID         int,
    CustomerName    varchar(30),
    DeliveryDate    date,
    primary key (PalletID,OrderID),
    foreign key (PalletID) references Pallet(PalletID),
    foreign key (OrderID) references Order(OrderID)
    };

create table Order{
    OrderID         int,
    Customer        varchar(30),
    DeliveryDate    date,
    primary key (OrderID),
    foreign key (OrderID) references DeliverdItem(OrderID)
    };

create table Customer{
    UserName    varchar(30),
    Address     varchar(30),
    primary key (UserName)
    };

create table OrderedItem{
    OrderID         int,
    CookieName      varchar(30),
    NbrPallets      int,
    primary key (OrderID,CookieName),
    foreign key (OrderID) references Order(OrderID)
    foreign key (CookieName) references Cookie(CookieName)
    };    

