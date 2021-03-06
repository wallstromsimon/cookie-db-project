insert into Customer (UserName, Address)
values ('Finkakor AB', 'Helsingborg'),
        ('Småbröd AB', 'Malmö'),
        ('Kaffebröd AB', 'Landskrona'),
        ('Bjudkakor AB', 'Ystad'),
        ('Kalaskakor AB', 'Trelleborg'),
        ('Partykakor AB', 'Kristianstad'),
        ('Gästkakor AB', 'Hässleholm'),
        ('Skånekakor AB', 'Perstorp');

insert into Cookie
values ('Nut ring'),
        ('Nut cookie'),
        ('Amneris'),
        ('Tango'),
        ('Almond delight'),
        ('Berliner');

insert into RecipeItem (CookieName, IngredientName, Amount)
values ('Nut ring','Flour', 450),
        ('Nut ring','Butter', 450),
        ('Nut ring','Icing sugar', 190),
        ('Nut ring','Roasted, chopped nuts', 225),
        ('Nut cookie','Fine-ground nuts', 750),
        ('Nut cookie','Ground, roasted nuts', 625),
        ('Nut cookie','Bread crumbs', 125),
        ('Nut cookie','Sugar', 375),
        ('Nut cookie','Egg whites', 350),
        ('Nut cookie','Chocolate', 50),
        ('Amneris','Marzipan', 750),
        ('Amneris','Butter', 250),
        ('Amneris','Eggs', 250),
        ('Amneris','Potato starch', 25),
        ('Amneris','Wheat flour', 25),
        ('Tango','Butter', 200),
        ('Tango','Sugar', 250),
        ('Tango','Flour', 300),
        ('Tango','Sodium bicarbonate', 4),
        ('Tango','Vanilla', 2),
        ('Almond delight','Butter', 400),
        ('Almond delight','Sugar', 270),
        ('Almond delight','Chopped almonds', 279),
        ('Almond delight','Flour', 400),
        ('Almond delight','Cinnamon', 10),
        ('Berliner','Flour', 350),
        ('Berliner','Butter', 250),
        ('Berliner','Icing sugar', 100),
        ('Berliner','Eggs', 50),
        ('Berliner','Vanilla sugar', 5),
        ('Berliner','Chocolate', 50);



insert into Ingredient (IngredientName, AmountLeft)
values ('Flour', 2147483646),
        ('Butter', 2147483646),
        ('Icing sugar', 2147483646),
        ('Roasted, chopped nuts', 2147483646),
        ('Fine-ground nuts', 2147483646),
        ('Ground, roasted nuts', 2147483646),
        ('Bread crumbs', 2147483646),
        ('Sugar', 2147483646),
        ('Egg whites', 2147483646),
        ('Chocolate', 2147483646),
        ('Marzipan', 2147483646),
        ('Eggs', 2147483646),
        ('Potato starch', 2147483646),
        ('Wheat flour', 2147483646),
        ('Sodium bicarbonate', 2147483646),
        ('Vanilla', 2147483646),
        ('Chopped almonds', 2147483646),
        ('Cinnamon', 2147483646),
        ('Vanilla sugar', 2147483646);

insert into Customer (UserNAme,Address)
values ('Simon', 'Lund');

insert into Orders (OrderID,Customer,DeliveryDate)
values (0,'Simon', '2016-05-05');

insert into OrderedItem(OrderID,CookieName,NbrPallets)
values (1, 'Berliner', 1337),
       (1, 'Tango', 2);
