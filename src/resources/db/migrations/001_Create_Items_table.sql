CREATE TABLE items(
  id varchar(8) PRIMARY KEY,
  name varchar(50),
  quantity int,
  buyPrice decimal(7,2),
  sellPrice decimal(7,2),
  available int
);